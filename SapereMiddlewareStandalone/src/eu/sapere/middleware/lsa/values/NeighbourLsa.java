package eu.sapere.middleware.lsa.values;

/**
 * Enumerates Neighbors-specific properties
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public enum NeighbourLsa {

	/**
	 * A property to represent the name of the neighbor node
	 */
	NEIGHBOUR("neighbour"),

	/**
	 * A property to represent the ip address of the neighbor node
	 */
	IP_ADDRESS("ipAddress");

	private String text;

	NeighbourLsa(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}

	/**
	 * Returns the textual representation of the property
	 * 
	 * @return the textual representation of this property
	 */
	public String getText() {
		return this.text;
	}

}
