package eu.sapere.middleware.agent.multithread;

import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;

/**
 * The interface to be implemented to handle a multi-thread LSA as a reaction to
 * events happened (bonds, modification in a bonded LSA, etc.)
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface ILsaHandler {

	/**
	 * The method called on a Bond notification.
	 * 
	 * @param event
	 *            The BondAddedEvent
	 * @param callback
	 *            the thread in charge of managing the LSA
	 */
	public void onBond(BondAddedEvent event, ThreadSapereAgent callback);

	/**
	 * The method called on the modification of a bonded LSA
	 * 
	 * @param event
	 *            The BondedLsaUpdateEvent
	 * @param callback
	 *            the thread in charge of managing the LSA
	 */
	public void onBondedLsaModified(BondedLsaUpdateEvent event,
			ThreadSapereAgent callback);

}
