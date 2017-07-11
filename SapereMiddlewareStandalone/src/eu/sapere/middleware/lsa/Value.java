/**
 * 
 */
package eu.sapere.middleware.lsa;

import java.io.Serializable;
import java.util.Vector;

/**
 * Represents a value for a Property
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Value implements Serializable {

	private static final long serialVersionUID = 1L;
	Vector<String> value = null;

	/**
	 * Initializes an empty value
	 */
	public Value() {
		value = new Vector<String>();
	}

	/**
	 * @return the value
	 */
	public Vector<String> getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Vector<String> value) {
		this.value = new Vector<String>();
		this.value = value;
	}

	/**
	 * Add a Property value to this Value set
	 * 
	 * @param value
	 *            the value to be added
	 */
	public void addValue(String value) {

		if (this.value == null)
			this.value = new Vector<String>();
		this.value.add(value);

	}

	/**
	 * @param value
	 */
	public void setValue(String value) {

		this.value = new Vector<String>();

		this.value.add(value);
	}

	/**
	 * Returns true if this Value contains the specified value
	 * 
	 * @param value
	 *            The value type
	 * @return true if this Value contains the specified value, false otherwise
	 */
	public boolean contains(ValueType value) {
		return this.value.contains(value);
	}

	/**
	 * Returns true if this Value contains the specified value
	 * 
	 * @param value
	 *            The value
	 * @return true if this Value contains the specified value, false otherwise
	 */
	public boolean contains(String value) {
		return this.value.contains(value);
	}

	/**
	 * Returns a clone of this Value
	 */
	public Vector<String> clone() {
		Vector<String> copy = new Vector<String>();
		for (int i = 0; i < value.size(); i++)
			copy.add(value.elementAt(i));

		return copy;
	}

	/**
	 * Returns a string representation of this LSA Content
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * Returns true if this Value matches the specified value
	 * 
	 * @param matcher
	 *            The value to be matched
	 * @return Returns true if this Value matches the specified value, false
	 *         otherwise
	 */
	public boolean matches(Value matcher) {

		if (this.value.size() == matcher.value.size()) {
			for (int i = 0; i < this.value.size(); i++) {
				boolean found = false;

				for (int j = 0; j < matcher.value.size(); j++)
					if (ValueType.matches(value.elementAt(i),
							matcher.value.elementAt(j)))
						found = true;

				if (found == false)
					return false;

			}
		}

		return true;

	}

	/**
	 * Returns true if this Value is formal, i.e., contains ? or *
	 * 
	 * @return true if this Value is formal, false otherwise
	 */
	public boolean isFormal() {

		for (int i = 0; i < this.value.size(); i++) {
			if (ValueType.isFormal(value.elementAt(i))) {
				return true;
			}
		}

		return false;

	}

}
