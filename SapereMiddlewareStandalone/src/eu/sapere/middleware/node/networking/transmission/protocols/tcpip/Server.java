package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import eu.sapere.middleware.lsa.Lsa;

/**
 * The tcp/ip server for the Sapere network interface. Multi-thread
 * implementation.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Server extends Thread implements LsaReceived {

	private ServerSocket server;
	private Executor executor;

	private boolean listening = true;
	private LsaReceived listener;

	/**
	 * Instantiates the Server.
	 * 
	 * @param port
	 *            The tcp port.
	 * @param listener
	 *            The listener to be notified of the reception of a Lsa.
	 */
	public Server(int port, LsaReceived listener) {

		this.listener = listener;

		try {
			server = new ServerSocket(port);
			executor = Executors.newCachedThreadPool();
			start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (listening) {
			try {
				Socket socket = server.accept();
				ServerThread thread = new ServerThread(socket, this);
				executor.execute(thread);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLsaReceived(Lsa lsaReceived) {
		listener.onLsaReceived(lsaReceived);
	}

	/**
	 * Terminates this server.
	 */
	public void quit() {

		listening = false;

		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
