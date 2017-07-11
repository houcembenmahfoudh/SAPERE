package eu.sapere.middleware.node.lsaspace;

import eu.sapere.middleware.node.NodeManager;

/**
 * Manages the local LSA space life cicle
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class SpaceRunner implements Runnable {

	private boolean stop = false;

	private long sleepTime;

	/** The Operation Manager */
	private OperationManager operationManager = null;

	/** The eco-laws engine */
	private EcoLawsEngine ecoLawsEngine = null;

	/**
	 * Creates an instance of the SpaceRunner and initializes the
	 * OperationManager and the Ecolaws Engine
	 * 
	 * @param node
	 *            a reference to the local Node
	 * @param opTime
	 *            the operation time for the OperationManager
	 * @param sleepTime
	 *            the sleepTime for each Middleware Cicle
	 */
	public SpaceRunner(NodeManager node, long opTime, long sleepTime) {
		this.sleepTime = sleepTime;

		// Operation Manager
		operationManager = new OperationManager(node.getSpace(),
				node.getNotifier(), opTime);

		// Ecolaws Engine
		ecoLawsEngine = new EcoLawsEngine(node.getSpace(), operationManager,
				node.getNotifier(), node.getNetworkDeliveryManager());

	}

	@Override
	public void run() {

		while (!stop) {

			operationManager.exec();
			ecoLawsEngine.exec();

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	/**
	 * @return true if the SpaceRunner is stopped, false otherwise
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * Stops the SpaceRunner
	 */
	public void stop() {
		this.stop = true;
	}

	/**
	 * @return a reference to the OperationManager
	 */
	public OperationManager getOperationManager() {
		return operationManager;
	}

	/**
	 * @return a reference to the EcoLawsEngine
	 */
	public EcoLawsEngine getEcoLawsEngine() {
		return ecoLawsEngine;
	}

}