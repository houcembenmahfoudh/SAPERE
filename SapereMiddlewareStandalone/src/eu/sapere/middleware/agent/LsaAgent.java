package eu.sapere.middleware.agent;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.lsaspace.Agent;
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
import eu.sapere.middleware.node.notifier.filter.BondedLsaUpdateFilter;
import eu.sapere.middleware.node.notifier.filter.DecayedFilter;

/**
 * Internal class that implements actions for Agents that manages LSA
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class LsaAgent extends Agent implements ISapereAgent {

	/** The managed LSA */
	protected ILsa lsa = null;

	/** The id of the maanged LSA */
	protected Id lsaId = null;

	/** The OperationManager of the local SAPERE node */
	protected OperationManager opMng = null;

	/** The Notifier of the local SAPERE node */
	protected INotifier notifier = null;

	/**
	 * Instantiates this LsaAgent
	 * 
	 * @param agentName
	 *            The name of this Agent
	 */
	public LsaAgent(String agentName) {
		super(agentName);

		this.lsa = new Lsa();
		this.opMng = NodeManager.instance().getOperationManager();
		this.notifier = NodeManager.instance().getNotifier();
	}

	/**
	 * Removes the managed LSA from the local LSA space
	 */
	public void removeLsa() {
		Operation op = new Operation().removeOperation(lsaId.getCopy(), getAgentName());
		opMng.queueOperation(op);

		lsa = new Lsa();
		lsaId = null;
	}

	protected void submitOperation() {
		if (lsaId == null) {
			lsaId = injectOperation();
		} else {
			updateOperation();
		}
	}

	protected Id injectOperation() {
		Id lsaId = null;

		Operation op = new Operation().injectOperation((Lsa) this.lsa, getAgentName(), this);

		lsaId = opMng.queueOperation(op);
		return lsaId;
	}

	protected void updateOperation() {
		// use the already associated LsaId
		Operation op = new Operation().updateOperation((Lsa) this.lsa, lsaId, getAgentName(), this);

		opMng.queueOperation(op);
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {

		if (event.getClass().isAssignableFrom(BondAddedEvent.class)) {

			BondAddedEvent bondAddedEvent = (BondAddedEvent) event;

			Id bonded = new Id(bondAddedEvent.getBondId());
			if (!bonded.isSdId(bonded))
				bonded = new Id(bondAddedEvent.getBondId().substring(0, bondAddedEvent.getBondId().lastIndexOf("#")));

			BondedLsaUpdateEvent bondedLsaUpdateEvent = new BondedLsaUpdateEvent(null);
			BondedLsaUpdateFilter filter = new BondedLsaUpdateFilter(bonded, lsaId.toString());
			Subscription s = new Subscription(bondedLsaUpdateEvent, filter, this, this.getAgentName());

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

			BondedLsaUpdateFilter filter = new BondedLsaUpdateFilter(new Id(bondRemovedEvent.getBondId()),
					lsaId.toString());
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
			Subscription s = new Subscription(decayEvent, filter, this, lsaId.toString());

			notifier.unsubscribe(s);
		}

	}

	@Override
	public String toString() {
		return lsa.toString();
	}

}
