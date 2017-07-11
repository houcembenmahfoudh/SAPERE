package eu.sapere.middleware.agent;

import eu.sapere.middleware.agent.multithread.ILsaHandler;
import eu.sapere.middleware.agent.multithread.LsaMultiThreadHandler;
import eu.sapere.middleware.agent.multithread.ThreadSapereAgent;
import eu.sapere.middleware.agent.multithread.ThreadsMap;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Agent;
import eu.sapere.middleware.node.notifier.INotifier;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;

/**
 * The implementation of a Multi-Thread Sapere Agents. On start-up an LSA that
 * represents the service provided and an associated Thread Agent is created.
 * When a bond event happens, a new Thread Agent is created
 * 
 * Generates new thread agents to handle new bonds
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
// rimettere abstract
public class MultithreadSapereAgent extends Agent implements Runnable {

	/** The reference to the local Operation Manager */
	private OperationManager opMng = null;

	/** The reference to the local Notifier */
	protected INotifier notifier = null;

	/** The Thread Maps of the current Multi-Thread Sapere Agent */
	private ThreadsMap map = null;

	/** The handler for events happening to LSAs */
	private ILsaHandler handler = null;

	/** The LSA that represents the service */
	private ILsa lsa = null;

	/**
	 * Generates a new Thread Agent
	 */
	public void thread() {
		ThreadSapereAgent agent = null;
		Long id = map.put(agent);

		if (handler instanceof LsaMultiThreadHandler) {
			agent = new ThreadSapereAgent(agentName + "_" + id, opMng,
					notifier, this,
					((LsaMultiThreadHandler) handler).getHandler());

		} else
			agent = new ThreadSapereAgent(agentName + "_" + id, opMng,
					notifier, this, handler);

		// agent.setAgentName(""+id);
		agent.setId(id);
		agent.setInitialLSA(((Lsa) lsa).getCopy());
	}

	/**
	 * Sets the reference to the local node's Notifier
	 * 
	 * @param notifier
	 */
	public void setINotifier(INotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Instantiates a new Multi-Thread Sapere Agent
	 * 
	 * @param name
	 *            The name of the agent
	 * @param handler
	 *            The refence to the handler of events that happens to each LSA
	 * @param startingLsa
	 *            The initial LSA that represents the provided service
	 */
	public MultithreadSapereAgent(String name, ILsaHandler handler,
			ILsa startingLsa) {

		super(name);
		this.map = new ThreadsMap();
		this.lsa = ((Lsa) startingLsa).getCopy();
		this.handler = handler;

		this.opMng = NodeManager.instance().getOperationManager();
		this.notifier = NodeManager.instance().getNotifier();

		thread();

	}

	/**
	 * Instantiates a new Multi-Thread Sapere Agent
	 * 
	 * @param name
	 *            The name of the agent
	 * @param handler
	 *            The refence to the handler of events that happens to each LSA
	 * @param startingLsa
	 *            The initial LSA that represents the provided service
	 */
	public MultithreadSapereAgent(String name, LsaMultiThreadHandler handler,
			ILsa startingLsa) {

		super(name);
		this.map = new ThreadsMap();
		this.lsa = ((Lsa) startingLsa).getCopy();
		this.handler = handler;

		this.opMng = NodeManager.instance().getOperationManager();
		this.notifier = NodeManager.instance().getNotifier();

		thread();

	}

	/**
	 * Retrieves the Name of the Agent
	 * 
	 * @return the name of the agent
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * Removes the specified ThreadSapereAgent from the map of active threads
	 * 
	 * @param thread
	 *            the thread to be removed
	 */
	public void removeThread(ThreadSapereAgent thread) {
		this.map.remove(thread.getId());
	}

	public void run() {
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {
		// TODO Auto-generated method stub
		
	}

}
