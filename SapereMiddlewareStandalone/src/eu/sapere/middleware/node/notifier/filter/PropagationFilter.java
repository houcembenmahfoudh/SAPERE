package eu.sapere.middleware.node.notifier.filter;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.PropagationEvent;

/**
 * Provides a Filter for PropagationEvent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class PropagationFilter implements IFilter {

	private Id targetLsaId = null;

	/**
	 * Instatiates a Filter for PropagationEvent
	 * 
	 * @param targetLsaId
	 *            The id of the involved LSA
	 */
	public PropagationFilter(Id targetLsaId) {
		this.targetLsaId = targetLsaId;
	}

	@Override
	public boolean apply(AbstractSapereEvent event) {

		boolean ret = false;

		PropagationEvent pEvent = (PropagationEvent) event;

		if (((Lsa) pEvent.getLsa()).getId().toString()
				.equals(targetLsaId.toString()))
			ret = true;
		return ret;
	}

	@Override
	public boolean apply(AbstractSapereEvent event, String agentName) {
		return false;
	}

	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof PropagationFilter) {
			ret = (targetLsaId.toString()
					.equals(((PropagationFilter) o).targetLsaId.toString()));
		}
		return ret;
	}

}
