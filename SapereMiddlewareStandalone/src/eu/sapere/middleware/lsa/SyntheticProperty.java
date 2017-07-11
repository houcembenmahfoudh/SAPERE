/**
 * 
 */
package eu.sapere.middleware.lsa;

import java.io.Serializable;

import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * Represets a Synthetic Property, i.e., a Property added and maintained by the
 * middleware.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class SyntheticProperty extends Property implements Serializable,
		Cloneable {

	// CREATOR_ID , CREATION_TIME, LAST_MODIFIED, LOCATION, BONDS;

	private static final long serialVersionUID = -4698475527922276434L;

	/**
	 * Initializes a Synthetic Property with the specified name
	 * 
	 * @param name
	 *            The name of the Property
	 */
	public SyntheticProperty(SyntheticPropertyName name) {
		super(name.toString());
	}

	/**
	 * Initializes a Synthetic Property with the specified name and a single
	 * value
	 * 
	 * @param name
	 *            The name of the Property
	 * @param value
	 *            The value of the Property
	 */
	public SyntheticProperty(SyntheticPropertyName name, String value) {
		super(name.toString(), value);
	}

	/**
	 * Initializes a Synthetic Property with the specified name and a multiple
	 * value
	 * 
	 * @param name
	 *            The name of the Property
	 * @param values
	 *            The values of the Property
	 */
	public SyntheticProperty(SyntheticPropertyName name, String... values) {

		super(name.toString(), values);
	}

}
