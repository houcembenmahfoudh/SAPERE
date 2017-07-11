package eu.sapere.middleware.node.lsaspace.ecolaws;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.values.NeighbourLsa;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;

/**
 * The Propagation eco-law implementation.
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class Propagation extends AbstractEcoLaw {
	/**
	 * Creates a new instance of the Propagation eco-law.
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
	public Propagation(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		super(space, opManager, notifier, networkDeliveryManager);
	}

	/**
	 * {@inheritDoc}
	 */
	public void invoke() {
		// System.out.println("LsaList size " + getLSAs().size());
		for (Lsa lsa : getLSAs()) {
			// System.out.println(lsa.toString());
			if (isGradientPropagation(lsa)) {
				// System.out.println("doGradientPropagation");
				doGradientPropagation(lsa);
			} else if (isDirectPropagation(lsa)) {
				// System.out.println("doDirectPropagation");
				doDirectPropagation(lsa);
			}
		}
	}

	/**
	 * Propagate an LSA using direct propagation. The network delivery manager
	 * checks to see if the host is valid so we do not need to do that here.
	 * 
	 * @param an_lsa
	 *            the LSA to propagate.
	 */
	private void doDirectPropagation(Lsa anLsa) {
		String destinationName = anLsa.getSingleValue(PropertyName.DESTINATION.toString());
		for (Lsa lsa : getLSAs()) {
			if (lsa.isNeighbour()) {
				if (!lsa.getFirstProperty(NeighbourLsa.NEIGHBOUR.getText())
						.equals(anLsa.getFirstProperty(PropertyName.PREVIOUS.toString()))) {
					if ((destinationName.equals("all") == false)
							&& lsa.getSingleValue("neighbour").equals(destinationName)) {
						propagate(anLsa, lsa);

					} else if (destinationName.equals("all")) {// all
						propagate(anLsa, lsa);
					}
				}
			}
		}
	}

	private void propagate(Lsa anLsa, Lsa lsa) {
		getNetworkDeliveryManager().doSpread(directPropagationCopy(anLsa), lsa);
		// an_lsa.removeProperty(PropertyName.DIFFUSION_OP.toString());
		if (space.hasVisualInspector())
			this.space.visualPropagate(anLsa.getId());

		// event triggered for each spread
		PropagationEvent event = new PropagationEvent(anLsa);
		publish(event);
	}

	/**
	 * Propagate an LSA using gradient propagation
	 * 
	 * @param an_lsa
	 *            the LSA to propagate.
	 */
	private void doGradientPropagation(Lsa anLsa) {
		for (Lsa lsa : getLSAs()) {
			if (lsa.isNeighbour()) {
				if (!lsa.getFirstProperty(NeighbourLsa.NEIGHBOUR.getText())
						.equals(anLsa.getFirstProperty(PropertyName.PREVIOUS.toString()))) {
					getNetworkDeliveryManager().doSpread(gradientCopy(anLsa), lsa);
				}
			}
		}

		/*
		 * for (Lsa lsa : lsasToSend.values()) {
		 * getNetworkDeliveryManager().doSpread(gradientCopy(an_lsa), lsa); }
		 */
		// an_lsa.addProperty(new Property(PropertyName.DECAY.toString(),"1"));
		// String uuid = an_lsa.getProperty("uuid").getValue().get(0);
		// NetworkReceiverManager.getIgnoreList().put(UUID.fromString(uuid));
		// IF VISUAL INSPECTOR ACTIVE, represent the PROPAGATION
		if (space.hasVisualInspector())
			this.space.visualPropagate(anLsa.getId());
		PropagationEvent event = new PropagationEvent(anLsa);
		publish(event);
	}

	/**
	 * Creates a copy of an LSA to be propagated, removing the id, bonds and
	 * other properties.
	 * 
	 * @param an_lsa
	 *            the LSA to be copied.
	 * @param the_finalHop
	 *            true if this is the final hop for the LSA, false otherwise.
	 * @return the LSA copy.
	 */
	private Lsa gradientCopy(Lsa lsa) {
		Lsa copy = lsa.getCopy();
		copy.removeSyntheticProperty(SyntheticPropertyName.BONDS);
		copy.removeSyntheticProperty(SyntheticPropertyName.CREATION_TIME);
		copy.removeSyntheticProperty(SyntheticPropertyName.CREATOR_ID);
		copy.removeSyntheticProperty(SyntheticPropertyName.LAST_MODIFIED);
		copy.removeSyntheticProperty(SyntheticPropertyName.LOCATION);
		copy.addProperty(new Property(PropertyName.PREVIOUS.toString(), getSpaceName()));
		copy.setId(null);
		int nextHop = getNextHop(copy);
		if (nextHop >= 1) {
			copy.addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "GRADIENT_" + nextHop));
			copy.addProperty(new Property(lsa.getFirstProperty(PropertyName.FIELD_VALUE.toString()), "" + nextHop));
		} else {
			// diffuse the LSA, but diffused LSA won't be propagated further
			copy.removeProperty(PropertyName.DIFFUSION_OP.toString());
		}
		return copy;
	}

	/**
	 * Gets the next hop count for a gradient LSA.
	 * 
	 * @param an_lsa
	 *            the LSA to extract the hop count from.
	 * @return the next hop count.
	 */
	private int getNextHop(Lsa lsa) {

		String op;
		int currentHop = 0;
		// gradient : decrease counter
		if (lsa.hasPropagatotionOp(PropertyName.DIFFUSION_OP.toString())) {
			op = lsa.getSingleValue(PropertyName.DIFFUSION_OP.toString());
			// System.out.println("DIFFUSION_OP "+op);
			currentHop = Integer.parseInt(op.substring(op.indexOf("_") + 1));
			currentHop -= 1;
			// Dynamic gradient : increase counter
		}
		return currentHop;
	}

	/**
	 * Creates a copy of an LSA to be propagated directly, removing the id,
	 * bonds and other properties.
	 * 
	 * @param an_lsa
	 *            the LSA to be copied.
	 * @return the LSA copy.
	 */
	private Lsa directPropagationCopy(final Lsa an_lsa) {
		Lsa copy = an_lsa.getCopy();
		copy.removeSyntheticProperty(SyntheticPropertyName.BONDS);
		copy.removeSyntheticProperty(SyntheticPropertyName.CREATION_TIME);
		copy.removeSyntheticProperty(SyntheticPropertyName.CREATOR_ID);
		copy.removeSyntheticProperty(SyntheticPropertyName.LAST_MODIFIED);
		copy.removeSyntheticProperty(SyntheticPropertyName.LOCATION);
		copy.addProperty(new Property(PropertyName.PREVIOUS.toString(), getSpaceName()));
		// copy.removeProperty(PropertyName.DESTINATION.toString());
		// copy.removeProperty(PropertyName.DIFFUSION_OP.toString());
		copy.setId(null);
		// copy.removeBonds();
		return copy;
	}

	/**
	 * Whether the input LSA has direct propagation properties set.
	 * 
	 * @param an_lsa
	 *            the LSA to check for direct propagation.
	 * @return true if the LSA conforms, false otherwise.
	 */
	private static boolean isDirectPropagation(Lsa lsa) {
		boolean ret = false;
		if (lsa.hasPropagatotionOp(PropertyName.DIFFUSION_OP.toString()))
			ret = lsa.getFirstProperty(PropertyName.DIFFUSION_OP.toString()).equals("direct");
		return ret;
	}

	/**
	 * Whether the input LSA has gradient propagation properties set.
	 * 
	 * @param an_lsa
	 *            the LSA to check for gradient propagation.
	 * @return true if the LSA conforms, false otherwise.
	 */
	private boolean isGradientPropagation(Lsa lsa) {
		boolean ret = false;
		// updated by Houssem
		if (lsa.hasPropagatotionOp(PropertyName.DIFFUSION_OP.toString())) {
			ret = lsa.getFirstProperty(PropertyName.DIFFUSION_OP.toString()).contains("GRADIENT");
		}
		return ret;

	}

}
