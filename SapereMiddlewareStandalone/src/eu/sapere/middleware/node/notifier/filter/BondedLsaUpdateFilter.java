/**
 * 
 */
package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;

/**
 * Provides a Filter for BondedLsaUpdateEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public class BondedLsaUpdateFilter implements IFilter {

	private Id targetLsaId = null;

	private String requestingId = null;

	/**
	 * Instatiates a Filter for BondedLsaUpdateEvent
	 * 
	 * @param lsaId
	 *            The id of the involved LSA
	 * @param requestingId
	 *            The name of the Agent that must be notified
	 */
	public BondedLsaUpdateFilter(Id lsaId, String requestingId) {
		this.targetLsaId = lsaId; // changedId
		this.requestingId = requestingId; // requesting Id

	}

	public boolean apply(AbstractSapereEvent event, String agentName) {

		boolean ret = false;

		BondedLsaUpdateEvent bondedLsaUpdateEvent = (BondedLsaUpdateEvent) event;

	    if(((Lsa) bondedLsaUpdateEvent.getLsa()).getId() != null &&
	    		targetLsaId != null)
	    		if (((Lsa) bondedLsaUpdateEvent.getLsa()).getId().toString()
	    				.equals(targetLsaId.toString()))
	    				ret = true;

		return ret;
	}

	public boolean apply(AbstractSapereEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return
	 */
	public Id getTargetLsaId() {
		return targetLsaId;
	}

	/**
	 * @return
	 */
	public String getRequestingId() {
		return requestingId;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof BondedLsaUpdateFilter) {
			ret = (targetLsaId.toString().equals(
					((BondedLsaUpdateFilter) o).targetLsaId.toString()) && requestingId
					.equals(((BondedLsaUpdateFilter) o).requestingId));
		}
		return ret;
	}

}
