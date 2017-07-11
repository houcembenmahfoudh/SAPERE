package sapere;

import eu.sapere.middleware.node.networking.topology.overlay.Neighbour;
import eu.sapere.middleware.node.networking.topology.overlay.NeighbourProperty;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayAnalyzer;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayListener;
import eu.sapere.util.ConfigReader;

/**
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NetworkAnalyzer extends OverlayAnalyzer {

	public NetworkAnalyzer(OverlayListener listener, String overlayName) {
		super(listener, overlayName);
	}

	@Override
	public void run() {
		ConfigReader config = new ConfigReader();
		String[][] neighs = config.getNeighs();
		if (neighs != null) {
			Neighbour n;
			for (int i = 0; i < neighs.length; i++) {
				n = new Neighbour(neighs[i][0], neighs[i][1], new NeighbourProperty("nodeType", "mobile"));
				addNeighbour(n);
			}
			// Neighbour n2 = new Neighbour("155.185.49.35", "gabriella", new
			// NeighbourProperty("nodeType", "mobile"));
			// addNeighbour(n2);
		}

	}

	@Override
	public void stopOverlay() {
	}
}
