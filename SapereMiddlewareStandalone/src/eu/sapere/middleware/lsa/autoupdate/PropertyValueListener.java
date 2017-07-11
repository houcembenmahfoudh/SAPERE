package eu.sapere.middleware.lsa.autoupdate;

/**
 * Provides an interface for a listner for the automatic and periodic update of
 * a Property
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface PropertyValueListener {

	/**
	 * Invoked when a value has been generated to change it in the property
	 * value
	 * 
	 * @param event
	 *            The event
	 */
	public void propertyValueGenerated(PropertyValueEvent event);

	/**
	 * Invoked when a value has been generated to append it to the property
	 * 
	 * @param event
	 *            The event
	 */
	public void propertyValueAppendGenerated(PropertyValueEvent event);

}
