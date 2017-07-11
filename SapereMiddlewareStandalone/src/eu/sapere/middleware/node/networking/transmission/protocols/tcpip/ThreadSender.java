package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import eu.sapere.middleware.lsa.interfaces.ILsa;

/**
 * @author houssem
 *
 */
public class ThreadSender extends Thread {
	// queue of lsa waiting for being sent
	private Queue<ILsa> queue;
	// ip and port of the destination node
	//
	private String ipDest;
	private int port;
	private PoolThread pool;
	private Boolean on;

	/**
	 * @param pool
	 * @param ipDest
	 * @param port
	 */
	public ThreadSender(PoolThread pool, String ipDest, int port) {
		this.ipDest = ipDest;
		this.port = port;
		this.pool = pool;
		queue = new LinkedList<ILsa>();
		on = true;
	}

	public void run() {
		// get the next element
		while (on) {
			try {
				ILsa nextLsa;
				synchronized (this) {
					nextLsa = queue.poll();

					if (nextLsa == null) {
						this.wait();
						continue;
					}
				}
				// send this lsa
				on=sendLsa(nextLsa);
			} catch (InterruptedException ex) {
				on = false;
			}
		}
		// send a message to the pool
		pool.onThreadExit(this);
	}

	/**
	 * @param lsa
	 */
	public synchronized void pushLsa(ILsa lsa) {
		queue.add(lsa);
		this.notify();
	}

	private Boolean sendLsa(ILsa lsa) {
		try {
			Socket socket = new Socket(ipDest, port);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(lsa);
			socket.close();

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 */
	public synchronized void forceThreadStop() {
		on = false;
		this.notify();
	}

	/**
	 * @return
	 */
	public String getIpNode() {
		return this.ipDest;
	}

	/**
	 * @return
	 */
	public int getPortNode() {
		return this.port;
	}
}
