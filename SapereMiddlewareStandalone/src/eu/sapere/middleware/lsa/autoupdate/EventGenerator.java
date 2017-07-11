package eu.sapere.middleware.lsa.autoupdate;

import java.util.Iterator;
import java.util.Vector;

/**
 * Manages the periodic and automatic generation of values for the property of a
 * LSA
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class EventGenerator {

	private Vector<PropertyValueListener> listeners = new Vector<PropertyValueListener>();

	private String propertyName = null;

	boolean onAppend;

	/**
	 * Instantiates an EventGenerator that overrides the property value
	 */
	public EventGenerator() {
		this.onAppend = false;
	}

	/**
	 * Instantiates an EventGenerator
	 * 
	 * @param onAppend
	 *            true is the propoerty values must be appended, false if must
	 *            be overridden
	 */
	public EventGenerator(boolean onAppend) {

		this.onAppend = onAppend;
	}

	/**
	 * Sets the name of the property to be managed by the EventGenerator
	 * 
	 * @param propertyName
	 *            the name of the property to be managed by the EventGenerator
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Invoked to triiger the update of the property value
	 * 
	 * @param s
	 *            the updated value
	 */
	public void autoUpdate(String s) {

		firePropertyValueEvent(s);
	}

	/**
	 * Adds a PropertyValueListener
	 * 
	 * @param l
	 *            the propertyValueListener to be added
	 */
	public synchronized void addPropertyValueListener(PropertyValueListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a PropertyValueListener
	 * 
	 * @param l
	 *            the propertyValueListener to be removed
	 */
	public synchronized void removePropertyValueListener(PropertyValueListener l) {
		listeners.remove(l);
	}

	private synchronized void firePropertyValueEvent(String s) {

		PropertyValueEvent newEvent = new PropertyValueEvent(this,
				propertyName, s);

		Iterator<PropertyValueListener> pvListeners = listeners.iterator();
		while (pvListeners.hasNext()) {

			if (this.onAppend = true)
				((PropertyValueListener) pvListeners.next())
						.propertyValueGenerated(newEvent);
			else
				((PropertyValueListener) pvListeners.next())
						.propertyValueAppendGenerated(newEvent);
		}
	}
}
