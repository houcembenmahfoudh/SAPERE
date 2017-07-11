package eu.sapere.middleware.agent;

import java.util.Vector;

import eu.sapere.middleware.agent.implicitrr.IRRAsynchronousService;
import eu.sapere.middleware.agent.implicitrr.IRRService;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.exception.UnresolvedPropertyNameException;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;

/**
 * Abstract Class for Services that implements the Implicit Request-Response
 * interaction Pattern
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class ImplicitRRAgent extends LsaAgent {

	private String potentialPropertyName = null;
	private String formalPropertyName = null;
	private IRRService service = null;

	private IRRAsynchronousService complexService = null;

	/**
	 * @param name
	 *            The name of this Agent
	 */
	public ImplicitRRAgent(String name) {

		super(name);
	}

	/**
	 * Use this method to set the initial content of the LSA managed by this
	 * Agent.
	 */
	public abstract void setInitialLSA();

	private void addLsaProperty(String potentialPropertyName,
			String formalPropertyName, IRRAsynchronousService service) {
		this.potentialPropertyName = potentialPropertyName;
		this.formalPropertyName = formalPropertyName;
		this.complexService = service;

		Property potentialProperty = new Property(potentialPropertyName, "!");
		Property formalProperty = new Property(formalPropertyName, "?");

		lsa.addProperty(potentialProperty);
		lsa.addProperty(formalProperty);

	}

	private void addLsaProperty(String potentialPropertyName,
			String formalPropertyName, IRRService service) {
		this.potentialPropertyName = potentialPropertyName;
		this.formalPropertyName = formalPropertyName;
		this.service = service;

		Property potentialProperty = new Property(potentialPropertyName, "!");
		Property formalProperty = new Property(formalPropertyName, "?");

		lsa.addProperty(potentialProperty);
		lsa.addProperty(formalProperty);

	}

	/**
	 * Adds the required Properties to this Agent
	 * 
	 * @param potentialPropertyName
	 * @param formalPropertyName
	 * @param service
	 */
	public void addProperty(String potentialPropertyName,
			String formalPropertyName, IRRService service) {

		addLsaProperty(potentialPropertyName, formalPropertyName, service);

		submitOperation();
	}

	/**
	 * Adds the required Properties to this Agent
	 * 
	 * @param potentialPropertyName
	 * @param formalPropertyName
	 * @param service
	 * @param properties
	 *            Other properties
	 */
	public void addProperty(String potentialPropertyName,
			String formalPropertyName, IRRService service,
			Property... properties) {

		addLsaProperty(potentialPropertyName, formalPropertyName, service);

		Property p[] = properties;
		for (int i = 0; i < p.length; i++) {
			lsa.addProperty(p[i]);
		}

		submitOperation();

	}

	/**
	 * Adds the required Properties to this Agent
	 * 
	 * @param potentialPropertyName
	 * @param formalPropertyName
	 * @param service
	 */
	public void addProperty(String potentialPropertyName,
			String formalPropertyName, IRRAsynchronousService service) {

		addLsaProperty(potentialPropertyName, formalPropertyName, service);

		submitOperation();
	}

	// private void setFormalPropertyValue(String value){
	// setProperty(new Property(formalPropertyName, value), false);
	// }

	private void setFormalPropertyValue(Vector<String> value) {
		setProperty(new Property(formalPropertyName, value), false);
	}

	private void resetFormalPropertyValue(boolean updateLsa) {
		setProperty(new Property(formalPropertyName, "!"), updateLsa);
	}

	/**
	 * Sets the value of the potential property
	 * 
	 * @param value
	 *            The value to be set
	 */
	public void setPotentialPropertyValue(String value) {
		setProperty(new Property(potentialPropertyName, value), true);
	}

	/**
	 * Sets the value of the potential property
	 * 
	 * @param value
	 *            The values to be set
	 */
	public void setPotentialPropertyValue(Vector<String> value) {
		setProperty(new Property(potentialPropertyName, value), true);
	}

	private void resetPotentialPropertyValue(boolean updateLsa) {
		setProperty(new Property(potentialPropertyName, "?"), updateLsa);
	}

	/**
	 * Resets the Request-Reply Properties
	 */
	public void resetService() {
		resetFormalPropertyValue(false);
		resetPotentialPropertyValue(true);
	}

	private void setProperty(Property p, boolean updateLsa) {

		try {
			lsa.setProperty(p);
			if (updateLsa) {
				submitOperation();
			}
		} catch (UnresolvedPropertyNameException e) {			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onBondAddedNotification(BondAddedEvent event) {

		Vector<String> formalPropertyValue = event.getBondedLsa()
				.getProperty(formalPropertyName).getValue();
		setFormalPropertyValue(formalPropertyValue);

		if (service != null)
			setPotentialPropertyValue(service.provideResponse((Lsa) event
					.getBondedLsa()));
		else
			complexService.onBondedLsaNotification((Lsa) event.getBondedLsa());
	}

	public void onBondRemovedNotification(BondRemovedEvent event) {
		this.resetService();

	}
}
