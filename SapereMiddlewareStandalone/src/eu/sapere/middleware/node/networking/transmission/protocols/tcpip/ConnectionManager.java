package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import java.net.Socket;
import java.util.HashMap;

/**
 * @author
 *
 */
public final class ConnectionManager 
{
	private static ConnectionManager singleton=null;
	private HashMap<String, Socket> sockets;
	
	private ConnectionManager()
	{
		sockets = new HashMap<String, Socket>();
	}
	
	/**
	 * @return
	 */
	public static ConnectionManager getInstance()
	{
		if (singleton == null)
		{
			singleton = new ConnectionManager();	
			return singleton;
		}
		else
			return singleton;
	}

	/**
	 * @param socket
	 * @param ip
	 * @param port
	 */
	public synchronized void addSocket(Socket socket, String ip, int port)
	{
		sockets.put(ip+ ":" + port, socket );
	}
	
	/**
	 * @param ip
	 * @param port
	 * @return
	 */
	public synchronized Socket getSocketConnection(String ip, int port)
	{
		if (sockets.containsKey(ip+ ":"))
			return sockets.get(ip+ ":");
		else
		{
			try
			{
				Socket socket = new Socket(ip, port);
				sockets.put(ip+ ":", socket);
				return socket;
			}
			catch(Exception ex)
			{
				return null;
			}
		}
	}

	
}
