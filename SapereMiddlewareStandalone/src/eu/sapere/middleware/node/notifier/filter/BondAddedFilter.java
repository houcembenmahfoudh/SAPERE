/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;

/**
 * 
 * Provides a Filter for BondAddedEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class BondAddedFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for BondAddedEvent
	 * 
	 * @param lsaId
	 *            The id of the binded LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public BondAddedFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		BondAddedEvent bondAddedEvent = (BondAddedEvent) event;

		if (((Lsa) bondAddedEvent.getLsa()).getId().toString()
				.equals(targetLsaId.toString())
				&& bondAddedEvent.getRequiringAgent().equals(requestingId))
			ret = true;

		return ret;
	}

	public boolean apply(AbstractSapereEvent event, String lsaSubscriberId) {
		return false;
	}

	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof BondAddedFilter) {
			ret = (targetLsaId.toString().equals(
					((BondAddedFilter) o).targetLsaId.toString()) && requestingId
					.equals(((BondAddedFilter) o).requestingId));
		}
		return ret;
	}

}
