package eu.sapere.middleware.node.timemachine;

import eu.sapere.middleware.agent.multithread.LsaMultiThreadHandler;
import eu.sapere.middleware.agent.multithread.ThreadSapereAgent;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;

/**
 * The StoreAgent is responsible for storing snapshots of the binded Lsa, on a
 * onupdate strategy.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Alberto Rosi (UNIMORE)
 */
public class StoreAgent extends LsaMultiThreadHandler {

	private Storage storage = null;

	/**
	 * Instantiates the StoreAgent
	 * 
	 * @param storage
	 *            The storage manager.
	 */
	public StoreAgent(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void onBond(BondAddedEvent event, ThreadSapereAgent callback) {
		// add lsa entry
		storage.createSnapshot(((Lsa) event.getBondedLsa()));
	}

	@Override
	public void onBondedLsaModified(BondedLsaUpdateEvent event,
			ThreadSapereAgent callback) {
		// store lsa
		storage.createSnapshot(((Lsa) event.getLsa()));
	}

	@Override
	public LsaMultiThreadHandler getHandler() {
		return new StoreAgent(storage);
	}
}
