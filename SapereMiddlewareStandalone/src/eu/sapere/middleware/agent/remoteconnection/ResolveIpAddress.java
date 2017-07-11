package eu.sapere.middleware.agent.remoteconnection;

import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueEvent;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

/**
 * A SapereAgent deployed to get the Ip address of a specific neighbor node.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ResolveIpAddress extends SapereAgent {

	private ProxySapereAgent proxy = null;

	/**
	 * Instantiates the Agent
	 * 
	 * @param name
	 *            The name of the Agents
	 */
	public ResolveIpAddress(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Triggers the lookup for the ip address associated to a node name
	 * 
	 * @param name
	 *            The name of the neighbor node
	 * @param proxy
	 *            The callback agent
	 */
	public ResolveIpAddress(String name, ProxySapereAgent proxy) {
		super(name);
		this.proxy = proxy;
	}

	/**
	 * Sets the initial content of the LSA managed by this Agent.
	 * 
	 * @param nodeName
	 *            The name of the neighbor node.
	 */
	public void setInitialLSA(String nodeName) {
		addProperty(new Property("neighbour", nodeName), new Property(
				"ipAddress", "?"));

	}

	@Override
	public void onBondAddedNotification(BondAddedEvent event) {
		String destinationNode = null;
		destinationNode = event.getBondedLsa().getProperty("ipAddress")
				.getValue().elementAt(0);
		this.removeLsa();
		proxy.setIp(destinationNode);
	}

	@Override
	public void onBondRemovedNotification(BondRemovedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPropagationEvent(PropagationEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInitialLSA() {
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

	@Override
	public void propertyValueAppendGenerated(PropertyValueEvent event) {
		// TODO Auto-generated method stub
		
	}

}
