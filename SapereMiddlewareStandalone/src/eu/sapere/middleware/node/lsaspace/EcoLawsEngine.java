package eu.sapere.middleware.node.lsaspace;

import java.util.ArrayList;
import java.util.List;

import eu.sapere.middleware.node.lsaspace.ecolaws.Aggregation;
import eu.sapere.middleware.node.lsaspace.ecolaws.Bonding;
import eu.sapere.middleware.node.lsaspace.ecolaws.Decay;
import eu.sapere.middleware.node.lsaspace.ecolaws.GradientAggregation;
import eu.sapere.middleware.node.lsaspace.ecolaws.IEcoLaw;
import eu.sapere.middleware.node.lsaspace.ecolaws.Propagation;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;

/**
 * Manages the exectution of eco-laws.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Graeme Stevenson (STA)
 */
public class EcoLawsEngine {

	/** Identifies a generic eco-law */
	public static final String ECO_LAWS = "EcoLaws";

	/** Identifies the Decay eco-law executor */
	public static final String ECO_LAWS_DECAY = "EcoLawsDecay";

	/** Identifies the Aggregation eco-law executor */
	public static final String ECO_LAWS_AGGREGATION = "EcoLawsAggregation";

	/** Identifies the Propagation eco-law executor */
	public static final String ECO_LAWS_PROPAGATION = "EcoLawsPropagation";

	/** Identifies the gradient eco-law executor */
	public static final String ECO_LAWS_GRADIENT_AGGREGATION = "EcoLawAggregation";

	private final List<IEcoLaw> my_ecoLaws = new ArrayList<IEcoLaw>();

	/**
	 * Creates an instance of the eco-laws engine that manages the execution of
	 * eco-laws.
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
	public EcoLawsEngine(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		// Add the eco-laws in execution order.
		my_ecoLaws.add(new GradientAggregation(space, opManager, notifier, networkDeliveryManager));
		my_ecoLaws.add(new Decay(space, opManager, notifier, networkDeliveryManager));
		my_ecoLaws.add(new Aggregation(space, opManager, notifier, networkDeliveryManager));
		my_ecoLaws.add(new Bonding(space, opManager, notifier, networkDeliveryManager));
		my_ecoLaws.add(new Propagation(space, opManager, notifier, networkDeliveryManager));
	}

	/**
	 * Launches the ordered execution of eco-laws.
	 */
	public void exec() {
		for (IEcoLaw law : my_ecoLaws) {
			law.invoke();
		}
	}
}
