package eu.sapere.middleware.node.networking.transmission.delivery;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.networking.transmission.protocols.tcpip.Client;

/**
 * Provides the tcp-ip implementation for the delivery interface of the Sapere
 * networking.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NetworkDeliveryManager {

	/**
	 * Instantiates the NetworkDeliveryManager.
	 */
	public NetworkDeliveryManager() {
	}

	/**
	 * @param deliverLsa
	 * @param destinationLsa
	 * @return
	 */
	public boolean doSpread(Lsa deliverLsa, Lsa destinationLsa) {
		Client client = new Client();
		client.deliver(deliverLsa, destinationLsa);
		return true;
	}

}
