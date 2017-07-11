package eu.sapere.middleware.agent.remoteconnection;

import java.io.*;
import java.net.*;

import eu.sapere.middleware.lsa.Lsa;

/**
 * The thread to manage an incoming connection to the Server
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ThreadServer extends Thread {

	/** The client sockect */
	Socket clientSocket;

	/** The output stream */
	ObjectOutputStream oos;

	/** The input stream */
	ObjectInputStream ois;

	/** Used to close the connection */
	boolean active;

	/** The Remote Sapere Agent that will manage the icoming LSA */
	private RemoteSapereAgent agent = null;

	/**
	 * Instantiates the thread
	 * 
	 * @param client_socket
	 *            The client sockect
	 * @param OpMng
	 *            The local Operation Manager
	 * @param notifier
	 *            The local Notifier
	 */
	public ThreadServer(Socket clientSocket) {

		this.clientSocket = clientSocket;
		agent = new RemoteSapereAgent("remoteAgent_" + this.getId(), this);
	}

	public void run() {

		try {
			Object input;
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
			try {
				while (((input = ois.readObject()) != null) && (!input.equals("clientLeaving"))) {
					Lsa lsa = (Lsa) input;
					agent.setLsa(lsa);

					if (agent.lsaId == null)
						agent.injectOperation();
					else
						agent.updateOperation();
				}
				if (input.equals("clientLeaving"))
					if (agent.lsaId != null) {
						agent.removeLsa();
					}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the thread
	 */
	public void stopThread() {
		if (oos != null) {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (clientSocket != null) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Forwards local Events to the Remote agent that manages remotely the local
	 * LSA
	 * 
	 * @param event
	 *            The event to be forwarded
	 */
	public void forwardEvent(Object event) {
		try {
			oos.writeObject(event);
			// oos.reset();
			// oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}