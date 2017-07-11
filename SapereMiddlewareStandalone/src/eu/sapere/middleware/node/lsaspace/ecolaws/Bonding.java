package eu.sapere.middleware.node.lsaspace.ecolaws;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.SubDescription;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.notifier.Notifier;

/**
 * @author Gabriella Castelli (UNIMORE)
 * @author Graeme Stevenson (STA)
 */
public class Bonding extends AbstractEcoLaw {

	/**
	 * Creates a new instance of the bonding eco-law.
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
	public Bonding(Space space, OperationManager opManager, Notifier notifier,
			NetworkDeliveryManager networkDeliveryManager) {
		super(space, opManager, notifier, networkDeliveryManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invoke() {
		execBondsFromLSA();
		execBondsFromSD();
	}

	private void execBondsFromLSA() {
		for (Lsa outerLsa : getLSAs()) {

			if (isBondRequester(outerLsa)) {
				boolean continueWithSd = true; // processSubDescriptions

				if (outerLsa.isFormalOne()) {
					for (Lsa targetLsa : getLSAs()) {
						if (outerLsa != targetLsa && isBondable(targetLsa) && outerLsa.matches(targetLsa)) {
							constructBiDirectionalBondLSAToLSA(outerLsa, targetLsa);
							// continueWithSd = false; // match found, no need
							// to continue
							// looking.

							// IF VISUAL INSPECTOR ACTIVE, represent the BOND
							if (space.hasVisualInspector())
								space.addVisualBond(outerLsa.getId(), targetLsa.getId(), true);

							break;
						}
					}
				} else {
					// We express a * request
					for (Lsa targetLsa : getLSAs()) {
						if (outerLsa != targetLsa && isBondable(targetLsa) && !outerLsa.hasBondWith(targetLsa.getId())
								&& outerLsa.matches(targetLsa)) {
							constructUniDirectionalBondLSAToLSA(outerLsa, targetLsa);

							// IF VISUAL INSPECTOR ACTIVE, represent the BOND
							if (space.hasVisualInspector())
								space.addVisualBond(outerLsa.getId(), targetLsa.getId(), false);
						}
					}
				}

				if (continueWithSd) {
					// System.out.println("try LSA to SD");

					for (Lsa targetLsa : getLSAs()) {
						// System.out.println("\t"+targetLsa);
						if (continueWithSd)
							continueWithSd = execBondsFromLSAToSD(targetLsa, outerLsa);
						else
							break;
					}

				}

			}
		}

	}

	/**
	 * Executes all bonds from sub-descriptions to LSAs.
	 * 
	 * @param the_lsas
	 *            all the LSAs
	 * @param a_space
	 *            the space
	 * @param an_operationManager
	 *            the operation manager.
	 */
	private void execBondsFromSD() {

		// Iterate through all the LSAs
		for (Lsa outerLsa : getLSAs()) {
			SubDescription[] candidateSd = outerLsa.getSubDescriptions();
			// For all candidates
			for (SubDescription candidateRequester : candidateSd) {
				// If the sub description expresses a bond request
				if (isBondRequester(candidateRequester)) {
					// Flag for whether we should continue to process
					// subdescriptions
					boolean processSubDescriptions = true;

					// If we wish only a single bond and do not currently have
					// one established.
					if (candidateRequester.isFormalOne()) {
						for (Lsa targetLsa : getLSAs()) {
							if (outerLsa != targetLsa && isBondable(targetLsa)
									&& candidateRequester.matches(targetLsa)) {
								constructBiDirectionalBondSDToLSA(outerLsa, candidateRequester, targetLsa);
								processSubDescriptions = false; // match found,
																// no need to
																// continue
																// looking.

								if (space.hasVisualInspector())
									space.addVisualBond(outerLsa.getId(), targetLsa.getId(), true);
								break;
							}
						}
					} else {
						// We express a * request
						for (Lsa targetLsa : getLSAs()) {

							if (outerLsa != targetLsa && isBondable(targetLsa) && candidateRequester.matches(targetLsa)
									&& !outerLsa.hasBondWith(targetLsa.getId())) {
								constructUniDirectionalBondSDToLSA(outerLsa, candidateRequester, targetLsa);
								//System.out.println("execBondsFromSD - match found * "+ candidateRequester.getName());

								if (space.hasVisualInspector())
									space.addVisualBond(outerLsa.getId(), targetLsa.getId(), false);
							}
						}
					}
					if (processSubDescriptions) {
						execBondsFromSDToSD(outerLsa, candidateRequester);
					}
				}
			}
		}
	}

	/**
	 * Executes all bonds from sub-descriptions of an LSA to other LSAs
	 * subdescriptions.
	 * 
	 * @param a_subDescription
	 *            the subdescription we are processing.
	 * @param the_lsas
	 *            all the LSAs
	 * @param a_space
	 *            the space
	 * @param an_operationManager
	 *            the operation manager.
	 */
	private void execBondsFromSDToSD(final Lsa an_lsa, SubDescription a_request) {
		// If we wish only a single bond and do not currently have one
		// established.
		if (a_request.isFormalOne()) {
			for (Lsa targetLsa : getLSAs()) {
				for (SubDescription innerSubDesc : targetLsa.getSubDescriptions()) {
					if (an_lsa != targetLsa && isBondable(innerSubDesc) && a_request.matches(innerSubDesc)) {
						constructBiDirectionalBondSDToSD(an_lsa, a_request, targetLsa, innerSubDesc);

						if (space.hasVisualInspector())
							space.addVisualBond(an_lsa.getId(), targetLsa.getId(), true);

						break;
					}
				}
				if (!isBondable(a_request))
					break;
			}
		} else {
			// We express a * request
			for (Lsa targetLsa : getLSAs()) {
				for (SubDescription innerSubDesc : targetLsa.getSubDescriptions()) {
					if (an_lsa != targetLsa && isBondable(innerSubDesc) && a_request.matches(innerSubDesc)
							&& !a_request.hasBond(innerSubDesc.getId().toString())) {
						constructUniDirectionalBondSDToSD(an_lsa, a_request, targetLsa, innerSubDesc);
						if (space.hasVisualInspector())
							space.addVisualBond(an_lsa.getId(), targetLsa.getId(), false);
					}
				}
			}

		}

	}

	private boolean execBondsFromLSAToSD(final Lsa an_lsa, Lsa a_request) {
		boolean ret = true;

		SubDescription sds[] = null;
		sds = an_lsa.getSubDescriptions();
		if (sds != null) {
			for (SubDescription sd : sds) {
				if (isBondable(sd)) {
					if (a_request.isFormalOne()) {
						if (a_request.matches(sd)) {
							ret = false;

							constructBiDirectionalBondLSAToSD(a_request, sd, an_lsa);

							if (space.hasVisualInspector())
								space.addVisualBond(a_request.getId(), a_request.getId(), true);

							break;
						}
					}

					if (a_request.isFormalMany()) {
						if (a_request.matches(sd)) {

							constructUniDirectionalBondLSAToSD(a_request, sd, an_lsa);

							if (space.hasVisualInspector())
								space.addVisualBond(a_request.getId(), a_request.getId(), false);

						}
					}

				}

			}
		}

		return ret;
	}

	// constructBiDirectionalBondLSAToLSA(outerLsa, outerLsa, targetLsa);
	private void constructBiDirectionalBondLSAToLSA(Lsa a_bondRequestLsa, Lsa a_matchedLsa) {

		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		a_bondRequestLsaCopy.addBond(a_matchedLsa.getId().toString());
		update(a_bondRequestLsaCopy);

		Lsa a_matchedLsaCopy = a_matchedLsa.getCopy();
		a_matchedLsaCopy.addBond(a_bondRequestLsa.getId().toString());
		update(a_matchedLsaCopy);

		// update the working LSA
		a_bondRequestLsa.addBond(a_matchedLsa.getId().toString());
		a_matchedLsa.addBond(a_bondRequestLsa.getId().toString());

	}

	/**
	 * Constructs a bond between two lsas, via a sub-description on the first.
	 * 
	 * @param a_bondRequestLsa
	 *            the LSA containing the bond request subdescription
	 * @param a_bondSubDescription
	 *            the subdescription issuing the request
	 * @param a_matchedLsa
	 *            the lsa to bond to
	 * @param an_operationManager
	 *            the operation manager to perform the operation.
	 */
	private void constructBiDirectionalBondSDToLSA(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa) {

		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		SubDescription sd = a_bondRequestLsaCopy
				.getSubDescriptionByName(a_bondRequestLsaCopy.getSubDescriptionName(a_bondSubDescription.getId()));
		sd.addBond(a_matchedLsa.getId().toString());
		update(a_bondRequestLsaCopy);

		Lsa a_matchedLsaCopy = a_matchedLsa.getCopy();
		a_matchedLsaCopy.addBond(a_bondSubDescription.getId().toString());
		update(a_matchedLsaCopy);

		// update the working LSA
		SubDescription sd1 = a_bondRequestLsa
				.getSubDescriptionByName(a_bondRequestLsa.getSubDescriptionName(a_bondSubDescription.getId()));
		sd1.addBond(a_matchedLsa.getId().toString());

		a_matchedLsa.addBond(a_bondSubDescription.getId().toString());

	}

	private void constructBiDirectionalBondLSAToSD(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa) {

		// update using a Copy
		Lsa a_matchedLsaCopy = a_matchedLsa.getCopy();
		SubDescription sd = a_matchedLsaCopy
				.getSubDescriptionByName(a_matchedLsaCopy.getSubDescriptionName(a_bondSubDescription.getId()));
		sd.addBond(a_bondRequestLsa.getId().toString());
		update(a_matchedLsaCopy);

		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		a_bondRequestLsaCopy.addBond(sd.getId().toString());
		update(a_bondRequestLsaCopy);

		// update the working LSA
		SubDescription sd1 = a_matchedLsa
				.getSubDescriptionByName(a_matchedLsa.getSubDescriptionName(a_bondSubDescription.getId()));
		sd1.addBond(a_bondRequestLsa.getId().toString());

		a_bondRequestLsa.addBond(sd1.getId().toString());

		// AddToContentTracker
		// s.addVisualBond(candidateRequester.getId(), newAllLsa[c].getId(),
		// true);
	}

	private void constructBiDirectionalBondSDToSD(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa, SubDescription a_matchedSubDescription) {

		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		SubDescription sd = a_bondRequestLsaCopy
				.getSubDescriptionByName(a_bondRequestLsaCopy.getSubDescriptionName(a_bondSubDescription.getId()));
		sd.addBond(a_matchedSubDescription.getId().toString());
		update(a_bondRequestLsaCopy);

		Lsa a_matchedLsaCopy = a_matchedLsa.getCopy();
		SubDescription sd1 = a_matchedLsaCopy
				.getSubDescriptionByName(a_matchedLsaCopy.getSubDescriptionName(a_matchedSubDescription.getId()));
		sd1.addBond(a_bondSubDescription.getId().toString());
		update(a_matchedLsaCopy);

		// update the working LSA
		SubDescription sd2 = a_bondRequestLsa
				.getSubDescriptionByName(a_bondRequestLsa.getSubDescriptionName(a_bondSubDescription.getId()));
		sd2.addBond(a_matchedSubDescription.getId().toString());

		SubDescription sd3 = a_matchedLsa
				.getSubDescriptionByName(a_matchedLsa.getSubDescriptionName(a_matchedSubDescription.getId()));
		sd3.addBond(a_bondSubDescription.getId().toString());

	}

	private void constructUniDirectionalBondLSAToLSA(Lsa a_bondRequestLsa, Lsa a_matchedLsa) {

		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		a_bondRequestLsaCopy.addBond(a_matchedLsa.getId().toString());
		update(a_bondRequestLsaCopy);

		// update the working LSA
		a_bondRequestLsa.addBond(a_matchedLsa.getId().toString());
	}

	/**
	 * Constructs a bond between two lsas, via a sub-description on the first.
	 * 
	 * @param a_bondRequestLsa
	 *            the LSA containing the bond request subdescription
	 * @param a_bondSubDescription
	 *            the subdescription issuing the request
	 * @param the_matchedLsas
	 *            the lsas to bond with
	 * @param an_operationManager
	 *            the operation manager to perform the operation.
	 */
	private void constructUniDirectionalBondSDToLSA(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa) {
		
		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		SubDescription sd = a_bondRequestLsaCopy
				.getSubDescriptionByName(a_bondRequestLsaCopy.getSubDescriptionName(a_bondSubDescription.getId()));
		sd.addBond(a_matchedLsa.getId().toString());
		update(a_bondRequestLsaCopy);

		// update the working LSA
		SubDescription sd1 = a_bondRequestLsa
				.getSubDescriptionByName(a_bondRequestLsa.getSubDescriptionName(a_bondSubDescription.getId()));
		sd1.addBond(a_matchedLsa.getId().toString());
	}

	private void constructUniDirectionalBondSDToSD(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa, SubDescription a_matchedSubDescription) {
		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		SubDescription sd = a_bondRequestLsaCopy
				.getSubDescriptionByName(a_bondRequestLsaCopy.getSubDescriptionName(a_bondSubDescription.getId()));
		sd.addBond(a_matchedSubDescription.getId().toString());
		update(a_bondRequestLsaCopy);

		// update the working LSA
		SubDescription sd1 = a_bondRequestLsa
				.getSubDescriptionByName(a_bondRequestLsa.getSubDescriptionName(a_bondSubDescription.getId()));
		sd1.addBond(a_matchedSubDescription.getId().toString());
	}

	private void constructUniDirectionalBondLSAToSD(Lsa a_bondRequestLsa, SubDescription a_bondSubDescription,
			Lsa a_matchedLsa) {

		// update using a Copy
		Lsa a_bondRequestLsaCopy = a_bondRequestLsa.getCopy();
		a_bondRequestLsaCopy.addBond(a_bondSubDescription.getId().toString());
		update(a_bondRequestLsaCopy);

		// update the working LSA
		a_bondRequestLsa.addBond(a_bondSubDescription.getId().toString());

	}

	private boolean isBondRequester(Lsa candidate) {

		if (candidate.isFormal())
			if (!(candidate.isFormalOne() && candidate.hasBonds()))
				return true;

		return false;
	}

	private boolean isBondRequester(SubDescription candidate) {
		if (candidate.isFormal())
			if (!(candidate.isFormalOne() && candidate.hasBonds()))
				return true;

		return false;
	}

	private boolean isBondable(Lsa bondCandidate) {

		if ((!bondCandidate.isFormal()) && (!bondCandidate.hasBonds()))
			return true;

		if (bondCandidate.isFormalOne() && !bondCandidate.hasBonds())
			return true;

		if (bondCandidate.isFormalMany())
			return true;

		return false;
	}

	private boolean isBondable(SubDescription bondCandidate) {

		if ((!bondCandidate.isFormal()) && (!bondCandidate.hasBonds()))
			return true;

		if (bondCandidate.isFormalOne() && !bondCandidate.hasBonds())
			return true;

		if (bondCandidate.isFormalMany())
			return true;

		return false;
	}

}
