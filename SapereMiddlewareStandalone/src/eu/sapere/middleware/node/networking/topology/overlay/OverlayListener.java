package eu.sapere.middleware.node.networking.topology.overlay;

/**
 * Provides an interface for a listener for an overlay network
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface OverlayListener {

	/**
	 * Invoked when a a new neighbor in the overlay network is detected
	 * 
	 * @param neighbour
	 *            The neighbour to be added to the overlay network
	 * @param overlayName
	 *            The name of the overlay network to which add the neighbour.
	 */
	public void onNeighbourFound(Neighbour neighbour, String overlayName);
}
