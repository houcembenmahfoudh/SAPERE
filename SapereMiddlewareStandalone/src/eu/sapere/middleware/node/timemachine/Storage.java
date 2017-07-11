package eu.sapere.middleware.node.timemachine;

import java.util.HashMap;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * Provides a storage for the Time Machine.
 * 
 * @author Alberto Rosi (UNIMORE)
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Storage {

	private static HashMap<String, LsaContainer> storage;

	/**
	 * Instantiates the Storage.
	 */
	public Storage() {
		storage = new HashMap<String, LsaContainer>();
	}

	/**
	 * Creates a snapshot for the given Lsa.
	 * 
	 * @param lsa
	 *            The Lsa to be stored.
	 */
	public void createSnapshot(Lsa lsa) {

		String id = lsa.getId().getIdentifier();
		Long timestamp = Long.parseLong(lsa.getSyntheticProperty(SyntheticPropertyName.LAST_MODIFIED).getPropertyValue()
				.getValue().firstElement());

		synchronized (storage) {
			// if id is in index, add to existing container
			if (!storage.containsKey(id)) {
				LsaContainer container = new LsaContainer();
				container.addToContainer(timestamp, lsa);
				addToStorage(id, container);
			} else {
				getContainer(id).addToContainer(timestamp, lsa);
			}
		}

	}

	/**
	 * Deletes the snapshot for the given Lsa.
	 * 
	 * @param lsa
	 *            The Lsa to be removed from the storage.
	 */
	public void deleteSnapshot(Lsa lsa) {
		String id = lsa.getId().getIdentifier();
		synchronized (storage) {
			// if id is in index, add to existing container
			if (storage.containsKey(id)) {
				removeFromStorage(id);
			}
		}
	}

	/**
	 * Prints the snapshot for a given Lsa.
	 * 
	 * @param lsa
	 *            The lsa.
	 */
	public void printSnapshot(Lsa lsa) {
		String id = lsa.getId().getIdentifier();
		synchronized (storage) {
			// if id is in index, add to existing container
			if (storage.containsKey(id)) {
				System.out.println((getContainer(lsa.getId().getIdentifier())));
			} else {
				System.out.println("Container already moved.");
			}
		}
	}

	/**
	 * Retrieves the snapshot for a given Lsa.
	 * 
	 * @param lsa
	 *            The Lsa.
	 * @return An array containing the snapshot.
	 */
	public Lsa[] retrieveSnapshot(Lsa lsa) {
		synchronized (storage) {
			return getContainer(lsa.getId().getIdentifier()).extractLsas();
		}
	}

	/**
	 * Returns the size of the storage.
	 * 
	 * @return the size of the storage.
	 */
	public int getSize() {
		return storage.size();
	}

	private void addToStorage(String id, LsaContainer container) {
		storage.put(id, container);
	}

	private void removeFromStorage(String id) {
		storage.remove(id);
	}

	private LsaContainer getContainer(String id) {
		return (LsaContainer) storage.get(id);
	}
}
