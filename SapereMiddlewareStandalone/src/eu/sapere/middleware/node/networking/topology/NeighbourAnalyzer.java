package eu.sapere.middleware.node.networking.topology;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.networking.topology.overlay.Neighbour;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayAnalyzer;
import eu.sapere.middleware.node.networking.topology.overlay.OverlayListener;
import eu.sapere.util.ISystemConfiguration;

/**
 * Provide the implementation to manage the Sapere overlay network.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NeighbourAnalyzer extends OverlayAnalyzer {

	Hashtable<String, TimeLimitedCacheMap> neighbourTables = null;

	boolean isMerge = false;

	private boolean stop = false;

	/**
	 * Instantiates the Analyzer.
	 * 
	 * @param listener
	 *            The listener that manages the Sapere network.
	 * @param overlayName
	 *            The name of the overlay.
	 * @param neighbourTables
	 *            The tables of neighbours.
	 */
	public NeighbourAnalyzer(OverlayListener listener, String overlayName,
			Hashtable<String, TimeLimitedCacheMap> neighbourTables) {

		super(listener, overlayName);
		this.neighbourTables = neighbourTables;

		String overlayMode = NodeManager.getSystemConfiguration()
				.getProperties().getProperty(ISystemConfiguration.OVERLAY_MODE);
		if (overlayMode.equals("merge"))
			isMerge = true;
	}

	// adds neighbour from all tables: duplicates are merged together as best
	// effort
	@Override
	public void run() {

		while (!stop) {

			if (isMerge)
				mergeOverlays();
			else
				crossOverlays();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// nodes are neighbours if they appear in at least a single overlay network
	@SuppressWarnings("unchecked")
	private void mergeOverlays() {
		Collection<TimeLimitedCacheMap> overlays = neighbourTables.values();

		for (TimeLimitedCacheMap overlay : overlays) {

			Map<String, Object> clonedOverlay = overlay.getClonedMap();

			Iterator<String> iterator = clonedOverlay.keySet().iterator();

			while (iterator.hasNext()) {
				String key = iterator.next().toString(); // ipAddress
				HashMap<String, String> value = (HashMap<String, String>) clonedOverlay
						.get(key);

				Neighbour n = new Neighbour(value);
				listener.onNeighbourFound(n, "sapereNetwork");
			}

		}
	}

	// nodes are neighbours if they appear in at least a single overlay network
	@SuppressWarnings("unchecked")
	private void crossOverlays() {
		Collection<TimeLimitedCacheMap> overlays = neighbourTables.values();

		Map<String, Object> biggerOverlay = null;
		int nOverlays = 0;

		// 1. Select the overlay with more entries
		for (TimeLimitedCacheMap overlay : overlays) {
			if (biggerOverlay == null)
				biggerOverlay = overlay.getClonedMap();
			else {
				if (overlay.size() > biggerOverlay.size())
					biggerOverlay = overlay.getClonedMap();
			}
			nOverlays++;
		}

		// 2. Strat the intersection from the biggerOverlay

		Iterator<String> iterator = biggerOverlay.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			HashMap<String, String> value = (HashMap<String, String>) biggerOverlay
					.get(key);
			if (value != null)
				value = (HashMap<String, String>) value.clone();

			int isPresent = 0;
			for (TimeLimitedCacheMap overlay : overlays) {
				Map<String, Object> clonedOverlay = overlay.getClonedMap();
				HashMap<String, String> v = (HashMap<String, String>) clonedOverlay
						.get(key);
				if (v != null) {
					isPresent++;
					for (String k : v.keySet())
						value.put(k, v.get(k));
				} else
					break;
			}

			if (isPresent == nOverlays) {
				// 3. add the neighbour to the Sapere Network
				Neighbour n = new Neighbour(value);
				listener.onNeighbourFound(n, "sapereNetwork");
			}
		}

	}

	@Override
	public void stopOverlay() {
		this.stop = true;
	}

}
