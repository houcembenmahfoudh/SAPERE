package eu.sapere.middleware.node.networking.topology.overlay;

/**
 * The NetworkAnalyzer should provide the implementation to manage a specific
 * overlay network. For each neighbor detected, the NetworkAnalyzer should
 * invoke the listener to add it.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class OverlayAnalyzer implements Runnable {

	protected OverlayListener listener = null;

	protected String overlayName;

	/**
	 * Instantiates a Overlay.
	 * 
	 * @param listener
	 *            The OverlayListener associated to this NetworkAnalyzer.
	 * @param overlayName
	 *            The name of the overlay network
	 * 
	 */
	public OverlayAnalyzer(OverlayListener listener, String overlayName) {

		this.listener = listener;
		this.overlayName = overlayName;
	}

	/**
	 * Triggers the injection of a new neighbour in the overlay network
	 * 
	 * @param neighbour
	 *            The neighbour to be added
	 */
	public final void addNeighbour(Neighbour neighbour) {
		listener.onNeighbourFound(neighbour, overlayName);
	}

	/**
	 * Stops this Overlay.
	 */
	public abstract void stopOverlay();
}
