/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;

/**
 * Provides a Filter for ReadEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class ReadFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for ReadEvent
	 * 
	 * @param lsaId
	 *            The id of the involved LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public ReadFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		ReadEvent readEvent = (ReadEvent) event;

		if (readEvent.getLsa().getId().toString()
				.equals(targetLsaId.toString())
				&& readEvent.getRequiringAgent().equals(requestingId))
			ret = true;

		return ret;
	}

	public boolean apply(AbstractSapereEvent event, String lsaSubscriberId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ReadFilter) {
			ret = (targetLsaId.toString().equals(
					((ReadFilter) o).targetLsaId.toString()) && requestingId
					.equals(((ReadFilter) o).requestingId));
		}
		return ret;
	}

}
