package eu.sapere.middleware.node.lsaspace.ecolaws;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;

/**
 * The Decay eco-law implementation.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Graeme Stevenson (STA)
 */
public class Decay extends AbstractEcoLaw {

	/**
	 * Creates a new instance of the decay eco-law.
	 * 
	 * @param space
	 *            The space in which the eco-law executes.
	 * @param opManager
	 *            The OperationManager that manages operations in the space
	 * @param notifier
	 *            The Notifier that notifies agents whith events happening to
	 *            LSAs
	 * @param networkDeliveryManager
	 *            The interface for Network Delivery of LSAs
	 */
	public Decay(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		super(space, opManager, notifier, networkDeliveryManager);
	}

	/**
	 * {@inheritDoc}
	 */
	public void invoke() {
		for (Lsa lsa : getLSAs()) {
			if (lsa.getSingleValue("decay") != null) {
				applyToLsa(lsa);
			}
		}
	}

	/**
	 * Applies the decay transformation to an LSA.
	 * 
	 * @param an_lsa
	 *            the LSA to decay.
	 */
	private void applyToLsa(final Lsa an_lsa) {

		// decrement the TTL
		int decay = PropertyName.decrement(an_lsa.getDecayProperty());

		if (decay <= 0) {

			// Triggers the DecayedEvent
			AbstractSapereEvent decayEvent = new DecayedEvent(an_lsa);
			decayEvent.setRequiringAgent(null);
			publish(decayEvent);

			remove(an_lsa);

			// Should remove all the Subscriptions for this LSA
		} else {
			an_lsa.addProperty(new Property("decay", String.valueOf(decay)));
			update(an_lsa);

			// Triggers the LsaUpdatedEvent
			AbstractSapereEvent lsaUpdatedEvent = new LsaUpdatedEvent(an_lsa);
			lsaUpdatedEvent.setRequiringAgent(null);
			publish(lsaUpdatedEvent);
		}
	}

}
