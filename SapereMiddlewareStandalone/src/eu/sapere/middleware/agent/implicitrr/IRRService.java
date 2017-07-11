package eu.sapere.middleware.agent.implicitrr;

import java.util.Vector;

import eu.sapere.middleware.lsa.Lsa;

/**
 * Interface for Implicit Request-Response Services.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface IRRService {

	/**
	 * Called on bond. This method is used to provide the service.
	 * 
	 * @param bondedLsa
	 * @return
	 */
	public Vector<String> provideResponse(Lsa bondedLsa);

}
