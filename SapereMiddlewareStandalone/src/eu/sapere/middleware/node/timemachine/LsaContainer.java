package eu.sapere.middleware.node.timemachine;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import eu.sapere.middleware.lsa.Lsa;

/**
 * LsaContainer is a synchronized container to be used as storage for LSAs.
 * 
 * @author Alberto Rosi (UNIMORE)
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class LsaContainer {

	ConcurrentHashMap<Long, Lsa> lsaSnapshots;

	/**
	 * Instantiates the Container.
	 */
	public LsaContainer() {
		lsaSnapshots = new ConcurrentHashMap<Long, Lsa>();
	}

	/**
	 * Adds a Lsa to the container.
	 * 
	 * @param timestamp
	 *            The timestamp of the Lsa.
	 * @param lsa
	 *            The Lsa.
	 */
	public void addToContainer(Long timestamp, Lsa lsa) {
		lsaSnapshots.put(timestamp, lsa);
	}

	/**
	 * Returns the size of this container, id. the number of Lsa stored.
	 * 
	 * @return The size of this container.
	 */
	public int getSize() {
		return lsaSnapshots.size();
	}

	/**
	 * Returns an array containing the content of this container.
	 * 
	 * @return an array containing the content of this container.
	 */
	public Lsa[] extractLsas() {

		int i = 0;

		synchronized (lsaSnapshots) {
			Lsa[] temp = new Lsa[getSize()];
			Iterator<Long> iterator = lsaSnapshots.keySet().iterator();

			while (iterator.hasNext()) {
				Long key = (Long) iterator.next();
				temp[i++] = (Lsa) lsaSnapshots.get(key);

			}
			return temp;
		}

	}

	@Override
	public String toString() {
		String ret = null;
		Iterator<Long> iterator = lsaSnapshots.keySet().iterator();

		while (iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Lsa value = (Lsa) lsaSnapshots.get(key);

			ret += key + " " + value.toString() + "\n";
		}

		return ret;
	}

}
