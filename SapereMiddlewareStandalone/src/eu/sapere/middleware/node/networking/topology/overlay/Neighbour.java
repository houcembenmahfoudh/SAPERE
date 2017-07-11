package eu.sapere.middleware.node.networking.topology.overlay;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Provides an implementation of a Sapere Node neighbour.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Neighbour {

	private String ipAddress = null;
	private String neighbourName = null;
	// private String neighbourType = null;

	protected Hashtable<String, NeighbourProperty> neighbourProperties = null;

	/**
	 * Instantiates a Neighbour. The ipAddress and a nodeName are mandatory and
	 * cannot be null values.
	 * 
	 * @param ipAddress
	 *            The ipAddress of the neighbour node.
	 * @param nodeName
	 *            The name of the neighbour node.
	 */
	public Neighbour(String ipAddress, String nodeName) {

		if (ipAddress != null && nodeName != null) {
			this.ipAddress = ipAddress;
			this.neighbourName = nodeName;

			neighbourProperties = new Hashtable<String, NeighbourProperty>();
		}
	}

	/**
	 * Instantiates a Neighbour. The ipAddress and a nodeName are mandatory and
	 * cannot be null values. Other properties can be added.
	 * 
	 * @param ipAddress
	 *            The ipAddress of the neighbour node.
	 * @param nodeName
	 *            The name of the neighbour node.
	 * @param properties
	 *            Custom properties.
	 */
	public Neighbour(String ipAddress, String nodeName,
			NeighbourProperty... properties) {

		if (ipAddress != null && nodeName != null) {
			this.ipAddress = ipAddress;
			this.neighbourName = nodeName;
			neighbourProperties = new Hashtable<String, NeighbourProperty>();

			for (NeighbourProperty p : properties)
				neighbourProperties.put(p.getPropertyName(), p);
		}
	}

	/**
	 * Instantiates a Neighbour.
	 * 
	 * @param properties
	 *            The properties to be added to the neighbour.
	 */
	public Neighbour(HashMap<String, String> properties) {
		neighbourProperties = new Hashtable<String, NeighbourProperty>();

		for (String pName : properties.keySet()) {
			neighbourProperties.put(pName, new NeighbourProperty(pName,
					properties.get(pName)));
			if (pName.equals("ipAddress"))
				this.ipAddress = properties.get(pName);
			if (pName.equals("neighbourName"))
				this.neighbourName = properties.get(pName);
		}
	}

	// public HashMap<String, String> getData(){
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("ipAddress", ipAddress);
	// map.put("neighbour", neighbour);
	//
	// return map;
	// }

	/**
	 * Returns an HashMap containing all the Properties of this neighbour.
	 * 
	 * @return an HashMap containing all the Properties of this neighbour.
	 */
	public HashMap<String, String> getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ipAddress", ipAddress);
		map.put("neighbour", neighbourName);

		for (NeighbourProperty p : neighbourProperties.values()) {
			map.put(p.getPropertyName(), p.getPropertyValue());
		}

		return map;
	}

	/**
	 * Returns the ip address of this neighbour.
	 * 
	 * @return The ip address of this neighbour.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Returns the naighbour name of this neighbour.
	 * 
	 * @return The naighbour name of this neighbour.
	 */
	public String getNeighbour() {
		return neighbourName;
	}

	// public String getNeighbourType() {
	// return neighbourType;
	// }
	//
	// public void setNeighbourType(String nodeType) {
	// this.neighbourType = nodeType;
	// }

}
