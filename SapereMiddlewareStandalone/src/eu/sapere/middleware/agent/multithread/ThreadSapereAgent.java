package eu.sapere.middleware.agent.multithread;

import eu.sapere.middleware.agent.MultithreadSapereAgent;
import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueEvent;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.notifier.INotifier;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

/**
 * The implementation of a multi-thread Agent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ThreadSapereAgent extends SapereAgent {

	protected MultithreadSapereAgent parent = null;

	private ILsaHandler handler = null;

	private Long id = null;

	/**
	 * Instantiates a Thread Agent
	 * 
	 * @param name
	 * 
	 * @param opMng
	 *            The reference to the Operation Manager
	 * @param notifier
	 *            The reference to the Notifier
	 * @param parent
	 *            The reference to the Parent Sapere Agent
	 * @param handler
	 *            The handler for events that happens to the Thread Agent
	 */
	public ThreadSapereAgent(String name, OperationManager opMng, INotifier notifier, MultithreadSapereAgent parent,
			ILsaHandler handler) {

		super(name);

		this.opMng = opMng;
		this.notifier = notifier;
		this.parent = parent;
		this.handler = handler;
	}

	/**
	 * Sets the id for the Thread Agent
	 * 
	 * @param id
	 *            The id of the Thread
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The initial LSA
	 * 
	 * @param startingLsa
	 *            The initial LSA
	 */
	public void setInitialLSA(ILsa startingLsa) {

		this.lsa = (Lsa) startingLsa;

		this.submitOperation();
	}

	@Override
	public void setInitialLSA() { // not used
	}

	@Override
	public void onBondAddedNotification(BondAddedEvent event) {
		((LsaMultiThreadHandler) handler).onBond(event, this);

		parent.thread();
	}

	@Override
	public void onBondRemovedNotification(BondRemovedEvent event) {
		this.removeLsa();
		parent.removeThread(this);
	}

	@Override
	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event) {
		handler.onBondedLsaModified(event, this);
	}

	@Override
	public void propertyValueAppendGenerated(PropertyValueEvent event) {
		// TODO Auto-generated method stub

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

	/**
	 * Adds a SubDescription with a single Property. If submit is false, enables
	 * to submit multiple SubDescription in a single update operation.
	 * 
	 * @param submit
	 *            True to submit the update of Lsa, false otherwise
	 * @param name
	 * @param value
	 */
	public void addSubDescription(boolean submit, String name, Property value) {
		lsa.addSubDescription(name, value);
		if (submit)
			submitOperation();
	}

	/**
	 * Adds a SubDescription with a variable number of Properties.If submit is
	 * false, enables to submit multiple SubDescription in a single update
	 * operation.
	 * 
	 * @param submit
	 *            True to submit the update of Lsa, false otherwise
	 * @param name
	 * @param values
	 */
	public void addSubDescription(boolean submit, String name, Property... values) {
		lsa.addSubDescription(name, values);
		if (submit)
			submitOperation();
	}

	/**
	 * Updates the lsa in the local Sapere node.
	 */
	public void submit() {
		submitOperation();
	}

}
