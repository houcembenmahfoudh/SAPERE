package eu.sapere.middleware.lsa;

/**
 * Value Types for SAPERE Properties
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public enum ValueType {

	/**
	 * Represents a Formal "?" field
	 */
	FORMAL_ONE("?"),

	/**
	 * Represents a Formal "*" field
	 */
	FORMAL_MANY("*"),

	/**
	 * Represents a Potential "!" field
	 */
	POTENTIAL("!"),

	/**
	 * Represents a Actual field
	 */
	ACTUAL(null);

	private ValueType(final String text) {
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

	// public static String FORMAL_ONE = "?";
	// public static String FORMAL_MANY = "*";
	// public static String POTENTIAL = "!";

	/**
	 * Returns true is the field is actual
	 * 
	 * @param s
	 *            the field to be evaluated
	 * @return true is the field is actual
	 */
	public static boolean isActual(String s) {
		boolean ret = true;

		if (s.equals(FORMAL_ONE.toString()) || s.equals(FORMAL_MANY.toString()) || s.equals(POTENTIAL.toString()))
			ret = false;
		return ret;
	}

	/**
	 * Returns true is the field is potential
	 * 
	 * @param s
	 *            the field to be evaluated
	 * @return true is the field is potential
	 */
	public static boolean isPotential(String s) {
		boolean ret = false;

		if (s.equals(POTENTIAL.toString()))
			ret = true;
		return ret;
	}

	/**
	 * Returns true is the field is formal
	 * 
	 * @param s
	 *            the field to be evaluated
	 * @return true is the field is formal
	 */
	public static boolean isFormal(String s) {
		boolean ret = false;
		if (isFormalOne(s) || isFormalMany(s))
			ret = true;
		return ret;
	}

	/**
	 * Returns true is the field is formal "?"
	 * 
	 * @param s
	 *            the field to be evaluated
	 * @return true is the field is formal "?"
	 */
	public static boolean isFormalOne(String s) {
		boolean ret = false;
		if (s != null) {
			if (s.equals(FORMAL_ONE.toString()))
				ret = true;
		}
		return ret;
	}

	/**
	 * Returns true is the field is formal "*"
	 * 
	 * @param s
	 *            the field to be evaluated
	 * @return true is the field is formal "*"
	 */
	public static boolean isFormalMany(String s) {
		boolean ret = false;
		if (s != null) {
			if (s.equals(FORMAL_MANY.toString()))
				ret = true;
		}
		return ret;
	}

	/**
	 * Returns true is the fields matches
	 * 
	 * @param s1
	 *            a field
	 * @param s2
	 *            a field
	 * @return true is the fields matches
	 */
	public static boolean matches(String s1, String s2) {

		if (isActual(s1) && isActual(s2))
			return s1.equals(s2);

		if (isActual(s1) && isPotential(s2))
			return false;

		if (isActual(s1) && isFormal(s2))
			return true;

		if (isPotential(s1) && isFormalOne(s2))
			return true;

		if (isPotential(s1) && !isFormalOne(s2))
			return false;

		if (isFormalOne(s1) && isActual(s2))
			return true;

		if (isFormalOne(s1) && isPotential(s2))
			return true;

		if (isFormalOne(s1) && isFormal(s2))
			return false;

		if (isFormalMany(s1) && isActual(s2))
			return true;

		if (isFormalOne(s1) && !isActual(s2))
			return false;

		return false;
	}

};
