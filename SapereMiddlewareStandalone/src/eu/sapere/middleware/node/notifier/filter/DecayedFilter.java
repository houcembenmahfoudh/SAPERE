/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.DecayedEvent;

/**
 * Provides a Filter for DecayedEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class DecayedFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for DecayedEvent
	 * 
	 * @param lsaId
	 *            The id of the involved LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public DecayedFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		DecayedEvent decayEvent = (DecayedEvent) event;

		if (decayEvent.getLsa().getId().toString()
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
		if (o instanceof DecayedFilter) {
			ret = (targetLsaId.toString().equals(
					((DecayedFilter) o).targetLsaId.toString()) && requestingId
					.equals(((DecayedFilter) o).requestingId));
		}
		return ret;
	}

}
