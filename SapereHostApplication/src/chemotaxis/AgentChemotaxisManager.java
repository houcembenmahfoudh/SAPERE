package chemotaxis;

import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

public class AgentChemotaxisManager extends SapereAgent {
	private String agentName;
	private String previous;
	private AgentInjectorChemotaxis chemotaxisInjector;

	public AgentChemotaxisManager(String agentName) {
		super(agentName);
		this.agentName = agentName;
	}

	@Override
	public void setInitialLSA() {
		this.addSubDescription("sub1", new Property("gradientDestination", "*"));
		this.addSubDescription("sub2", new Property("chemoDestination", "*"));
		this.submitOperation();
		System.out.println("--------AgentRead created--------");
	}

	@Override
	public void onBondAddedNotification(BondAddedEvent event) {

		System.out.println("****************");

		if (event.getBondedLsa().getFirstProperty(PropertyName.DIFFUSION_OP.toString()).contains("GRADIENT"))
			this.previous = event.getBondedLsa().getFirstProperty(PropertyName.PREVIOUS.toString());

		System.out.println("gradientDestination " + event.getBondedLsa().getProperty("gradientDestination"));
		System.out.println("Value " + event.getBondedLsa().getProperty(PropertyName.DIFFUSION_OP.toString()));
		System.out.println("Source " + event.getBondedLsa().getProperty(PropertyName.SOURCE.toString()));
		System.out
				.println("AGGREGATION_OP " + event.getBondedLsa().getProperty(PropertyName.AGGREGATION_OP.toString()));
		System.out.println("previous " + previous);
		System.out.println("chemoDestination " + event.getBondedLsa().getProperty("chemoDestination"));

		if (event.getBondedLsa().getProperty("gradientDestination") != null) {
			if (agentName.equals(event.getBondedLsa().getFirstProperty("gradientDestination"))) {
				chemotaxisInjector = new AgentInjectorChemotaxis(previous);
				chemotaxisInjector.setInitialLSA();
			}
		}
		if (event.getBondedLsa().getProperty("chemoDestination") != null && chemotaxisInjector == null) {
			chemotaxisInjector = new AgentInjectorChemotaxis(previous);
			chemotaxisInjector.setInitialLSA();
			// addDirectPropagation(previous);
		}
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
	}

	@Override
	public void onLsaUpdatedEvent(LsaUpdatedEvent event) {
	}

	@Override
	public void onReadNotification(ReadEvent readEvent) {
	}

}
