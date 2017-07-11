package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import eu.sapere.middleware.lsa.Lsa;

/**
 * Provides an interface for Lsa received by the Sapere network interface.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface LsaReceived {

	/**
	 * Invoked when a Lsa is received.
	 * 
	 * @param lsaReceived
	 *            The received Lsa.
	 */
	public void onLsaReceived(Lsa lsaReceived);

}
