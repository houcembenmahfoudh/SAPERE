package eu.sapere.middleware.agent;

import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

/**
 * Interface for react to events happening in the LSA space
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface ISapereAgent {

	/**
	 * Called when a new Bond happens.
	 * 
	 * @param event
	 *            The BondAddedEvent
	 */
	public void onBondAddedNotification(BondAddedEvent event);

	/**
	 * Called when a Bond is removed.
	 * 
	 * @param event
	 *            The BondRemovedEvent
	 */
	public void onBondRemovedNotification(BondRemovedEvent event);

	/**
	 * CAlled when a binded LSA is updated
	 * 
	 * @param event
	 *            The BondedLsaUpdateEvent
	 */
	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event);

	/**
	 * Called when the LSA is removed by the Decay eco-law
	 * 
	 * @param event
	 *            The DecayedEvent
	 */
	public void onDecayedNotification(DecayedEvent event);

	/**
	 * Called when the LSA is propagated
	 * 
	 * @param event
	 *            The PropagationEvent
	 */
	public void onPropagationEvent(PropagationEvent event);

	/**
	 * Called when the LSA is updated by an eco-law
	 * 
	 * @param event
	 *            The LsaUpdatedEvent
	 */
	public void onLsaUpdatedEvent(LsaUpdatedEvent event);

	/**
	 * Called when a read operation is performed
	 * 
	 * @param readEvent
	 *            The ReadEvent
	 */
	public void onReadNotification(ReadEvent readEvent);

}
