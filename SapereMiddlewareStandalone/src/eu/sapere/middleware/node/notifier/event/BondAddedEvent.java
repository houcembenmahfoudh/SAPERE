package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;

/**
 * An event representing a Bond happened to a LSA
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class BondAddedEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = 899040460408932860L;
	private Lsa lsa = null;
	private Lsa bondedLsa = null;
	private String bondId = null; // the the Id of the LSa to which I have just
									// been bonded

	/**
	 * Instantiates the Event
	 * 
	 * @param lsa
	 * @param bondId
	 * @param bondedLsa
	 */
	public BondAddedEvent(Lsa lsa, String bondId, Lsa bondedLsa) {
		this.lsa = lsa;
		this.bondId = bondId;
		this.bondedLsa = bondedLsa;
	}

	/**
	 * @return The Lsa
	 */
	public ILsa getLsa() {
		return lsa;
	}

	/**
	 * @return The Id of the LSA which has been binded
	 */
	public String getBondId() {
		return bondId;
	}

	/**
	 * @return The binded LSA
	 */
	public ILsa getBondedLsa() {
		return bondedLsa;
	}

	// public void setBondedLsa(Lsa bondedLsa) {
	// this.bondedLsa = bondedLsa;
	// }

}
