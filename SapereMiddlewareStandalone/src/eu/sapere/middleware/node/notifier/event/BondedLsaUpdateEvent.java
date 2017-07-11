package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;

/**
 * Ana event representing that a binded LSA has been changed
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class BondedLsaUpdateEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = -5881376204815387600L;
	private Lsa bondedLsa = null;

	/**
	 * Instantiates the Event
	 * 
	 * @param bondedLsa
	 *            The binded LSA
	 */
	public BondedLsaUpdateEvent(Lsa bondedLsa) {

		this.bondedLsa = bondedLsa;
	}

	/**
	 * @return The binded LSA
	 */
	public ILsa getLsa() {
		return bondedLsa;
	}

}
