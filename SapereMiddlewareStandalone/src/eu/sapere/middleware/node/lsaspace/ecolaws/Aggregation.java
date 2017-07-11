package eu.sapere.middleware.node.lsaspace.ecolaws;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.SyntheticProperty;
import eu.sapere.middleware.lsa.exception.UnresolvedPropertyNameException;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.notifier.event.LsaUpdatedEvent;

/**
 * @author Gabriella Castelli (UNIMORE)
 * @author Graeme Stevenson (STA)
 */
public class Aggregation extends AbstractEcoLaw {

	/*
	 * LSA that wants to be propagated MUST have the following fields defined in
	 * PropertyValue: aggregation_op: Aggregation Operators field_value: Field
	 * to which the Aggregation Operator will be applied source: an id to
	 * identify the LSAs to be aggregated together
	 */

	/**
	 * Creates a new instance of the aggregation eco-law.
	 * 
	 * @param space
	 *            The space in which the eco-law executes.
	 * @param opManager
	 *            The OperationManager that manages operations in the space
	 * @param notifier
	 *            The Notifier that notifies agents whith events happening to
	 *            LSAs
	 * @param networkDeliveryManager
	 *            The interface for Network Delivery of LSAs
	 */
	public Aggregation(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		super(space, opManager, notifier, networkDeliveryManager);
	}

	/**
	 * {@inheritDoc}
	 */
	public void invoke() {
		selfAggregation();
		otherAggregation();

	}

	private void selfAggregation() {
		Vector<String> sourceProcessed = new Vector<String>();
		Vector<Lsa> toRemove = new Vector<Lsa>();

		for (Lsa lsa : getLSAs()) {
			if (lsa.getAggregationSource() != null) {
				if (lsa.explicitAggregationApplies()
						&& !sourceProcessed.contains(lsa.getAggregationSource().toString())) {
					sourceProcessed.add(lsa.getAggregationSource().toString());

					Vector<Lsa> compatible = getAggregationCompatibleLsaI(lsa, getLSAs());

					// EDIT BY GRAEME - prevents aggregation when only a single
					// LSA
					// is present. (i.e.,
					// aggregation with itself).
					if (compatible.size() >= 2) {

						if (lsa.getAggregationOp().equals(AggregationOperators.MAX.toString())) {
							Id l = null;
							l = AggregationOperators.max(compatible);

							if (l != null) {
								if (!lsa.getId().toString().equals(l.toString()))
									toRemove.add(lsa);

								for (int k = 0; k < compatible.size(); k++)
									if (!compatible.elementAt(k).getId().toString().equals(l.toString()))
										toRemove.add(compatible.elementAt(k));
							}

						}

						if (lsa.getAggregationOp().equals(AggregationOperators.MIN.toString())) {
							Id l = null;
							l = AggregationOperators.min(compatible);

							if (l != null) {
								if (!lsa.getId().toString().equals(l.toString()))
									toRemove.add(lsa);

								for (int k = 0; k < compatible.size(); k++)
									if (!compatible.elementAt(k).getId().toString().equals(l.toString()))
										toRemove.add(compatible.elementAt(k));
							}

						}

						if (lsa.getAggregationOp().equals(AggregationOperators.AVG.toString())) {
							// compatible.add(allLsa[i]);

							Lsa l = AggregationOperators.avg(compatible).getCopy();

							for (int k = 0; k < compatible.size(); k++) {
								toRemove.add(compatible.elementAt(k));
							}
							inject(l);
						}
					} // end if compatible.size >2

				}
			}
		}

		for (int j = 0; j < toRemove.size(); j++) {
			remove(toRemove.elementAt(j));
		}
	}

	private void otherAggregation() {

		for (Lsa lsa : getLSAs()) {

			if (lsa.requestedAggregationApplies()) {

				Vector<Lsa> compatible = getRequestAggregationCompatibleLsa(lsa, getLSAs());

				if (lsa.getAggregationOp().equals(AggregationOperators.MAX.toString())) {

					/*
					 * l = AggregationOperators.max(compatible,
					 * allLsa[i].getProperty(PropertyName.FIELD_VALUE
					 * .toString()).getValue().elementAt(0)); l.addProperty(new
					 * Property("aggregated_by",
					 * AggregationOperators.MAX.toString()));
					 */

					String pName = lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0);
					String value = AggregationOperators.maxValue(compatible,
							lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0));

					if (lsa.hasProperty(pName))
						try {
							lsa.setProperty(new Property(pName, value));
						} catch (UnresolvedPropertyNameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						lsa.addProperty(new Property(pName, value));

					// Triggers the LsaUpdatedEvent
					AbstractSapereEvent lsaUpdatedEvent = new LsaUpdatedEvent(lsa);
					lsaUpdatedEvent.setRequiringAgent(null);
					publish(lsaUpdatedEvent);

				}

				if (lsa.getAggregationOp().equals(AggregationOperators.MIN.toString())) {

					// l = AggregationOperators.min(compatible,
					// allLsa[i].getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0));
					// l.addProperty(new Property("aggregated_by",
					// AggregationOperators.MIN.toString()));

					String pName = lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0);
					String value = AggregationOperators.minValue(compatible,
							lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0));
					if (lsa.hasProperty(pName))
						try {
							lsa.setProperty(new Property(pName, value));
						} catch (UnresolvedPropertyNameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						lsa.addProperty(new Property(pName, value));

					// Triggers the LsaUpdatedEvent
					AbstractSapereEvent lsaUpdatedEvent = new LsaUpdatedEvent(lsa);
					lsaUpdatedEvent.setRequiringAgent(null);
					publish(lsaUpdatedEvent);
				}

				if (lsa.getAggregationOp().equals(AggregationOperators.AVG.toString())) {

					// l = AggregationOperators.mean(compatible,
					// allLsa[i].getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0)).getCopy();
					// l.addProperty(new Property("aggregated_by",
					// AggregationOperators.AVG.toString()));
					String pName = lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0);
					String value = AggregationOperators.avgValue(compatible,
							lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0));
					if (lsa.hasProperty(pName))
						try {
							lsa.setProperty(new Property(pName, value));
						} catch (UnresolvedPropertyNameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						lsa.addProperty(new Property(pName, value));

					// Triggers the LsaUpdatedEvent
					AbstractSapereEvent lsaUpdatedEvent = new LsaUpdatedEvent(lsa);
					lsaUpdatedEvent.setRequiringAgent(null);
					publish(lsaUpdatedEvent);
				}

				// Operation inject = new Operation().injectOperation(l,
				// EcoLawsEngine.ECO_LAWS_AGGREGATION, null);
				// opMng.execOperation(inject);

			}

		}

	}

	private Vector<Lsa> getRequestAggregationCompatibleLsa(Lsa lsa, List<Lsa> allLsa) {
		Vector<Lsa> ret = new Vector<Lsa>();
		for (Lsa secondLsa : allLsa) {

			if (!secondLsa.requestedAggregationApplies() && !secondLsa.explicitAggregationApplies()
					&& !secondLsa.hasProperty("aggregated_by") && secondLsa.hasProperty(
							lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0))) {
				// idGradient =
				// lsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().get(0);
				// if
				// (idGradient.equals(secondLsa.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().get(0)))
				ret.add(secondLsa.getCopy());
			}
		}
		return ret;
	}

	// private Vector<Lsa> getAggregationCompatibleLsa(Lsa lsa, List<Lsa>
	// allLsa) {
	// Vector<Lsa> ret = new Vector<Lsa>();
	// for (Lsa secondLsa : allLsa) {
	//
	// if (secondLsa.aggregationApplies() &&
	// !secondLsa.getId().toString().equals(lsa.getId().toString())
	// && secondLsa.getAggregationSource().equals(lsa.getAggregationSource()))
	// ret.add(secondLsa.getCopy());
	// }
	// return ret;
	// }

	// lsa included
	private Vector<Lsa> getAggregationCompatibleLsaI(Lsa lsa, List<Lsa> allLsa) {
		Vector<Lsa> ret = new Vector<Lsa>();
		for (Lsa secondLsa : allLsa) {

			if (secondLsa.aggregationApplies() && !secondLsa.getId().toString().equals(lsa.getId().toString())
					&& secondLsa.getAggregationSource().equals(lsa.getAggregationSource()))
				ret.add(secondLsa.getCopy());
		}

		ret.add(lsa);
		return ret;
	}

	enum AggregationOperators {

		NEWEST {
			public String toString() {
				return "NEWEST";
			}
		},

		OLDEST {
			public String toString() {
				return "OLDEST";
			}
		},

		MAX {
			public String toString() {
				return "MAX";
			}
		},

		MIN {
			public String toString() {
				return "MIN";
			}
		},

		AVG {
			public String toString() {
				return "AVG";
			}
		};

		private static BigDecimal getValue(Lsa l, String fieldName) {

			if (SyntheticPropertyName.isSyntheticProperty(fieldName)) {
				return new BigDecimal(
						l.getSyntheticProperty(SyntheticPropertyName.getSyntheticPropertiesValues(fieldName)).getValue()
								.elementAt(0));

			} else
				return new BigDecimal(l.getProperty(l.getFieldValue()).getValue().elementAt(0));
		}

		public static Id max(Vector<Lsa> allLsa) {

			Id ret = null;

			if (allLsa.isEmpty())
				return null;

			BigDecimal max = getValue(allLsa.elementAt(0), allLsa.elementAt(0).getFieldValue());
			ret = allLsa.elementAt(0).getId();

			for (int i = 0; i < allLsa.size(); i++) {
				BigDecimal m = getValue(allLsa.elementAt(i), allLsa.elementAt(0).getFieldValue());
				if (m.max(max).equals(m)) {
					max = m;
					ret = allLsa.elementAt(i).getId();
				}
			}

			return ret;

		}

		/*
		 * public static Lsa max (Vector<Lsa> allLsa, String fieldValue){ Lsa
		 * ret = null; if(allLsa.isEmpty()) return null; int max = new
		 * Integer(allLsa
		 * .elementAt(0).getProperty(fieldValue).getValue().elementAt
		 * (0)).intValue(); ret = allLsa.elementAt(0).getCopy(); for(int i=0;
		 * i<allLsa.size(); i++){ if (new
		 * Integer(allLsa.elementAt(i).getProperty
		 * (fieldValue).getValue().elementAt(0)).intValue() > max){ max = new
		 * Integer
		 * (allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt
		 * (0)).intValue(); ret = allLsa.elementAt(i).getCopy(); } } return ret;
		 * }
		 */

		public static String maxValue(Vector<Lsa> allLsa, String fieldValue) {

			// Lsa ret = null;

			if (allLsa.isEmpty())
				return null;

			int max = new Integer(allLsa.elementAt(0).getProperty(fieldValue).getValue().elementAt(0)).intValue();
			// ret = allLsa.elementAt(0).getCopy();

			for (int i = 0; i < allLsa.size(); i++) {
				if (new Integer(allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt(0)).intValue() > max) {
					max = new Integer(allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt(0)).intValue();
					// ret = allLsa.elementAt(i).getCopy();
				}
			}

			return new Integer(max).toString();

		}

		public static Id min(Vector<Lsa> allLsa) {

			Id ret = null;

			if (allLsa.isEmpty())
				return null;

			BigDecimal min = getValue(allLsa.elementAt(0), allLsa.elementAt(0).getFieldValue());
			ret = allLsa.elementAt(0).getId();

			for (int i = 0; i < allLsa.size(); i++) {
				BigDecimal m = getValue(allLsa.elementAt(i), allLsa.elementAt(0).getFieldValue());
				if (m.min(min).equals(m)) {
					min = m;
					ret = allLsa.elementAt(i).getId();
				}
			}

			return ret;

		}

		/*
		 * public static Lsa min (Vector<Lsa> allLsa, String fieldValue){ Lsa
		 * ret = null; if(allLsa.isEmpty()) return null; int min = new
		 * Integer(allLsa
		 * .elementAt(0).getProperty(fieldValue).getValue().elementAt
		 * (0)).intValue(); ret = allLsa.elementAt(0).getCopy(); for(int i=0;
		 * i<allLsa.size(); i++){ if (new
		 * Integer(allLsa.elementAt(i).getProperty
		 * (fieldValue).getValue().elementAt(0)).intValue() < min){ min = new
		 * Integer
		 * (allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt
		 * (0)).intValue(); ret = allLsa.elementAt(i).getCopy(); } } return ret;
		 * }
		 */

		public static String minValue(Vector<Lsa> allLsa, String fieldValue) {

			// Lsa ret = null;
			if (allLsa.isEmpty())
				return null;
			int min = new Integer(allLsa.elementAt(0).getProperty(fieldValue).getValue().elementAt(0)).intValue();
			// ret = allLsa.elementAt(0).getCopy();

			for (int i = 0; i < allLsa.size(); i++) {
				if (new Integer(allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt(0)).intValue() < min) {
					min = new Integer(allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt(0)).intValue();
					// ret = allLsa.elementAt(i).getCopy();
				}
			}

			return new Integer(min).toString();

		}

		public static Lsa avg(Vector<Lsa> allLsa) {

			Lsa ret = null;

			if (allLsa.isEmpty())
				return null;

			BigDecimal val = new BigDecimal("0");

			for (int i = 0; i < allLsa.size(); i++) {
				val = val.add(new BigDecimal(
						allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)));
			}

			val = val.divide(new BigDecimal(allLsa.size()));

			ret = allLsa.elementAt(0).getCopy();
			try {
				ret.setProperty(new Property(allLsa.elementAt(0).getFieldValue(), "" + val));
			} catch (UnresolvedPropertyNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret.addProperty(
					new SyntheticProperty(SyntheticPropertyName.LAST_MODIFIED, new String("" + new Date().getTime())));

			return ret;

		}

		public static String avgValue(Vector<Lsa> allLsa, String fieldValue) {

			Lsa ret = null;

			if (allLsa.isEmpty())
				return null;

			int val = 0;

			for (int i = 0; i < allLsa.size(); i++) {
				val += new Integer(allLsa.elementAt(i).getProperty(fieldValue).getValue().elementAt(0)).intValue();
			}

			val = val / allLsa.size();

			ret = allLsa.elementAt(0).getCopy();
			try {
				ret.setProperty(new Property(fieldValue, "" + val));
			} catch (UnresolvedPropertyNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret.addProperty(
					new SyntheticProperty(SyntheticPropertyName.LAST_MODIFIED, new String("" + new Date().getTime())));

			return new Integer(val).toString();

		}

	}

}
