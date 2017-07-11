package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import java.util.HashMap;
import eu.sapere.middleware.lsa.interfaces.ILsa;

/**
 * @author
 *
 */
public final class PoolThread {
	private HashMap<String, ThreadSender> senders;
	private static PoolThread singleton = null;
	/**
	 * Number of Instance 
	 */
	public static int nInstances = 0;
	/**
	 * Number of Thread
	 */
	public static int nThread = 0;

	/**
	 * @return
	 */
	public static PoolThread getInstance() {
		if (singleton == null)
			singleton = new PoolThread();
		nInstances++;
		return singleton;
	}

	private PoolThread() {
		senders = new HashMap<String, ThreadSender>();
	}

	/**
	 * @param ip
	 * @param port
	 */
	public synchronized void addNewThread(String ip, int port) {
		if (senders.containsKey(ip))
			return;

		ThreadSender sender = new ThreadSender(this, ip, port);
		// create a new thread sender
		senders.put(ip, sender);
		sender.start();
		nThread++;
	}

	/**
	 * @param lsa
	 * @param ipDest
	 */
	public synchronized void pushLsa(ILsa lsa, String ipDest) {
		// push an lsa into the queue of the associated thread sender
		senders.get(ipDest).pushLsa(lsa);
	}

	/**
	 * @param s
	 */
	public synchronized void onThreadExit(ThreadSender s) {
		this.senders.remove(s);
	}

	/**
	 * @param ipDest
	 */
	public synchronized void removeSenderThread(String ipDest) {
		ThreadSender sender = this.senders.get(ipDest);
		if (sender == null)
			return;
		// stop the thread
		sender.forceThreadStop();
	}
}
