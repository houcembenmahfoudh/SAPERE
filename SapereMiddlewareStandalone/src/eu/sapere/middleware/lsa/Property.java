/**
 * 
 */
package eu.sapere.middleware.lsa;

import java.io.Serializable;
import java.util.Vector;

import eu.sapere.middleware.lsa.exception.MalformedPropertyException;

/**
 * Represents an LSA Property
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Property implements Serializable, Cloneable {

	/**
	 * 
	 */

	// NB Exceptions in constructors are evil

	private static final long serialVersionUID = -4698475527922276434L;

	/** The name of the Property */
	protected String name = null;

	/** The value of the Property */
	private Value value = null;

	/**
	 * Retrieves the Property values
	 * 
	 * @return A Vector with all the values of this Property
	 */
	public Vector<String> getValue() {
		return value.getValue();
	}

	/**
	 * Retrieves the Property value
	 * 
	 * @return The Property Value
	 */
	public Value getPropertyValue() {
		return value;
	}

	/**
	 * Sets the Value of this Property
	 * 
	 * @param value
	 *            The String that represents the Property Value
	 */
	public void setValue(String value) {
		this.value.setValue(value);
	}

	/**
	 * Retrieves the Name of this Property
	 * 
	 * @return The name of the Property
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this Property
	 * 
	 * @param name
	 *            The name of the Property
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Initializes this Property with a single Value
	 * 
	 * @param name
	 *            The Property name
	 * @param value
	 *            The Property value
	 */
	public Property(String name, String value) {
		this.name = name;
		this.value = new Value();
		this.value.setValue(value);
	}

	/**
	 * Returns true if this Property is malformed, i.e., Value contains both *
	 * and ?
	 * 
	 * @param v
	 *            the Strings that represent the content of this Property
	 * @return true if this Property is malformed, false otherwise
	 */
	private boolean isMalformed(String[] v) {
		boolean ret = false;

		int n = 0; // number of special values

		for (int i = 0; i < v.length; i++) {
			if (v[i].contains(ValueType.FORMAL_MANY.toString()) || v[i].contains(ValueType.FORMAL_ONE.toString()))
				n++;
			if (n >= 2) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	/**
	 * Initializes this Property with a multi Value
	 * 
	 * @param name
	 *            The Property name
	 * @param values
	 *            The Property values
	 * @throws MalformedPropertyException
	 */
	public Property(String name, String... values) throws MalformedPropertyException {

		String v[] = values;

		if (!isMalformed(v)) {

			this.name = name;
			this.value = new Value();

			for (int i = 0; i < v.length; i++)
				this.value.addValue(v[i]);

		} else
			throw new MalformedPropertyException();

	}

	/**
	 * Returns true if this Property Value contains the specified value
	 * 
	 * @param value
	 *            The value
	 * @return true if this Property Value contains the specified value, false
	 *         otherwise
	 */
	public boolean contains(String value) {

		boolean ret = false;

		if (this.value.contains(value))
			ret = true;

		return ret;
	}

	/**
	 * Returns true if this Property Value contains the specified value
	 * 
	 * @param value
	 *            The value
	 * @return true if this Property Value contains the specified value, false
	 *         otherwise
	 */
	public boolean contains(ValueType value) {

		boolean ret = false;

		if (this.value.contains(value))
			ret = true;

		return ret;
	}

	/**
	 * Returns a string representation of this Property
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * Returns a copy of this Property
	 * 
	 * @return the copy of this Property
	 */
	public Property getCopy() {
		return new Property(this.name, (Vector<String>) this.value.clone());
	}

	/**
	 * Initializes a multi-value Property
	 * 
	 * @param name
	 *            The name of this Property
	 * @param values
	 *            The values
	 */
	public Property(String name, Vector<String> values) {
		this.name = name;
		this.value = new Value();
		value.setValue(values);
	}

	/**
	 * Returns true if this Property matches the specified Property
	 * 
	 * @param matcher
	 *            The Property to be matched
	 * @return true if this Property matches the specified Property, false
	 *         otherwise
	 */
	public boolean matches(Property matcher) {

		boolean ret = false;

		if (this.name.toString().equals(matcher.name.toString()))
			if (this.value.matches(matcher.value))
				ret = true;

		return ret;
	}
}
