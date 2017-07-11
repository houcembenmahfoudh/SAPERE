package eu.sapere.middleware.node.timemachine;

import eu.sapere.middleware.agent.multithread.LsaMultiThreadHandler;
import eu.sapere.middleware.agent.multithread.ThreadSapereAgent;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;

/**
 * The RetrieveAgent is responsible for storing snapshots of the binded Lsa, on
 * a onupdate strategy.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Alberto Rosi (UNIMORE)
 */
public class RetrieveAgent extends LsaMultiThreadHandler {

	private Storage storage = null;
	
	String sdName = null;
	String propertyName = null;

	/**
	 * Instantiates the RetrieveAgent.
	 * 
	 * @param storage
	 *            The storage manager.
	 */
	public RetrieveAgent(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void onBond(BondAddedEvent event, ThreadSapereAgent callback) {
		
		sdName = ((Lsa) event.getBondedLsa())
				.getSubDescriptionName(new Id(event.getBondId()));

		propertyName = ((Lsa) event.getBondedLsa())
				.getSubDescriptionByName(sdName)
				.getProperty(PropertyName.TIME_MACHINE_RETRIEVE.toString())
				.getValue().elementAt(0);

		Lsa[] retrievedLsa = storage.retrieveSnapshot((Lsa) event
				.getBondedLsa());

		// Call the Storage here
		for (int i = 0; i < retrievedLsa.length; i++) {
			callback.addSubDescription(false,
					"historic-" + propertyName + "-" + i,
					new Property(propertyName, ""
							+ retrievedLsa[i].getProperty("gps")
									.getPropertyValue()),
					new Property("timestamp", ""
							+ retrievedLsa[i].getSyntheticProperty(
									SyntheticPropertyName.LAST_MODIFIED)
									.getPropertyValue()));
		}
		callback.submit();
	}

	@Override
	public void onBondedLsaModified(BondedLsaUpdateEvent event,
			ThreadSapereAgent callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public LsaMultiThreadHandler getHandler() {
		return new RetrieveAgent(storage);
	}
}
