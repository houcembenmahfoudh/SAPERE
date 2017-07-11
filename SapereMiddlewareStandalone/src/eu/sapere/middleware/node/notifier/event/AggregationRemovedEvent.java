/**
 * 
 */
package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;

/**
 * An Event representing a LSA removed by the Aggregation Eco-law
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class AggregationRemovedEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = 2623336056801783324L;
	private Lsa lsa = null;

	/**
	 * Instatiates the Event
	 * 
	 * @param lsa
	 *            The removed LSA
	 */
	public AggregationRemovedEvent(Lsa lsa) {
		this.lsa = lsa;
	}

	/**
	 * Gets the removed LSA
	 * 
	 * @return The removed LSA
	 */
	public Lsa getLsa() {
		return lsa;
	}

}
