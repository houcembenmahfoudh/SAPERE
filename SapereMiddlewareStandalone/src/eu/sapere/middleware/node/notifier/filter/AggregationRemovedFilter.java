/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.AggregationRemovedEvent;

/**
 * Provides a Filter for AggregationRemovedEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class AggregationRemovedFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for AggregationRemovedEvent
	 * 
	 * @param lsaId
	 *            The id of the removed LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public AggregationRemovedFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {
		boolean ret = false;

		AggregationRemovedEvent aggregationRemovedEvent = (AggregationRemovedEvent) event;

		if (aggregationRemovedEvent.getLsa().getId().toString()
				.equals(targetLsaId.toString()))
			ret = true;

		return ret;
	}

	public boolean apply(AbstractSapereEvent event, String lsaSubscriberId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equals(Object o) {
		boolean ret = false;

		if (o instanceof AggregationRemovedFilter)
			ret = (targetLsaId.toString().equals(
					((AggregationRemovedFilter) o).targetLsaId.toString()) && requestingId
					.equals(((AggregationRemovedFilter) o).requestingId));

		return ret;
	}

}
