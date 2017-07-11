package eu.sapere.middleware.node.networking.topology;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import eu.sapere.middleware.agent.BasicSapereAgent;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.networking.topology.overlay.Neighbour;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayAnalyzer;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayListener;
import eu.sapere.util.ISystemConfiguration;

/**
 * Manages the topology of the Sapere network.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NetworkTopologyManager implements OverlayListener,
		NeighbourListener {

	// Overlay Network
	private Hashtable<String, TimeLimitedCacheMap> neighbourTables = null;

	// SAPERE node
	private NeighbourTable neighbours = null;
	private NeighbourAnalyzer neightbourAnalyzer = null;

	private Vector<OverlayAnalyzer> overlays = null;

	private static ISystemConfiguration systemConfiguration = null;

	/**
	 * Initialize the NetworkTopologyManager
	 * 
	 * @param sMng
	 *            The NodeManager of the local node.
	 */
	public NetworkTopologyManager(NodeManager sMng) {

		systemConfiguration = NodeManager.getSystemConfiguration();

		neighbourTables = new Hashtable<String, TimeLimitedCacheMap>();
		neighbours = new NeighbourTable(sMng, 10, 2, 30, TimeUnit.SECONDS, this);

		overlays = new Vector<OverlayAnalyzer>();

		// Start CustomOverlayNetwork
		if (systemConfiguration.getProperties() != null) {
			String overlayNet = systemConfiguration.getProperties()
					.getProperty(ISystemConfiguration.OVERLAY);

			if (!overlayNet.isEmpty() && overlayNet != null) {
				String classNameP[] = systemConfiguration.getProperties()
						.getProperty(ISystemConfiguration.OVERLAY).split(";");

				ClassLoader loader = systemConfiguration.getClassLoader();

				try {

					for (String c : classNameP) {

						String className[] = c.split(",");

						if (className.length != 4)
							break;

						TimeLimitedCacheMap t = new TimeLimitedCacheMap(1, 1,
								new Long(new String(className[3].trim())),
								TimeUnit.valueOf(className[2].trim()),
								className[1].trim(), null);
						
						neighbourTables.put(className[1].trim(), t);

						Class<?> myClass = loader
								.loadClass(className[0].trim());

						Constructor<?> constructor = null;

						try {
							constructor = myClass.getConstructor(
									OverlayListener.class, String.class);
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						OverlayAnalyzer myObject = (OverlayAnalyzer) constructor
								.newInstance(this, className[1].trim());
						new Thread(myObject).start();

						overlays.add(myObject);

					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		neightbourAnalyzer = new NeighbourAnalyzer(this, "sapereNetwork",
				neighbourTables);
		new Thread(neightbourAnalyzer).start();
	}

	@Override
	public void onNeighbourFound(Neighbour neighbour, String overlayName) {

		if (overlayName.equals("sapereNetwork")) {
			neighbours.put(neighbour.getIpAddress(), neighbour.getData());
		} else {
			this.neighbourTables.get(overlayName).put(neighbour.getIpAddress(),
					neighbour.getData());
		}
	}

	@Override
	public void onNeighbourExpired(String key, HashMap<String, String> data) {

		Lsa mylsa = new Lsa();

		Iterator<Entry<String, String>> i = data.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, String> e = i.next();
			mylsa.addProperty(new Property((String) e.getKey(), (String) e
					.getValue()));
		}
		mylsa.removeProperty("btMac");
		mylsa.setId(neighbours.getIdFromKey(key));

		BasicSapereAgent agent = new BasicSapereAgent("NetworkTopologyManager");

		agent.removeLsa(mylsa);

		neighbours.remove(key);
	}

	/**
	 * Stops the Network Overlays.
	 */
	public void stop() {
		for (OverlayAnalyzer o : overlays) {
			o.stopOverlay();
		}

	}

}
