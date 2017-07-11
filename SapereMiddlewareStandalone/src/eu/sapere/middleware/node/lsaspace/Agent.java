package eu.sapere.middleware.node.lsaspace;

import eu.sapere.middleware.node.notifier.AbstractSubscriber;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;

/**
 * Abstract class that represents a Sapere Agent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class Agent extends AbstractSubscriber {

	protected String agentName = null;

	/**
	 * @param agentName
	 *            The name of this Agent
	 */
	public Agent(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * Retrieves the name of the Agent
	 * 
	 * @return The name of the Agent
	 */
	public String getAgentName() {
		return agentName;
	}

}
