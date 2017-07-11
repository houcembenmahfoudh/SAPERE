package eu.sapere.middleware.node.networking.transmission.receiver;

import java.util.HashMap;
import java.util.UUID;
import eu.sapere.middleware.agent.BasicSapereAgent;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.networking.transmission.protocols.tcpip.LsaReceived;
import eu.sapere.middleware.node.networking.transmission.protocols.tcpip.Server;
import eu.sapere.util.ISystemConfiguration;

/**
 * Provides the tcp-ip implementation for the receiver interface of the Sapere
 * networking.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NetworkReceiverManager implements LsaReceived {

	// a new ignore list of sent lsas
	private static HashMap<UUID, Integer> ignoreLsaIDs = new HashMap<UUID, Integer>();
	private BasicSapereAgent agent = null;

	/**
	 * Instantiates the NetworkReceiverManager.
	 */
	public NetworkReceiverManager() {

		new Server(new Integer(
				NodeManager.getSystemConfiguration().getProperties().getProperty(ISystemConfiguration.NETWORK_PORT)),
				this);
	}

	/**
	 * @param receivedLsa
	 */
	public void doInject(Lsa receivedLsa) {
		// System.out.println("lsaReceived " +
		// receivedLsa.getProperty(PropertyName.DIFFUSION_OP.toString()));

		agent = new BasicSapereAgent("networkReceiverManager");
		agent.injectOperation(receivedLsa);

	}

	public void onLsaReceived(Lsa lsaReceived) {

		if (lsaReceived.getProperty("uuid") != null) {
			String stringUUID = lsaReceived.getFirstProperty(PropertyName.UUID.toString());
			if (!ignoreLsaIDs.containsKey(UUID.fromString(stringUUID))) {
				if (lsaReceived.getFirstProperty(PropertyName.DIFFUSION_OP.toString()).contains("GRADIENT")) {
					String varName = lsaReceived.getFirstProperty(PropertyName.FIELD_VALUE.toString());
					String sHops = lsaReceived.getFirstProperty(varName);
					int hops = Integer.parseInt(sHops);
					ignoreLsaIDs.put(UUID.fromString(stringUUID), hops);
					doInject(lsaReceived);
				} else {
					// propagation
					ignoreLsaIDs.put(UUID.fromString(stringUUID), 0);
					doInject(lsaReceived);

				}
			} else {
				Lsa lsa = null;
				lsa = NodeManager.instance().getSpace().getLsaById(UUID.fromString(stringUUID));
				int hopsLocal = ignoreLsaIDs.get(UUID.fromString(stringUUID));
				if (hopsLocal != 0) {
					String varName = lsaReceived.getFirstProperty(PropertyName.FIELD_VALUE.toString());

					String sHops = lsaReceived.getFirstProperty(varName);
					int hopsRemote = Integer.parseInt(sHops);

					if (hopsLocal < hopsRemote && lsa != null) {

						lsa.addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "GRADIENT_" + hopsRemote));
						// update other property
						varName = lsaReceived.getFirstProperty(PropertyName.FIELD_VALUE.toString());
						lsa.addProperty(new Property(varName, "" + hopsRemote));
					} else if (hopsLocal < hopsRemote && lsa == null) {
						doInject(lsaReceived);
					}
				}
			}
		} else
			doInject(lsaReceived);
	}

	/**
	 * @return
	 */
	public static HashMap<UUID, Integer> getIgnoreList() {
		return ignoreLsaIDs;
	}

}
