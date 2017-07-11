/**
 * 
 */
package eu.sapere.middleware.lsa.values;

import java.util.EnumSet;

/**
 * Synthetic Properties
 * 
 * @author Gabriella Castelli (UNIMORE)
 * @author Graeme Stevenson (STA)
 * 
 */
public enum SyntheticPropertyName {

	// Edit 18/10/12 - Graeme: reordered to support alphabetical enumeration

	@SuppressWarnings("javadoc")
	BONDS("#bonds"), @SuppressWarnings("javadoc")
	CREATION_TIME("#creationTime"), @SuppressWarnings("javadoc")
	CREATOR_ID("#creatorId"), @SuppressWarnings("javadoc")
	LAST_MODIFIED("#lastModified"), @SuppressWarnings("javadoc")
	LOCATION("#location");

	private SyntheticPropertyName(final String text) {
		this.text = text;
	}

	private final String text;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return text;
	}

	/**
	 * Returns true if the name provided is of a synthetic property
	 * 
	 * @param name
	 *            The name of the property
	 * @return true if the name provided is of a synthetic property
	 */
	public static boolean isSyntheticProperty(String name) {
		return (name).equals(LAST_MODIFIED.toString());
	}

	/**
	 * Returns the correct syntax for a synthetic property
	 * 
	 * @param s
	 *            the name of a synthetic property
	 * @return The correct syntax for a synthetic property
	 */
	public static SyntheticPropertyName getSyntheticPropertiesValues(String s) {

		for (SyntheticPropertyName sp : EnumSet
				.allOf(SyntheticPropertyName.class)) {
			if (sp.toString().equals(s))
				return sp;
		}
		return null;
	}

};
