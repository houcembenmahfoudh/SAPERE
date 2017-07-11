package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;

/**
 * Provides an interface for filtering events
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface IFilter {

	/**
	 * Applies a filter to the given Event
	 * 
	 * @param event
	 *            The event to filter
	 * @return true if the filter applies, false otherwise
	 */
	public boolean apply(AbstractSapereEvent event);

	/**
	 * Applies a filter to the given Event and the Subscriber's name
	 * 
	 * @param event
	 *            The event to filter
	 * @param agentName
	 *            The name of the Subscriber Agent
	 * @return true if the filter applies, false otherwise
	 */
	public boolean apply(AbstractSapereEvent event, String agentName);
}