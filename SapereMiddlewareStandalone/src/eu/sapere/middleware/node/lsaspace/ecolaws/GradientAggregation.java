package eu.sapere.middleware.node.lsaspace.ecolaws;

import java.util.HashMap;
import java.util.Vector;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.SyntheticProperty;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;
import eu.sapere.middleware.node.lsaspace.EcoLawsEngine;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;

/**
 * @author Francesco De Angelis (University of Geneva)
 */
public class GradientAggregation extends AbstractEcoLaw {

	/*
	 * LSA that wants to be propagated MUST have the following fields defined in
	 * PropertyValue: aggregation_op: Aggregation Operators field_value: Field
	 * to which the Aggregation Operator will be applied source: an id to
	 * identify the LSAs to be aggregated together
	 */

	/**
	 * @param space
	 * @param opManager
	 * @param notifier
	 * @param networkDeliveryManager
	 */
	public GradientAggregation(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		super(space, opManager, notifier, networkDeliveryManager);
	}

	public void invoke() {
		gradientAggregation();
	}

	private HashMap<String, Vector<Lsa>> findGradientsLsas() {
		HashMap<String, Vector<Lsa>> lsas = new HashMap<String, Vector<Lsa>>();

		for (Lsa lsa : getLSAs()) {

			Property gp = lsa.getProperty(PropertyName.DIFFUSION_OP.toString());
			if (gp == null)
				continue;
			else if (gp.getValue().firstElement().contains("GRADIENT")) {

				// String gradientId = gp.getValue().firstElement();
				String gradientId = lsa.getFirstProperty(PropertyName.FIELD_VALUE.toString());
				if (lsas.get(gradientId) == null) {
					lsas.put(gradientId, new Vector<Lsa>());
				}
				lsas.get(gradientId).add(lsa);
			}
		}
		return lsas;
	}

	@SuppressWarnings("unused")
	private Lsa trackMinimum(Vector<Lsa> lsas, Vector<Lsa> toRemove) {
		// int minValue = Integer.MAX_VALUE;
		int maxValue = 0;
		Lsa maxLsa = null;
		// boolean findPrevious=false;
		// System.out.println("trackMinimum");
		// System.out.println("lsas size " + lsas.size());
		for (int i = 0; i < lsas.size(); i++) {

			if (lsas.get(i).getFirstProperty(PropertyName.PREVIOUS.toString())
					.equals(lsas.get(i).getFirstProperty(PropertyName.SOURCE.toString()))) {
				// findPrevious=true;
				maxLsa = lsas.get(i);

			} else {
				String strV = lsas.get(i).getFirstProperty(PropertyName.DIFFUSION_OP.toString());
				int intValue = Integer.parseInt(strV.substring(strV.indexOf("_") + 1));
				if (intValue > maxValue) {
					maxValue = intValue;
					maxLsa = lsas.get(i);
				}
			}
		}

		for (int i = 0; i < lsas.size(); i++) {
			if (!lsas.get(i).equals(maxLsa))
				toRemove.add(lsas.get(i));
		}
		return maxLsa;
	}

	private void gradientAggregation() {
		HashMap<String, Vector<Lsa>> gradientLsas = findGradientsLsas();
		Vector<Lsa> toRemove = new Vector<Lsa>();

		if (gradientLsas.size() == 0)
			return;

		for (String gradientId : gradientLsas.keySet()) {

			if (gradientLsas.get(gradientId).size() < 2)
				continue;

			// Lsa maxlsa = trackMinimum(gradientLsas.get(gradientId),
			// toRemove);
			// System.out.println(maxlsa.getProperty(PropertyName.DIFFUSION_OP.toString()));
		}

		for (int i = 0; i < toRemove.size(); i++) {
			// add the creator syntethic property
			// System.out.println("to remove "
			// +
			// toRemove.get(i).getProperty(PropertyName.DIFFUSION_OP.toString()).getValue().elementAt(0));
			Lsa lsa = toRemove.elementAt(i);
			lsa.addProperty(new SyntheticProperty(SyntheticPropertyName.CREATOR_ID,
					EcoLawsEngine.ECO_LAWS_GRADIENT_AGGREGATION.toString()));
			remove(toRemove.elementAt(i));
		}
	}

}
