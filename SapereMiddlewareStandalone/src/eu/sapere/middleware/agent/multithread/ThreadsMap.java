package eu.sapere.middleware.agent.multithread;

import java.util.HashMap;

/**
 * Keeps the Map of active threads for a Multi-Thread Sapere Agent
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class ThreadsMap {

	/**
	 * The Threads' map
	 */
	private HashMap<Long, ThreadSapereAgent> threads = null;

	/**
	 * The next available id
	 */
	private static long nextId = 0L;

	/**
	 * Initialises the Thread Map
	 */
	public ThreadsMap() {
		this.threads = new HashMap<Long, ThreadSapereAgent>();
	}

	/**
	 * Puts the reference to a thread in the Map
	 * 
	 * @param agent
	 *            The reference to a thread
	 * @return the id associated to the thread
	 */
	public Long put(ThreadSapereAgent agent) {
		Long id = getFreshId();
		threads.put(id, agent);
		return id;
	}

	/**
	 * Removes the reference of the given thread from the Map
	 * 
	 * @param id
	 *            the id of the agents to be removed
	 */
	public void remove(Long id) {
		threads.remove(id);
	}

	/**
	 * Retrieves an id to be associated with a thread
	 * 
	 * @return an available id
	 */
	private Long getFreshId() {
		while (threads.containsKey(nextId)) {
			nextId++;
		}
		return nextId++;
	}

}
