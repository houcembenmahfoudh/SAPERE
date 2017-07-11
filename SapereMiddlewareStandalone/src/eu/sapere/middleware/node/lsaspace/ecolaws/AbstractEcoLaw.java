package eu.sapere.middleware.node.lsaspace.ecolaws;

import java.util.Arrays;
import java.util.List;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.lsaspace.EcoLawsEngine;
import eu.sapere.middleware.node.lsaspace.Operation;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;

/**
 * The abstract class for eco-laws.
 * 
 * @author Graeme Stevenson (STA)
 * @author Gabriella Castelli (UNIMORE)
 */
public abstract class AbstractEcoLaw implements IEcoLaw {

	/** The space in which the eco-law executes. */
	protected final Space space;

	/** The local Notifier */
	private Notifier notifier;

	/** The local Operation Manager */
	private OperationManager opManager;

	/** The interface for Network Delivery of LSAs */
	private NetworkDeliveryManager networkDeliveryManager;

	/**
	 * Constructs the Eco-Law.
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
	public AbstractEcoLaw(Space space, OperationManager opManager,
			Notifier notifier, NetworkDeliveryManager networkDeliveryManager) {
		this.space = space;
		this.opManager = opManager;
		this.notifier = notifier;
		this.networkDeliveryManager = networkDeliveryManager;
	}

	/**
	 * Returns the space name.
	 * 
	 * @return the space name.
	 */
	protected String getSpaceName() {
		return space.getName();
	}

	/**
	 * The space in which the eco-law executes.
	 * 
	 * @return the space
	 */
	protected List<Lsa> getLSAs() {
		return Arrays.asList(space.getAllLsa());
	}

	/**
	 * The network delivery manager of the space.
	 * 
	 * @return the network delivery manager
	 */
	protected NetworkDeliveryManager getNetworkDeliveryManager() {
		// return my_nodeManager.getNetworkDeliveryManager();
		return networkDeliveryManager;
	}

	/**
	 * Removes an Lsa from a space.
	 * 
	 * @param lsa
	 *            the lsa to remove
	 */
	protected void remove(Lsa lsa) {
		space.remove(lsa.getId(), getName());
	}

	/**
	 * Injects an Lsa in a space.
	 * 
	 * @param lsa
	 *            the lsa to update
	 */
	protected void inject(final Lsa lsa) {
		final Operation operation = new Operation().injectOperation(lsa,
				getName(), null);
		// my_nodeManager.getOperationManager().execOperation(operation);
		opManager.execOperation(operation);
	}

	/**
	 * Updates an Lsa in a space.
	 * 
	 * @param lsa
	 *            the lsa to update
	 */
	protected void update(final Lsa lsa) {
		final Operation operation = new Operation().updateOperation(lsa,
				lsa.getId(), getName(), null);
		// my_nodeManager.getOperationManager().execOperation(operation);
		opManager.execOperation(operation);
	}

	/**
	 * Updates Lsa bonds in a space.
	 * 
	 * @param lsa
	 *            the lsa to update
	 */
	protected void updateBond(final Lsa lsa) {
		final Operation operation = new Operation().updateBondOperation(lsa,
				lsa.getId(), getName());
		// my_nodeManager.getOperationManager().execOperation(operation);
		opManager.execOperation(operation);
	}

	/**
	 * Updates Lsa parameterised bonds in a space.
	 * 
	 * @param lsa
	 *            the lsa to update
	 */
	protected void updateBondParamaterised(final Lsa lsa) {
		final Operation operation = new Operation()
				.updateBondParametrizedOperation(lsa, lsa.getId(), getName());
		// my_nodeManager.getOperationManager().execOperation(operation);
		opManager.execOperation(operation);
	}

	/**
	 * Publishes an event using the notifier
	 * 
	 * @param event
	 *            the event to publish
	 */
	protected void publish(final AbstractSapereEvent event) {
		// this.my_nodeManager.getNotifier().publish(an_event);
		notifier.publish(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return EcoLawsEngine.ECO_LAWS;
	}

}
