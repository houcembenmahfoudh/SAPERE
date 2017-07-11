/**
 * 
 */
package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;

/**
 * An Event representing a LSA removed by the Decay Eco-law
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class DecayedEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = -7035112534612356142L;
	private Lsa lsa = null;

	/**
	 * Instatiates the Event
	 * 
	 * @param lsa
	 *            The removed LSA
	 */
	public DecayedEvent(Lsa lsa) {
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
