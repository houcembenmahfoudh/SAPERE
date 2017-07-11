package eu.sapere.middleware.agent.remoteconnection;

import java.io.*;
import java.net.*;

/**
 * The server waiting incoming connection for the Remote Connection Manager
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Server {

	/** The Server Scoket */
	static ServerSocket ss = null;

	/** Used to close the server */
	static boolean active = false;

	/**
	 * Starts the server
	 */
	public void startServer() {

		active = true;

		try {

			ss = new ServerSocket(1234);

			while (active) { // Server rimane in attesa

				Socket clientsocket = ss.accept();// Attesa socket

				new ThreadServer(clientsocket).start();

			}
		} catch (Exception e) {

		}
	}

	/**
	 * Stops the server
	 */
	static void stopServer() {
		active = false;
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}