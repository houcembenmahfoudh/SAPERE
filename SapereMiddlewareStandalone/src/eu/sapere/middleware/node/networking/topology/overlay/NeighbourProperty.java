package eu.sapere.middleware.node.networking.topology.overlay;

/**
 * Provides the implementation of a property for neighbours. A NeighbourProperty
 * is a string-value pair.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class NeighbourProperty {

	private String propertyName = null;
	private String propertyValue = null;

	/**
	 * Creates a property.
	 * 
	 * @param propertyName
	 *            The name of the property.
	 * @param propertyValue
	 *            The value of the property.
	 */
	public NeighbourProperty(String propertyName, String propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	/**
	 * Returns the name of this property.
	 * 
	 * @return The name of this property.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	// public void setPropertyName(String propertyName) {
	// this.propertyName = propertyName;
	// }

	/**
	 * Returns the value of this property.
	 * 
	 * @return The value of this property.
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	// public void setPropertyValue(String propertyValue) {
	// this.propertyValue = propertyValue;
	// }

}
