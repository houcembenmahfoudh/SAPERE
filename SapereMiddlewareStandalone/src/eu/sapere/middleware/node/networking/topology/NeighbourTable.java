package eu.sapere.middleware.node.networking.topology;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import eu.sapere.middleware.agent.BasicSapereAgent;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.networking.transmission.protocols.tcpip.PoolThread;
import eu.sapere.util.ISystemConfiguration;

/**
 * Manages the table of neighbours for the Sapere network.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NeighbourTable {

	private TimeLimitedCacheMap table = null;
	private HashMap<String, Id> lsaTable = null;

	private BasicSapereAgent agent = null;

	/**
	 * @param sMng
	 * @param initialDelay
	 * @param evictionDelay
	 * @param expiryTime
	 * @param unit
	 * @param listener
	 */
	public NeighbourTable(NodeManager sMng, long initialDelay, long evictionDelay, long expiryTime, TimeUnit unit,
			NeighbourListener listener) {
		table = new TimeLimitedCacheMap(initialDelay, evictionDelay, expiryTime, unit, "NeighbourTable", listener);
		lsaTable = new HashMap<String, Id>();
	}

	/**
	 * Removes a neighbour from the Sapere network.
	 * 
	 * @param key
	 *            The ip address of the neighbour to be removed.
	 */
	public void remove(String key) {
		table.remove(key);
		lsaTable.remove(key);

		// remove the sender thread associated with the neighbour
		PoolThread.getInstance().removeSenderThread(key);
	}

	/**
	 * Adds a neighbour to the Sapere network.
	 * 
	 * @param key
	 *            The ip address of the neighbour to be added.
	 * @param data
	 *            The description of the neighbour to be added.
	 */
	public void put(String key, HashMap<String, String> data) {

		HashMap<String, String> oldLsa = (HashMap<String, String>) table.get(key);

		if (oldLsa != null) {

			// try to add new entries to the map
			for (String s : data.keySet()) {
				oldLsa.put(s, data.get(s));
			}

		} else
			oldLsa = data;

		table.put(key, oldLsa);

		Lsa mylsa = new Lsa();

		Iterator<Entry<String, String>> i = oldLsa.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, String> e = i.next();
			mylsa.addProperty(new Property((String) e.getKey(), (String) e.getValue()));
		}

		agent = new BasicSapereAgent("NetworkTopologyManager");

		if (lsaTable.containsKey(key)) {
			agent.updateOperation(mylsa, lsaTable.get(key));
		}

		else {

			mylsa.removeProperty("btMac");

			Id n = agent.injectOperation(mylsa);
			lsaTable.put(key, n);
		}

		// create a new sender thread
		PoolThread.getInstance().addNewThread(key, new Integer(
				NodeManager.getSystemConfiguration().getProperties().getProperty(ISystemConfiguration.NETWORK_PORT))); // to
																														// be
																														// set
																														// in
																														// the))
	}

	/**
	 * Returns the ip address of the neighbour.
	 * 
	 * @param key
	 *            The identifier of the neighbour.
	 * @return The ip address of the neighbour.
	 */
	public Id getIdFromKey(String key) {
		return lsaTable.get(key);
	}

}
