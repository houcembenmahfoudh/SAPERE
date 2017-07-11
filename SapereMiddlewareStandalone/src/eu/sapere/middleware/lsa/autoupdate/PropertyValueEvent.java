package eu.sapere.middleware.lsa.autoupdate;

import java.util.EventObject;

/**
 * An event representing a periodic and automatic update in the value of a
 * property
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class PropertyValueEvent extends EventObject {

	private static final long serialVersionUID = 5461269044381723264L;
	private String propertyName;
	private String value;

	/**
	 * Instantiates an Event
	 * 
	 * @param source
	 *            The source of the event
	 * @param propertyName
	 *            The name of the property affected
	 * @param value
	 *            The new value for the property
	 */
	public PropertyValueEvent(Object source, String propertyName, String value) {
		super(source);
		this.propertyName = propertyName;
		this.value = value;
	}

	/**
	 * Returns the name of the property affected
	 * 
	 * @return the name of the property affected
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Returns the updated value for the property
	 * 
	 * @return the updated value for the property
	 */
	public String getValue() {
		return value;
	}

}
