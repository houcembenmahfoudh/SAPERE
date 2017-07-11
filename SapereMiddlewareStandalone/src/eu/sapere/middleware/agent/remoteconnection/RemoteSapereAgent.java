package eu.sapere.middleware.agent.remoteconnection;

import eu.sapere.middleware.agent.LsaAgent;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.lsaspace.Operation;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.notifier.INotifier;
import eu.sapere.middleware.node.notifier.Subscription;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;
import eu.sapere.middleware.node.notifier.filter.BondedLsaUpdateFilter;
import eu.sapere.middleware.node.notifier.filter.DecayedFilter;
import eu.sapere.middleware.node.notifier.filter.IFilter;

/**
 * The Remote Sapere Agent is in charge of managing local operations for an LSA
 * that has been actively propagated to the local node. Local events are
 * forwarded to the remote Agent.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class RemoteSapereAgent extends LsaAgent {

	/** The LSA managed */
	private ILsa lsa = null;

	/** The id of the LSA managed */
	protected Id lsaId = null;

	/** The reference to the local Operation Manager */
	private OperationManager opMng = null;

	/** The reference to the local Notifier */
	protected INotifier notifier = null;

	/** The associated thread server */
	private ThreadServer thread = null;

	/**
	 * Instantiates the Remote Sapere Agent
	 * 
	 * @param agentName
	 *            The name of the Agent
	 * @param OpMng
	 *            The reference to the local Operation Manager
	 * @param notifier
	 *            The reference to the local Notifier
	 * @param thread
	 *            The associated thread server
	 */
	public RemoteSapereAgent(String agentName, ThreadServer thread) {

		super(agentName);
		this.opMng = NodeManager.instance().getOperationManager();
		this.notifier = NodeManager.instance().getNotifier();
		this.thread = thread;
		this.lsa = new Lsa();

	}

	/**
	 * Sets the Agent LSA
	 * 
	 * @param lsa
	 *            The LSA managed by the Agent
	 */
	public void setLsa(Lsa lsa) {
		this.lsa = lsa;
	}

	@Override
	protected Id injectOperation() {
		Id lsaId = null;

		Operation op = new Operation().injectOperation((Lsa) this.lsa,
				getAgentName(), this);

		lsaId = opMng.queueOperation(op);
		this.lsaId = lsaId;
		return lsaId;
	}

	@Override
	protected void updateOperation() {

		Operation op = new Operation().updateOperation((Lsa) this.lsa, lsaId,
				getAgentName(), null);

		opMng.queueOperation(op);
	}

	@Override
	public void removeLsa() {

		Operation op = new Operation().removeOperation(lsaId, getAgentName());
		opMng.queueOperation(op);

		lsa = new Lsa();
		lsaId = null;
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {

		if (event.getClass().isAssignableFrom(BondAddedEvent.class)) {

			BondAddedEvent bondAddedEvent = (BondAddedEvent) event;

			Id bonded = new Id(bondAddedEvent.getBondId());
			if (!bonded.isSdId(bonded))
				bonded = new Id(bondAddedEvent.getBondId().substring(0,
						bondAddedEvent.getBondId().lastIndexOf("#")));

			BondedLsaUpdateEvent bondedLsaUpdateEvent = new BondedLsaUpdateEvent(
					null);
			BondedLsaUpdateFilter filter = new BondedLsaUpdateFilter(bonded,
					lsaId.toString());
			Subscription s = new Subscription(bondedLsaUpdateEvent, filter,
					this, this.getAgentName());

			notifier.subscribe(s); // sistemare

			onBondAddedNotification(bondAddedEvent);
		}
		if (event.getClass().isAssignableFrom(BondedLsaUpdateEvent.class)) {
			BondedLsaUpdateEvent bondedLsaUpdateEvent = (BondedLsaUpdateEvent) event;
			onBondedLsaUpdateEventNotification(bondedLsaUpdateEvent);
		}
		if (event.getClass().isAssignableFrom(BondRemovedEvent.class)) {
			BondRemovedEvent bondRemovedEvent = (BondRemovedEvent) event;
			onBondRemovedNotification(bondRemovedEvent);

			// Remove Subscription to BondedLSaUpdateEvent

			BondedLsaUpdateFilter filter = new BondedLsaUpdateFilter(new Id(
					bondRemovedEvent.getBondId()), lsaId.toString());
			notifier.unsubscribe(filter);
		}

		if (event.getClass().isAssignableFrom(PropagationEvent.class)) {
			PropagationEvent propagationEvent = (PropagationEvent) event;
			onPropagationEvent(propagationEvent);
		}

		if (event.getClass().isAssignableFrom(LsaUpdatedEvent.class)) {
			LsaUpdatedEvent lsaUpdatedEvent = (LsaUpdatedEvent) event;
			onLsaUpdatedEvent(lsaUpdatedEvent);
		}

		if (event.getClass().isAssignableFrom(DecayedEvent.class)) {
			// remove a reference to my LSA
			lsa = new Lsa();
			lsaId = null;

			// Trigger the Event
			DecayedEvent decayEvent = (DecayedEvent) event;
			onDecayedNotification(decayEvent);

			// Remove the Subscription to DecayedEvent
			DecayedFilter filter = new DecayedFilter(lsaId, this.agentName);
			Subscription s = new Subscription(decayEvent, filter, this,
					lsaId.toString());

			notifier.unsubscribe(s);
		}

	}

	public void onBondAddedNotification(BondAddedEvent event) {

		String checkId = event.getBondId();

		String tentativo = event.getBondId().substring(0,
				event.getBondId().lastIndexOf("#"));

		if (tentativo.contains("#"))
			checkId = tentativo;

		BondedLsaUpdateEvent bondedLsaUpdateEvent = new BondedLsaUpdateEvent(
				null);
		IFilter filter = new BondedLsaUpdateFilter(new Id(checkId),
				this.getAgentName());
		Subscription s = new Subscription(bondedLsaUpdateEvent, filter, this,
				this.getAgentName());

		notifier.subscribe(s); //
		thread.forwardEvent(event);

	}

	public void onBondRemovedNotification(BondRemovedEvent event) {

		thread.forwardEvent(event);
	}

	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event) {

		thread.forwardEvent(event);
	}

	@Override
	public void onDecayedNotification(DecayedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPropagationEvent(PropagationEvent event) {
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

}
