/**
 * 
 */
package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;

/**
 * An Event representing the result of a read operation
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ReadEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = 6993728768596508832L;
	private Lsa lsa = null;

	/**
	 * Instatiates the Event
	 * 
	 * @param lsa
	 *            The read LSA
	 */
	public ReadEvent(Lsa lsa) {
		this.lsa = lsa;
	}

	/**
	 * Gets the read LSA
	 * 
	 * @return The read LSA
	 */
	public Lsa getLsa() {
		return lsa;
	}

}
