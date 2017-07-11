package chemotaxis;

import java.util.Timer;
import java.util.TimerTask;
import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;
import utils.BulbActuator;

public class AgentInjectorChemotaxis extends SapereAgent {
	private String chemoDestination;

	public AgentInjectorChemotaxis(String chemoDestination) {
		super("MyAgent");
		this.chemoDestination = chemoDestination;
	}

	@Override
	public void setInitialLSA() {
		final BulbActuator bulbActuator = new BulbActuator();
		System.out.println("--------Agentchemo created--------");
		this.removeSubDescription("sub1");
		this.removeSubDescription("sub2");

		Timer timer = new Timer();

		this.addProperty(new Property("chemoDestination", chemoDestination));

		this.addDirectPropagation(chemoDestination);
		try {
			bulbActuator.sendingPostRequest("10");
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						bulbActuator.sendingPostRequest("0");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 30000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// this.addDirectPropagation();
		// this.addGradient(10, "+", "light");
		// this.addDynamicGradient("id", 10);
		this.submitOperation();

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
	}

	@Override
	public void onLsaUpdatedEvent(LsaUpdatedEvent event) {
	}

	@Override
	public void onReadNotification(ReadEvent readEvent) {
	}

}
