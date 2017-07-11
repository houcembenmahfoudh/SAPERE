/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;

/**
 * Provides a Filter for LsaUpdatedEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class LsaUpdatedFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for LsaUpdatedEvent
	 * 
	 * @param lsaId
	 *            The id of the involved LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public LsaUpdatedFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		LsaUpdatedEvent lsaUpdatedEvent = (LsaUpdatedEvent) event;

		if (lsaUpdatedEvent.getLsa().getId().toString()
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
		if (o instanceof LsaUpdatedFilter) {
			ret = (targetLsaId.toString().equals(
					((LsaUpdatedFilter) o).targetLsaId.toString()) && requestingId
					.equals(((LsaUpdatedFilter) o).requestingId));
		}
		return ret;
	}

}
