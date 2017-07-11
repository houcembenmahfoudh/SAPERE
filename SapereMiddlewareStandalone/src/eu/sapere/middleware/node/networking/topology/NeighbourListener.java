package eu.sapere.middleware.node.networking.topology;

import java.util.HashMap;

import eu.sapere.middleware.node.networking.topology.overlay.OverlayListener;

/**
 * Provides an interface for a listener for the Sapere network
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface NeighbourListener extends OverlayListener {

	/**
	 * Invoked when a neighbour should be removed from the Sapere Network.
	 * 
	 * @param key
	 *            The ip address of the neighbour to be removed.
	 * @param data
	 *            The description of the neighbour to be removed.
	 */
	public void onNeighbourExpired(String key, HashMap<String, String> data);

}
