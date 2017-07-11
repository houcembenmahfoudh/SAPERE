/**
 * 
 */
package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;

/**
 * An Event representing a LSA updated by an Eco-law
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class LsaUpdatedEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = 2291794225134181080L;
	private Lsa lsa = null;

	/**
	 * Instatiates the Event
	 * 
	 * @param lsa
	 *            The updated LSA
	 */
	public LsaUpdatedEvent(Lsa lsa) {
		this.lsa = lsa;
	}

	/**
	 * Gets the updated LSA
	 * 
	 * @return The updated LSA
	 */
	public Lsa getLsa() {
		return lsa;
	}

}
