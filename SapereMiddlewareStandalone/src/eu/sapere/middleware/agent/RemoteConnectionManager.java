package eu.sapere.middleware.agent;

import eu.sapere.middleware.agent.remoteconnection.Server;

/**
 * Act as a skeleton for a Remote Connection Manager, that is accepts incoming
 * active connections from remote nodes, instantiates a Sapere Agent in order to
 * manage the incoming LSA and forwards events to the remote node.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class RemoteConnectionManager implements Runnable {

	/** The name of the Remote Connection Manager */
	protected String agentName = null;

	/** The reference to the server that manages incoming connections */
	private Server server = null;

	/**
	 * Instantiates the Remote Connection Manager
	 * 
	 * @param name
	 *            The name for the Remote Connection Manager
	 */
	public RemoteConnectionManager() {

		this.agentName = "RemoteConnectionManager";

		server = new Server();
		new Thread(this).start();

	}

	public void run() {

		server.startServer();

	}

}
