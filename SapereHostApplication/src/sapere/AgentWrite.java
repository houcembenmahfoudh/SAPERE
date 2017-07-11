package sapere;

import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

public class AgentWrite extends SapereAgent {
	private String gradientDestination;

	public AgentWrite(String propName, String propValue, String gradientDestination) {
		super("MyAgent");
		this.gradientDestination = gradientDestination;
	}

	@Override
	public void setInitialLSA() {
		this.addProperty(new Property("gradientDestination", this.gradientDestination));
		// this.addDirectPropagation();
		// this.addDirectPropagation("Rasp2");
		this.addGradient(10, "+", "light");
		// this.addDynamicGradient("id", 10);
		this.submitOperation();
		System.out.println("--------AgentWrite created--------");

	}

	@Override
	public void onBondAddedNotification(BondAddedEvent event) {

	}

	@Override
	public void onBondRemovedNotification(BondRemovedEvent event) {

	}

	@Override
	public void onBondedLsaUpdateEventNotification(BondedLsaUpdateEvent event) {

	}

	@Override
	public void onPropagationEvent(PropagationEvent event) {

	}

	@Override
	public void onDecayedNotification(DecayedEvent event) {
		// System.out.println("onDecayedNotification");
		// this.lsaId=null;
		// this.setInitialLSA();

	}

	@Override
	public void onLsaUpdatedEvent(LsaUpdatedEvent event) {

	}

	@Override
	public void onReadNotification(ReadEvent readEvent) {
	}

}
