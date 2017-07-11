/**
 * 
 */
package eu.sapere.middleware.node.notifier;

import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.filter.IFilter;

/**
 * A supscription to events happening to LSAs.
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class Subscription {

	// Stores information about a single subscription

	private AbstractSapereEvent eventType;
	private IFilter filter;
	private AbstractSubscriber subscriber;
	private String subscriberName;

	/**
	 * Instantiates a Subscription
	 * 
	 * @param anEventType
	 *            The type of event
	 * @param aFilter
	 *            The filter for events
	 * @param aSubscriber
	 *            The Subscriber Object
	 * @param subscriberName
	 *            The name of the Subscriber
	 */
	public Subscription(AbstractSapereEvent anEventType, IFilter aFilter,
			AbstractSubscriber aSubscriber, String subscriberName) {
		this.eventType = anEventType;
		this.filter = aFilter;
		this.subscriber = aSubscriber;
		this.subscriberName = subscriberName;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;

		if (o instanceof Subscription)
			ret = (eventType.getClass().equals(
					((Subscription) o).eventType.getClass())
					&& filter.equals(((Subscription) o).filter) && subscriberName
					.equals(((Subscription) o).subscriberName));

		return ret;
	}

	@Override
	public String toString() {
		String ret = new String();

		ret = "<eventType=" + eventType + ", filter=" + filter
				+ ", subscriber=" + subscriber + ", subscriberName="
				+ subscriberName + ">";

		return ret;
	}

	/**
	 * Gets the Subscriber for this Subscription
	 * 
	 * @return The Subscriber
	 */
	public AbstractSubscriber getSubscriber() {
		return subscriber;
	}

	// public void setSubscriber(AbstractSubscriber subscriber) {
	// this.subscriber = subscriber;
	// }

	/**
	 * Gets the name of the Subscriber for this Subscription
	 * 
	 * @return The name of the Subscriber
	 */
	public String getSubscriberName() {
		return subscriberName;
	}

	/**
	 * Gets the Filter for this Subscription
	 * 
	 * @return The Filter
	 */
	public IFilter getFilter() {
		return filter;
	}

	/**
	 * Gets the EventType for this Subscription
	 * 
	 * @return The EventType
	 */
	public Class<? extends AbstractSapereEvent> getEventType() {
		return eventType.getClass();
	}

}
