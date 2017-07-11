/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;

/**
 * Provides a Filter for BondRemovedEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class BondRemovedFilter implements IFilter {

	private Id targetLsaId = null;
	private String requestingId = null;

	/**
	 * Instatiates a Filter for BondRemovedEvent
	 * 
	 * @param lsaId
	 *            The id of the involved LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public BondRemovedFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId;
		this.requestingId = requestingId;
	}

	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		BondRemovedEvent bondRemovedEvent = (BondRemovedEvent) event;

		if (((Lsa) bondRemovedEvent.getLsa()).getId().toString()
				.equals(targetLsaId.toString())
				&& bondRemovedEvent.getRequiringAgent().equals(requestingId))
			ret = true;

		return ret;
	}

	public boolean apply(AbstractSapereEvent event, String lsaSubscriberId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof BondRemovedFilter) {
			ret = (targetLsaId.toString().equals(
					((BondRemovedFilter) o).targetLsaId.toString()) && requestingId
					.equals(((BondRemovedFilter) o).requestingId));
		}
		return ret;
	}

}
