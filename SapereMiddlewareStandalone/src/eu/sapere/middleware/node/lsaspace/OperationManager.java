package eu.sapere.middleware.node.lsaspace;

import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.notifier.Subscription;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;
import eu.sapere.middleware.node.notifier.filter.BondAddedFilter;
import eu.sapere.middleware.node.notifier.filter.BondRemovedFilter;
import eu.sapere.middleware.node.notifier.filter.DecayedFilter;
import eu.sapere.middleware.node.notifier.filter.LsaUpdatedFilter;
import eu.sapere.middleware.node.notifier.filter.PropagationFilter;
import eu.sapere.middleware.node.notifier.filter.ReadFilter;

/**
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class OperationManager {

	private Queue<Operation> operationsQueue = null; // FIFO Queues operation
														// requested by
														// SapereAgents

	private Space space = null;
	private Notifier notifier = null;

	private long opTime = 5000;

	private long sleepTime = 1;

	/**
	 * Creates an instance of the Operation Manager
	 * 
	 * @param space
	 *            the local LSA Space
	 * @param notifier
	 *            the Notifier
	 * @param opTime
	 *            the operation time
	 */
	public OperationManager(Space space, Notifier notifier, long opTime) {

		this.operationsQueue = new ConcurrentLinkedQueue<Operation>(); // This
																		// FIFO
																		// queue
																		// is
																		// synchronized
																		// AND
																		// NON
																		// BLOCKING
		this.space = space;
		this.notifier = notifier;
		this.opTime = opTime;

	}

	/**
	 * Launches the execution of the operation submitted immediately.
	 * 
	 * @param operation
	 *            the operation to be executed
	 * @return the Id of the LSA injected if the operation is an inject, null
	 *         otherwise
	 */
	public Id execOperation(Operation operation) {
		Id id = null;

		if (operation.getOpType() == OperationType.Inject) {
			id = space.getFreshId();
			operation.setLsaId(id);

			if (operation.getRequestingAgent() != null) {

				// Subscription to BondAddedEvent
				BondAddedEvent event1 = new BondAddedEvent(null, null, null);
				BondAddedFilter filter1 = new BondAddedFilter(operation.getLsaId(), operation.getRequestingId());
				Subscription s1 = new Subscription(event1, filter1, operation.getRequestingAgent(),
						operation.getRequestingId());
				notifier.subscribe(s1);

			}

			if (operation.getRequestingAgent() != null) {
				BondRemovedEvent event3 = new BondRemovedEvent(null, null);
				BondRemovedFilter filter3 = new BondRemovedFilter(operation.getLsaId(), operation.getRequestingId());
				Subscription s3 = new Subscription(event3, filter3, operation.getRequestingAgent(),
						operation.getRequestingId());
				notifier.subscribe(s3);
			}

		}

		this.execOp(operation);

		return id;
	}

	/**
	 * Adds an operation to the queue of operations to be performed
	 * 
	 * @param operation
	 *            the operation to be queued
	 * @return the Id of the LSA injected if the operation is an inject, null
	 *         otherwise
	 */
	public Id queueOperation(Operation operation) {
		Id id = null;

		if (operation.getOpType() == OperationType.Inject) {
			id = space.getFreshId();
			operation.setLsaId(id);
			if (operation.getRequestingAgent().getAgentName() != null) {
				if ((!operation.getRequestingAgent().getAgentName().equals("DataLsa"))) {

					if (operation.getLsa().hasDecayProperty()) {

						/*
						 * System.out.println("\t" + operation.getLsaId());
						 * System.out.println("\t" +
						 * operation.getRequestingId()); System.out.println("\t"
						 * + operation.getRequestingAgent());
						 */

						DecayedEvent event = new DecayedEvent(null);
						DecayedFilter filter = new DecayedFilter(operation.getLsaId(), operation.getRequestingId());
						Subscription s = new Subscription(event, filter, operation.getRequestingAgent(),
								operation.getRequestingId());
						notifier.subscribe(s);

						// LsaUpdatedEvent event1 = new LsaUpdatedEvent(null);
						// LsaUpdatedFilter filter1 = new
						// LsaUpdatedFilter(operation.getLsaId(),
						// operation.getRequestingId());
						// Subscription s1 = new Subscription(event1.getClass(),
						// filter1, operation.getRequestingAgent(),
						// operation.getRequestingId());
						// notifier.subscribe(s1);
					}

					BondAddedEvent event1 = new BondAddedEvent(null, null, null);
					BondAddedFilter filter1 = new BondAddedFilter(operation.getLsaId(), operation.getRequestingId());
					Subscription s1 = new Subscription(event1, filter1, operation.getRequestingAgent(),
							operation.getRequestingId());
					notifier.subscribe(s1);

					BondRemovedEvent event3 = new BondRemovedEvent(null, null);
					BondRemovedFilter filter3 = new BondRemovedFilter(operation.getLsaId(),
							operation.getRequestingId());
					Subscription s3 = new Subscription(event3, filter3, operation.getRequestingAgent(),
							operation.getRequestingId());
					notifier.subscribe(s3);

					// Subscription to PropagationEvent
					PropagationEvent event11 = new PropagationEvent(null);
					PropagationFilter filter11 = new PropagationFilter(operation.getLsaId());
					Subscription s11 = new Subscription(event11, filter11, operation.getRequestingAgent(),
							operation.getRequestingId());
					notifier.subscribe(s11);

					LsaUpdatedEvent event2 = new LsaUpdatedEvent(null);
					LsaUpdatedFilter filter2 = new LsaUpdatedFilter(operation.getLsaId(), operation.getRequestingId());
					Subscription s2 = new Subscription(event2, filter2, operation.getRequestingAgent(),
							operation.getRequestingId());
					notifier.subscribe(s2);

				}
			}
		}

		if (operation.getOpType() == OperationType.Update) {

			if (operation.getRequestingId() != null) {

				if (operation.getLsa().hasDecayProperty()) {

					DecayedEvent event = new DecayedEvent(null);
					DecayedFilter filter = new DecayedFilter(operation.getLsaId(), operation.getRequestingId());
					Subscription s = new Subscription(event, filter, operation.getRequestingAgent(),
							operation.getRequestingId());
					notifier.subscribe(s);

				}

				LsaUpdatedEvent event1 = new LsaUpdatedEvent(null);
				LsaUpdatedFilter filter1 = new LsaUpdatedFilter(operation.getLsaId(), operation.getRequestingId());
				Subscription s1 = new Subscription(event1, filter1, operation.getRequestingAgent(),
						operation.getRequestingId());
				notifier.subscribe(s1);

			}

		}

		// if(operation.getOpType() == OperationType.Update){
		// if (operation.getLsa().hasDecayProperty() ) {
		// DecayedEvent event = new DecayedEvent(null);
		// DecayedFilter filter = new DecayedFilter(operation.getLsaId(),
		// operation.getRequestingId());
		// Subscription s = new Subscription(event.getClass(), filter,
		// operation.getRequestingAgent(),
		// operation.getRequestingId());
		// notifier.subscribe(s);
		// }
		// }

		if (operation.getOpType() == OperationType.Remove) {
			// Lsa updated by eco-laws (aggregation, basically)
			{
				LsaUpdatedEvent event = new LsaUpdatedEvent(null);
				LsaUpdatedFilter filter = new LsaUpdatedFilter(operation.getLsaId(), operation.getRequestingId());
				Subscription s = new Subscription(event, filter, operation.getRequestingAgent(),
						operation.getRequestingId());
				notifier.unsubscribe(s);
			}
		}

		// Adds the operation to the queue
		operationsQueue.add(operation);

		return id;
	}

	/**
	 * Launches the ordered execution of operations, one by one, until the
	 * operation time is not expired.
	 */
	public void exec() {

		long startTime = new Date().getTime();
		boolean ended = false;

		do {
			if (!operationsQueue.isEmpty()) {
				execNextOp();

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			} else {
				ended = true;
			}
		} while ((new Date().getTime() - startTime < opTime) && (ended == false));

	}

	/**
	 * Executes an operation on the local LSA space
	 * 
	 * @param nextOp
	 *            the operation to be executed
	 */
	private void execOp(Operation nextOp) {

		if (nextOp.getOpType() == OperationType.Inject) {
			space.inject(nextOp.getLsa(), nextOp.getRequestingId());
		}
		if (nextOp.getOpType() == OperationType.Remove) {
			space.remove(nextOp.getLsaId(), nextOp.getRequestingId());
		}
		if (nextOp.getOpType() == OperationType.Read) {

			ReadEvent event = new ReadEvent(null);
			ReadFilter filter = new ReadFilter(nextOp.getLsaId(), nextOp.getRequestingId());
			Subscription s = new Subscription(event, filter, nextOp.getRequestingAgent(), nextOp.getRequestingId());

			notifier.subscribe(s);

			space.read(nextOp.getLsaId(), nextOp.getRequestingId()); // TO DO: e
																		// return
																		// value
																		// is
																		// needed

			notifier.unsubscribe(s);

		}

		// TO DO: check if needed
		if (nextOp.getOpType() == OperationType.Update)
			space.update(nextOp.getLsaId(), nextOp.getLsa(), nextOp.getRequestingId());
		if (nextOp.getOpType() == OperationType.BondUpdate)
			space.update(nextOp.getLsaId(), nextOp.getLsa(), nextOp.getRequestingId(), true, false); // true,
																										// false
		if (nextOp.getOpType() == OperationType.UpdateParametrized)
			space.update(nextOp.getLsaId(), nextOp.getLsa(), nextOp.getRequestingId(), false, true);
		if (nextOp.getOpType() == OperationType.BondUpdateParametrized)
			space.update(nextOp.getLsaId(), nextOp.getLsa(), nextOp.getRequestingId(), false, false); // false,
																										// false

	}

	/**
	 * Pulls out the next operation to be executed by the queue, and launches
	 * its execution.
	 * 
	 * @return true if an operation has been executed, false otherwise
	 */
	private boolean execNextOp() {
		Operation nextOp = null;
		boolean ret = false;

		Iterator<Operation> iterator = operationsQueue.iterator();

		if (iterator.hasNext()) {
			nextOp = (Operation) iterator.next();
			operationsQueue.poll();
			execOp(nextOp);
			ret = true;
		} else
			return false;

		return ret;
	}

	/**
	 * @return
	 */
	public String getSpaceName() {
		return "";
	}

}
