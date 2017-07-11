package eu.sapere.middleware.node.timemachine;

import eu.sapere.middleware.agent.MultithreadSapereAgent;
import eu.sapere.middleware.agent.multithread.ILsaHandler;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.lsa.values.PropertyName;

/**
 * Manages the local Time Machine.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Alberto Rosi (UNIMORE)
 * 
 */
public class TimeMachineManager {

	private ILsaHandler storeHandler = null;
	private ILsaHandler retrieveHandler = null;

	private Storage storage = null;

	/**
	 * Instantiates the local Time Machine
	 */
	public TimeMachineManager() {

		storage = new Storage();

		// Store
		ILsa storeTemplate = new Lsa().addProperty(new Property(
				PropertyName.TIME_MACHINE_STORE.toString(), "?"));
		storeHandler = new StoreAgent(storage);
		new MultithreadSapereAgent("timeMachineStoreAgent", storeHandler,
				storeTemplate); // storeAgent

		// Retrieve
		ILsa retrieveTemplate = new Lsa().addProperty(new Property(
				PropertyName.TIME_MACHINE_RETRIEVE.toString(), "?"));
		retrieveHandler = new RetrieveAgent(storage);
		new MultithreadSapereAgent("timeMachineRetrieveAgent", retrieveHandler,
				retrieveTemplate); // retrieveAgent

	}

}
