package eu.sapere.middleware.agent;

import java.util.Vector;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.SubDescription;
import eu.sapere.middleware.lsa.values.PropertyName;
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
 * Abstract Class for Services that implements the Implicit Request-Response
 * interaction Pattern
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class TimeMachineAgent extends SapereAgent {

	boolean isSd = false;

	private boolean timeMachineEnabled;

	private Vector<String> historicPropertyNames = null;

	/**
	 * @param name
	 *            The name of this Agent
	 */
	public TimeMachineAgent(String name) {

		super(name);
		timeMachineEnabled = false;
		historicPropertyNames = new Vector<String>();
	}

	/**
	 * Use this method to set the initial content of the LSA managed by this
	 * Agent.
	 */
	public abstract void setInitialLSA();

	/**
	 * Enables the time machine on the specified Property.
	 * 
	 * @param propertyName
	 *            The name of the property available as historic.
	 */
	public void enableTimeMachineOnProperty(String propertyName) {

		if (!((Lsa) lsa).hasProperty(propertyName))
			return;

		if (!timeMachineEnabled) { // Lsa stored only once
			// enables the storing for the whole Lsa
			addProperty(new Property(
					PropertyName.TIME_MACHINE_STORE.toString(), "yes"));
			timeMachineEnabled = true;
		}

		if (!((Lsa) lsa).hasSubDescription("historic-" + propertyName))
			addSubDescription(propertyName, new Property("historic-"
					+ propertyName, "on-update"));

		historicPropertyNames.add(propertyName);
	}

	// public void addSubDescription(String name, String potentialPropertyName,
	// String formalPropertyName, IRRService service){
	// addLsaSubDescription(name, potentialPropertyName, formalPropertyName,
	// service);
	//
	// submitOperation ();
	// }
	//
	// public void addSubDescription(String name, String potentialPropertyName,
	// String formalPropertyName, IRRAsynchronousService service){
	// addLsaSubDescription(name, potentialPropertyName, formalPropertyName,
	// service);
	//
	// submitOperation ();
	// }

	//
	// private void addLsaSubDescription (String name, String
	// potentialPropertyName, String formalPropertyName, IRRAsynchronousService
	// service){
	// this.potentialPropertyName = potentialPropertyName;
	// this.formalPropertyName = formalPropertyName;
	// this.complexService = service;
	//
	// this.isSd = true;
	// this.sdName = name;
	//
	// Property potentialProperty = new Property(potentialPropertyName, "!");
	// Property formalProperty = new Property(formalPropertyName, "?");
	//
	// addSubDescription(name, potentialProperty, formalProperty);
	//
	// }
	//
	// private void addLsaSubDescription (String name, String
	// potentialPropertyName, String formalPropertyName, IRRService service){
	// this.potentialPropertyName = potentialPropertyName;
	// this.formalPropertyName = formalPropertyName;
	// this.service = service;
	//
	// this.isSd = true;
	// this.sdName = name;
	//
	// Property potentialProperty = new Property(potentialPropertyName, "!");
	// Property formalProperty = new Property(formalPropertyName, "?");
	//
	// addSubDescription(name, potentialProperty, formalProperty);
	//
	// }
	//
	// private void setFormalPropertyValue(Vector<String> value){
	// setProperty(new Property(formalPropertyName, value), false);
	// }
	//
	// private void resetFormalPropertyValue(boolean updateLsa){
	// setProperty(new Property(formalPropertyName, "!"), updateLsa);
	// }
	//
	// /**
	// * Sets the value of the potential property
	// * @param value The value to be set
	// */
	// public void setPotentialPropertyValue(String value){
	// setProperty(new Property(potentialPropertyName, value), true);
	// }
	//
	//
	// /**
	// * Sets the value of the potential property
	// * @param value The values to be set
	// */
	// public void setPotentialPropertyValue(Vector<String> value){
	// setProperty(new Property(potentialPropertyName, value), true);
	// }
	//
	// private void resetPotentialPropertyValue(boolean updateLsa){
	// setProperty(new Property(potentialPropertyName, "?"), updateLsa);
	// }
	//
	// /**
	// * Resets the Request-Reply Properties
	// */
	// public void resetService(){
	// resetFormalPropertyValue(false);
	// resetPotentialPropertyValue(true);
	// }
	//
	// private void setProperty(Property p, boolean updateLsa){
	//
	// try {
	// if (this.isSd){
	// SubDescription sd = lsa.getSubDescriptionByName(sdName);
	// sd.setProperty(p);
	// }
	// else
	// lsa.setProperty(p);
	//
	// if(updateLsa){
	// submitOperation ();
	// }
	// } catch (UnresolvedPropertyNameException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public void onNotification(AbstractSapereEvent event) {

		if (event.getClass().isAssignableFrom(BondAddedEvent.class)) {

			Lsa bondedLsa = (Lsa) ((BondAddedEvent) event).getBondedLsa();

			boolean isTimeMachineRetrieve = bondedLsa
					.hasProperty(PropertyName.TIME_MACHINE_RETRIEVE.toString());
			boolean isTimeMachineStore = bondedLsa
					.hasProperty(PropertyName.TIME_MACHINE_STORE.toString());
			boolean isTimeMachineRequester = false;

			String propertyName = null;

			// Set isTimeMachineRequester
			for (String p : historicPropertyNames) {
				if (bondedLsa.hasProperty("historic-" + p)) {
					propertyName = p;
					isTimeMachineRequester = true;
					break;
				}
			}

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

			if (!isTimeMachineRetrieve && !isTimeMachineStore
					&& !isTimeMachineRequester) {
				onBondAddedNotification(bondAddedEvent);
			}
			if (isTimeMachineRetrieve) {
				manageTimeMachineRetrieve((Lsa) ((BondAddedEvent) event)
						.getBondedLsa());
			}
			if (isTimeMachineStore) {
			}
			if (isTimeMachineRequester) {
				manageTimeMachineRequester(propertyName);
			}

		}
		if (event.getClass().isAssignableFrom(BondedLsaUpdateEvent.class)) {

			Lsa bondedLsa = (Lsa) ((BondedLsaUpdateEvent) event).getLsa();
			boolean isTimeMachineRetrieve = bondedLsa
					.hasProperty(PropertyName.TIME_MACHINE_RETRIEVE.toString());

			if (!isTimeMachineRetrieve) {
				BondedLsaUpdateEvent bondedLsaUpdateEvent = (BondedLsaUpdateEvent) event;
				onBondedLsaUpdateEventNotification(bondedLsaUpdateEvent);
			} else
				this.manageTimeMachineRetrieve((Lsa) ((BondedLsaUpdateEvent) event)
						.getLsa());
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

	private void manageTimeMachineRetrieve(Lsa bondedLsa) {
		SubDescription[] sds = bondedLsa.getSubDescriptions();

		for (SubDescription sd : sds)
			addSubDescription(false, sd.getName(), sd.getProperties());
		submitOperation();
		if (sds.length > 0){
			String propertyName = sds[0].getName().substring(sds[0].getName().indexOf("-")+1, sds[0].getName().lastIndexOf("-"));
			removeTimeMachineRequester(propertyName);
		}
		
	}

	private void manageTimeMachineRequester(String propertyName) {
		addSubDescription("historic-" + propertyName + "-retrieve",
				new Property(PropertyName.TIME_MACHINE_RETRIEVE.toString(),
						propertyName));
	}
	
	private void removeTimeMachineRequester(String propertyName) {
		this.removeSubDescription("historic-" + propertyName + "-retrieve");
	}

	/**
	 * Adds a SubDescription with a variable number of Properties.If submit is false, enables to submit
	 * multiple SubDescription in a single update operation.
	 * @param submit True to submit the update of Lsa, false otherwise 
	 * @param name
	 * @param values
	 */
	public void addSubDescription(boolean submit, String name, Property... values) {
		lsa.addSubDescription(name, values);
		if(submit)
			submitOperation();
	}
	
	/**
	 * Updates the lsa in the local Sapere node. 
	 */
	public void submit(){
		submitOperation();
	}

}
