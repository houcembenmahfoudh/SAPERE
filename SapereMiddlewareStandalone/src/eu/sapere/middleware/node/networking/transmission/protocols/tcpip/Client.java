package eu.sapere.middleware.node.networking.transmission.protocols.tcpip;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.values.NeighbourLsa;

/**
 * The tcp/ip client for the Sapere network interface.
 * 
 */
public class Client {

	Lsa deliverLsa = null;
	Lsa remoteNode = null;
	/**
	 * Total time
	 */
	public static long totalTime = 0;
	/**
	 * number of delivered LSA
	 */
	public static int nMex = 0;

	/**
	 * instantiate client
	 */
	public Client() {
		ConnectionManager.getInstance();
	}

	/**
	 * Delivers a Lsa to the node specified.
	 * 
	 * @param deliverLsa
	 *            The Lsa to be delivered.
	 * @param destinationLsa
	 *            The Lsa that describes the destination node.
	 */
	public void deliver(Lsa deliverLsa, Lsa destinationLsa) {
		this.deliverLsa = deliverLsa;
		// System.out.println("deliverLsa"+deliverLsa.getProperty(PropertyName.DIFFUSION_OP.toString()));
		// System.out.println("destinationLsa
		// "+destinationLsa.getProperty(PropertyName.DIFFUSION_OP.toString()));

		try {
			String ip = destinationLsa.getProperty(NeighbourLsa.IP_ADDRESS.toString()).getValue().elementAt(0);

			// PERFORMANCE
			long start = System.currentTimeMillis();

			// get the associated socket if it's existing
			// Socket socket = cManager.getSocketConnection(ip, new
			// Integer(port));
			// Socket socket = new Socket(ip,new Integer(port));
			// ObjectOutputStream oos = new ObjectOutputStream(
			// socket.getOutputStream());
			// oos.writeObject(deliverLsa);
			// socket.close();
			PoolThread.getInstance().pushLsa(deliverLsa, ip);
			totalTime += System.currentTimeMillis() - start;
			nMex++;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
