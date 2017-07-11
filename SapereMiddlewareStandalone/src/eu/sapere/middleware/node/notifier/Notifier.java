package eu.sapere.middleware.node.notifier;

import java.util.Enumeration;
import java.util.Vector;

import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.filter.BondedLsaUpdateFilter;

/**
 * Provides the implementation for the Notifier
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class Notifier implements INotifier {

	//private Class<AbstractSapereEvent> eventClass = null;

	protected Vector<Subscription> subscriptions = null;

	/**
	 * Creates an instance for the Notifier
	 */
	public Notifier() {
		//eventClass = AbstractSapereEvent.class;
		subscriptions = new Vector<Subscription>();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void publish(AbstractSapereEvent event) {

		for (Enumeration<Subscription> elems = subscriptions.elements(); elems.hasMoreElements();) {

			Subscription subscription = elems.nextElement();

			if (subscription.getEventType().equals(BondedLsaUpdateEvent.class)
					&& (event instanceof BondedLsaUpdateEvent)) {

				if (subscription.getFilter().apply(event, subscription.getSubscriberName())) {
					subscription.getSubscriber().onNotification(event);
				}

			} else {

				if (subscription.getEventType().isAssignableFrom(event.getClass())
						&& (subscription.getFilter() == null || subscription.getFilter().apply(event))) {
					try {
						subscription.getSubscriber().onNotification(event);
					} catch (NullPointerException e) {
						// LOGGER.warning("Caught notification to agent
						// (Notifier.java): "
						// + event.getClass());
						if (event instanceof BondAddedEvent) {
							// LOGGER.info("requiring agent: " +
							// event.getRequiringAgent());
						}
					}

				}

			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void subscribe(Subscription s) {
		// if (!eventClass.isAssignableFrom(s.getEventType()))
		// throw new InvalidEventTypeException();

		boolean contains = false;

		for (Subscription ss : subscriptions)
			if (ss.equals(s))
				contains = true;

		if (!contains)
			subscriptions.addElement(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void unsubscribe(Subscription s) {

		// if (!eventClass.isAssignableFrom(s.getEventType()))
		// throw new InvalidEventTypeException();
		subscriptions.remove(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void unsubscribe(String subscriberName) {
		Vector<Subscription> v = new Vector<Subscription>(); // ss to be removed

		for (Subscription ss : subscriptions)
			if (ss.getSubscriberName().equals(subscriberName))
				v.add(ss);

		for (Subscription ss : v)
			unsubscribe(ss);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void unsubscribe(BondedLsaUpdateFilter filter) {

		Vector<Integer> v = new Vector<Integer>();

		for (int i = 0; i < subscriptions.size(); i++) {
			Subscription s = subscriptions.elementAt(i);

			if (s.getFilter().getClass().equals(filter.getClass())) {
				BondedLsaUpdateFilter f = (BondedLsaUpdateFilter) s.getFilter();

				if (f.getRequestingId().equals(filter.getRequestingId())
						&& f.getTargetLsaId().toString().equals(filter.getTargetLsaId().toString()))
					v.add(i);
			}

		}

		for (int j = 0; j < v.size(); j++) {
			subscriptions.removeElementAt(v.elementAt(j));
		}

	}

	@Override
	public String toString() {
		String ret = null;
		for (Enumeration<Subscription> elems = subscriptions.elements(); elems.hasMoreElements();) {
			Subscription subscription = elems.nextElement();

			if (ret == null)
				ret = new String();
			ret += "\t" + subscription.getSubscriberName() + " " + subscription.getEventType() + "\n";
		}

		return ret;
	}

}
