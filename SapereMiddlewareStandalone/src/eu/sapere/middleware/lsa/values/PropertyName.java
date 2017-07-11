package eu.sapere.middleware.lsa.values;

/**
 * Well-known Properties
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public enum PropertyName {

	// for gradient
	// GRADIENT("gradient_diffusion"),
	// for gradient
	// @SuppressWarnings("javadoc")
	// GRADIENT_PREVIOUS("gradient_previous"),
	// for gradient
	// @SuppressWarnings("javadoc")
	// GRADIENT_PREVIOUS_LOCAL("local"),
	// for gradient
	@SuppressWarnings("javadoc")
	GRADIENT_MAX_HOP("gradient_max_hop"),
	// for gradient
	// @SuppressWarnings("javadoc")
	// GRADIENT_HOP("gradient_hop"),

	@SuppressWarnings("javadoc")
	UUID("uuid"),

	@SuppressWarnings("javadoc")
	DIFFUSION_OP("diffusion_op"),

	@SuppressWarnings("javadoc")
	AGGREGATION_OP("aggregation_op"),

	@SuppressWarnings("javadoc")
	FIELD_VALUE("field_value"),

	@SuppressWarnings("javadoc")
	SOURCE("source"),

	@SuppressWarnings("javadoc")
	PREVIOUS("previous"),

	@SuppressWarnings("javadoc")
	DESTINATION("destination"),

	@SuppressWarnings("javadoc")
	ACTIVE_PROPAGATION("active_propagation"),

	@SuppressWarnings("javadoc")
	DECAY("decay"),

	@SuppressWarnings("javadoc")
	TIME_MACHINE_STORE("time-machine-store"),

	@SuppressWarnings("javadoc")
	TIME_MACHINE_RETRIEVE("time-machine-retrieve");

	private PropertyName(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return text;
	}

	/**
	 * Returns true if the property name is the one of the decay property
	 * 
	 * @param s
	 *            The property name
	 * @return true if the property name is the one of the decay property
	 */
	public static boolean isDecay(String s) {
		return s.toLowerCase().equals(DECAY.toString());
	};

	/**
	 * Returns true if the value is a correct decay value
	 * 
	 * @param s
	 *            The string value of the decay property
	 * @return true if the value is a correct decay value
	 */
	public static boolean isDecayValue(String s) {

		try {

			Integer.parseInt(s);

		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the integer representation of a decay value
	 * 
	 * @param s
	 *            The decay property value
	 * @return The crresponding integer
	 */
	public static Integer getDecayValue(String s) {

		Integer n = null;

		try {

			n = Integer.parseInt(s);

		} catch (NumberFormatException ex) {

		}

		return n;
	}

	/**
	 * Decrements the integer value provided
	 * 
	 * @param n
	 *            the integer value to be decremented
	 * @return The decremented value
	 */
	public static Integer decrement(Integer n) {
		return n - 1;
	}

	// public static boolean isExpired(Integer n){
	// return n.intValue() == 0;
	// }

}
