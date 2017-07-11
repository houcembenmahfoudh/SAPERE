package eu.sapere.middleware.node.lsaspace.ecolaws;

/**
 * An interface for eco-laws that provides a means of invoking them.
 * 
 * @author Graeme Stevenson (STA)
 * @author Gabriella Castelli (UNIMORE)
 */
public interface IEcoLaw {

	/**
	 * Invokes the eco-law.
	 */
	void invoke();

	/**
	 * @return eco-law name
	 */
	String getName();

}
