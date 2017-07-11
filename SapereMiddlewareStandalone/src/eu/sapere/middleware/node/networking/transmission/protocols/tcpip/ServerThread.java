package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import java.io.IOException;
import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.Socket;

import eu.sapere.middleware.lsa.Lsa;

/**
 * The thread that manages a connection received by the server.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ServerThread extends Thread {

	private Socket socket = null;
	private LsaReceived listener;

	/**
	 * Instantiates a ServerThread.
	 * 
	 * @param socket
	 *            The socket
	 * @param listener
	 *            The listener to be notified with received Lsas.
	 */
	public ServerThread(Socket socket, LsaReceived listener) {
		super("Server");
		this.listener = listener;
		ConnectionManager.getInstance();
		// get the ip of the remote space
		//String ip = socket.getRemoteSocketAddress().toString();
		// get the port of the remote space
		//int port = socket.getPort();
		// add the socket to the list
		//cManager.addSocket(socket, ip, port);
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());

			Lsa receivedLsa = (Lsa) ois.readObject();
			listener.onLsaReceived(receivedLsa);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
