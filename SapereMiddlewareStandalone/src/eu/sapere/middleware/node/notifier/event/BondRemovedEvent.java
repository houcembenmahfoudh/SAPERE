/**
 * 
 */
package eu.sapere.middleware.node.notifier.event;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;

/**
 * An event representing the removal of a Bond
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class BondRemovedEvent extends AbstractSapereEvent {

	private static final long serialVersionUID = -7328201342833095480L;
	private Lsa lsa = null;
	private String bondId = null; // the the Id of the LSa to which I have just
									// been bonded

	/**
	 * Instantiates the Event
	 * 
	 * @param lsa
	 *            My LSA
	 * @param bondId
	 *            The Id of the LSA that is no longer binded
	 */
	public BondRemovedEvent(Lsa lsa, String bondId) {
		this.lsa = lsa;
		this.bondId = bondId;
	}

	/**
	 * @return My LSA
	 */
	public ILsa getLsa() {
		return lsa;
	}

	/**
	 * @return The Id of the LSA that is no longer binded
	 */
	public String getBondId() {
		return bondId;
	}

}
