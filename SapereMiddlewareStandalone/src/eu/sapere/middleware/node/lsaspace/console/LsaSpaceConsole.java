package eu.sapere.middleware.node.lsaspace.console;

import eu.sapere.middleware.lsa.Lsa;

/**
 * Provides an interface for the SAPERE Middleware Console.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface LsaSpaceConsole {

	/**
	 * Updates the Monitor, printing the given list of LSA
	 * 
	 * @param list
	 *            the list of LSA to show
	 */
	public void update(final Lsa[] list);

	/**
	 * Notifies the removal of a Lsa.
	 * 
	 * @param lsaId
	 *            The id of the removed Lsa.
	 */
	public void removeLsa(String lsaId);

	/**
	 * Notifies the removal of a Bond.
	 * 
	 * @param from
	 *            The id of the first Lsa involved in the Bond
	 * @param to
	 *            The id of the second Lsa involved in the Bond
	 */
	public void removeBond(String from, String to);

	/**
	 * Notifies the propagation of a Lsa.
	 * 
	 * @param lsaId
	 *            The id of the propagated Lsa
	 */
	public void propagateLsa(String lsaId);

	/**
	 * Removes a bidirectional Bond.
	 * 
	 * @param from
	 *            The id of the first Lsa involved in the Bond
	 * @param to
	 *            The id of the second Lsa involved in the Bond
	 */
	public void addBidirectionalBond(String from, String to);

	/**
	 * Removes a unidirectional Bond.
	 * 
	 * @param from
	 *            The id of the first Lsa involved in the Bond
	 * @param to
	 *            The id of the second Lsa involved in the Bond
	 */
	public void addUnidirectionalBond(String from, String to);

	/**
	 * Returns true if the Visual Inspector is active
	 * 
	 * @return true if the Visual Inspector is active
	 * */
	public abstract boolean hasInspector();

}
