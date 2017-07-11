package eu.sapere.middleware.agent.multithread;

import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;

/**
 * The class to be implemented to handle a multi-thread LSA as a reaction to
 * events happened (bonds, modification in a bonded LSA, etc.)
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class LsaMultiThreadHandler implements ILsaHandler {
	
	@Override
	public abstract void onBond(BondAddedEvent event, ThreadSapereAgent callback);

	@Override
	public abstract void onBondedLsaModified(BondedLsaUpdateEvent event,
			ThreadSapereAgent callback);

	/**
	 * Returns a new instance of the LSA handler, must be the class that extends
	 * the abstract LsaMultiThreadHandler
	 * 
	 * @return a new instance of the LSA handler.
	 */
	public abstract LsaMultiThreadHandler getHandler();

}
