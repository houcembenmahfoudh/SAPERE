package eu.sapere.middleware.agent.remoteconnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.agent.SapereAgentRNM;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueEvent;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;
import eu.sapere.middleware.node.notifier.filter.BondedLsaUpdateFilter;

/**
 * Acts as a proxy to forward the Lsa to the remote node and manages
 * notifications back to a SapereAgentRNM.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ProxySapereAgent extends SapereAgent implements Runnable {

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	private SapereAgentRNM callingAgent = null;
	private ResolveIpAddress agent = null;

	private String ip = null;

	private boolean isOn = false;
	private Thread t = null;

	private Queue<ILsa> lsaQueue = null; // FIFO Queues

	/**
	 * Instantiates the proxy.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param nodeName
	 *            The name of the remote node.
	 * @param sapereAgentRNM
	 *            The SapereAgentRNM that manages a remote Lsa.
	 */
	public ProxySapereAgent(String name, String nodeName,
			SapereAgentRNM sapereAgentRNM) {
		super(name);

		this.callingAgent = sapereAgentRNM;
		this.lsaQueue = new ConcurrentLinkedQueue<ILsa>();

		getIpFromNodeName(nodeName);
	}

	private void getIpFromNodeName(String nodeName) {
		agent = new ResolveIpAddress("agentIp", this);
		agent.setInitialLSA(nodeName);
	}

	private void emptyQueue() {
		Iterator<ILsa> iterator = lsaQueue.iterator();
		while (iterator.hasNext()) {
			ILsa nextLsa = (ILsa) iterator.next();
			lsaQueue.poll();
			forward(nextLsa);
		}
	}

	/**
	 * Sets the Ip address of the remote node.
	 * 
	 * @param ip
	 *            The ip address of the remote node.
	 */
	public void setIp(String ip) {
		this.ip = ip;
		openConnection(ip);

		if (!lsaQueue.isEmpty())
			emptyQueue();
	}

	@Override
	public String getAgentName() {
		return agentName;
	}

	/**
	 * Sets the Lsa to be managed.
	 * 
	 * @param lsa
	 *            The lsa.
	 */
	public void setLsa(Lsa lsa) {
		this.lsa = lsa;
	}

	/**
	 * Forwards the Lsa of the agent to the remote node.
	 */
	public void forward() {

		if (ip != null)
			sendData(lsa);
		else
			lsaQueue.add(lsa);
	}

	private void forward(ILsa lsa) {
		sendData(lsa);
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {

		if (event.getClass().isAssignableFrom(BondAddedEvent.class)) {

			BondAddedEvent bondAddedEvent = (BondAddedEvent) event;

			Id bonded = new Id(bondAddedEvent.getBondId());
			if (!bonded.isSdId(bonded))
				bonded = new Id(bondAddedEvent.getBondId().substring(0,
						bondAddedEvent.getBondId().lastIndexOf("#")));

			callingAgent.onBondAddedNotification(bondAddedEvent);
		}
		if (event.getClass().isAssignableFrom(BondedLsaUpdateEvent.class)) {
			BondedLsaUpdateEvent bondedLsaUpdateEvent = (BondedLsaUpdateEvent) event;
			callingAgent
					.onBondedLsaUpdateEventNotification(bondedLsaUpdateEvent);
		}
		if (event.getClass().isAssignableFrom(BondRemovedEvent.class)) {
			BondRemovedEvent bondRemovedEvent = (BondRemovedEvent) event;
			callingAgent.onBondRemovedNotification(bondRemovedEvent);

			// Remove Subscription to BondedLSaUpdateEvent
			BondedLsaUpdateFilter filter = new BondedLsaUpdateFilter(new Id(
					bondRemovedEvent.getBondId()), lsaId.toString());
			notifier.unsubscribe(filter);
		}

		if (event.getClass().isAssignableFrom(PropagationEvent.class)) {
			PropagationEvent propagationEvent = (PropagationEvent) event;
			callingAgent.onPropagationEvent(propagationEvent);
		}

		if (event.getClass().isAssignableFrom(LsaUpdatedEvent.class)) {
			LsaUpdatedEvent lsaUpdatedEvent = (LsaUpdatedEvent) event;
			callingAgent.onLsaUpdatedEvent(lsaUpdatedEvent);
		}

		if (event.getClass().isAssignableFrom(DecayedEvent.class)) {
			// remove a reference to my LSA
			lsa = new Lsa();
			lsaId = null;

			// Trigger the Event
			DecayedEvent decayEvent = (DecayedEvent) event;
			callingAgent.onDecayedNotification(decayEvent);
		}

	}

	public void onBondAddedNotification(BondAddedEvent event) {

	}

	public void onBondRemovedNotification(BondRemovedEvent event) {
	}

	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event) {
	}

	private void sendData(Object input) {
		try {
			oos.writeObject(input);
			oos.reset();
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Socket Part
	private void openConnection(String ip) {

		try {

			socket = new Socket(ip, 1234);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.isOn = true;
		t = new Thread(this);
		t.start();

	}

	/**
	 * Closes the socket connection.
	 */
	public void closeConnection() {

		try {
			isOn = false;

			oos.writeObject("clientLeaving");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (oos != null) {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (socket != null) {
			try {

				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		Object input;

		try {
			try {
				while (isOn && ((input = ois.readObject()) != null)) {
					AbstractSapereEvent event = (AbstractSapereEvent) input;
					onNotification(event);

				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void propertyValueAppendGenerated(PropertyValueEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInitialLSA() {
		// not used here

	}

	@Override
	public void onPropagationEvent(PropagationEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDecayedNotification(DecayedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLsaUpdatedEvent(LsaUpdatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReadNotification(ReadEvent readEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyValueGenerated(PropertyValueEvent event) {
		// TODO Auto-generated method stub
		
	}

}
