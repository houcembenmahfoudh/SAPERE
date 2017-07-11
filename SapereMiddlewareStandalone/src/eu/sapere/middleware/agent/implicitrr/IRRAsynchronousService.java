package eu.sapere.middleware.agent.implicitrr;

import eu.sapere.middleware.lsa.Lsa;

/**
 * Interface for Implicit Request-Response Services.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface IRRAsynchronousService {

	/**
	 * Called on bond. It is up to the programmer to provide the service answer
	 * in the property with "!" value.
	 * 
	 * @param bondedLsa
	 * @return
	 */
	public void onBondedLsaNotification(Lsa bondedLsa);

}
