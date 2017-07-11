package eu.sapere.middleware.node.lsaspace;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.SubDescription;
import eu.sapere.middleware.lsa.SyntheticProperty;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;
import eu.sapere.middleware.node.lsaspace.console.LsaSpaceConsole;
import eu.sapere.middleware.node.notifier.INotifier;
import eu.sapere.middleware.node.notifier.event.AggregationRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondAddedEvent;
import eu.sapere.middleware.node.notifier.event.BondRemovedEvent;
import eu.sapere.middleware.node.notifier.event.BondedLsaUpdateEvent;
import eu.sapere.middleware.node.notifier.event.ReadEvent;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;

/**
 * The SAPERE LSA space implementation.
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class Space {

	private HashMap<String, Lsa> space = null;

	private String name = null;

	private static long nextId = 0L;

	private INotifier notifier = null;

	private LsaSpaceConsole console = null;

	/**
	 * Instantiates a LSA space.
	 * 
	 * @param name
	 *            The name of the local SAPERE Node
	 * @param notifier
	 *            The reference to the Notfier
	 * @param console
	 *            The refernce to the GUI
	 */
	public Space(String name, INotifier notifier, LsaSpaceConsole console) {
		space = new HashMap<String, Lsa>();
		this.name = name;
		this.notifier = notifier;
		this.console = console;
	}

	private void updateConsole() {
		if (console != null)
			console.update(getAllLsa());
	}

	/**
	 * Injects a copy of the LSA into the LSA space
	 * 
	 * @param lsa
	 *            the LSA to be injected
	 * @param creatorId
	 *            the name of the Agent that requests the injection
	 */
	public void inject(Lsa lsa, String creatorId) {
		Lsa copy = lsa.getCopy();

		// add Synthetic Properties
		copy.addProperty(new SyntheticProperty(SyntheticPropertyName.CREATOR_ID, creatorId));
		String currentTime = "" + new Date().getTime();
		copy.addProperty(new SyntheticProperty(SyntheticPropertyName.CREATION_TIME, currentTime));
		copy.addProperty(new SyntheticProperty(SyntheticPropertyName.LAST_MODIFIED, currentTime));
		copy.addProperty(new SyntheticProperty(SyntheticPropertyName.LOCATION, "local"));

		space.put(lsa.getId().toString(), copy);
		updateConsole();
	}

	/**
	 * Updates a LSA in the LSA space
	 * 
	 * @param lsaId
	 *            the id of the LSA to be updated
	 * @param lsa
	 *            the new content fo the LSA
	 * @param creatorId
	 *            the name of the Agent that requests the update
	 */
	public void update(Id lsaId, Lsa lsa, String creatorId) {
		update(lsaId, lsa, creatorId, true, true);
	}

	/**
	 * Updates a LSA in the LSA space
	 * 
	 * @param lsaId
	 *            the id of the LSA to be updated
	 * @param lsa
	 *            the new content for the LSA
	 * @param creatorId
	 *            the name of the Agent that requests the update
	 * @param destroyBonds
	 *            true if already existing bonds can be destroyed, false
	 *            otherwise
	 * @param generateEvents
	 *            true if events must be triggered, false otherwise
	 */
	public void update(Id lsaId, Lsa lsa, String creatorId, boolean destroyBonds, boolean generateEvents) {
		Lsa copy = lsa.getCopy();
		Lsa oldLsa = space.get(lsaId.toString());

		boolean bondJustAdded = false;

		if (space.containsKey(lsaId.toString()))
			if (space.get(lsaId.toString()).getSyntheticProperty(SyntheticPropertyName.CREATOR_ID) != null)
				if (space.get(lsaId.toString()).getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
						.elementAt(0) == creatorId || creatorId.equals(EcoLawsEngine.ECO_LAWS.toString())
						|| creatorId.equals(EcoLawsEngine.ECO_LAWS_DECAY.toString())) {

					copy.setId(new Id(lsaId.toString()));

					String currentTime = "" + new Date().getTime();
					copy.addProperty(new SyntheticProperty(SyntheticPropertyName.CREATOR_ID, space.get(lsaId.toString())
							.getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue().elementAt(0)));
					copy.addProperty(
							(SyntheticProperty) oldLsa.getSyntheticProperty(SyntheticPropertyName.CREATION_TIME));
					copy.addProperty(new SyntheticProperty(SyntheticPropertyName.LAST_MODIFIED, currentTime));
					copy.addProperty(new SyntheticProperty(SyntheticPropertyName.LOCATION, "local"));
					if (creatorId == EcoLawsEngine.ECO_LAWS.toString()) {
						if (lsa.hasBonds())
							for (int i = 0; i < lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue()
									.size(); i++)
								copy.addBond(
										lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue().elementAt(i));
					}

					else {
						if (oldLsa.hasBonds())
							for (int i = 0; i < oldLsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue()
									.size(); i++)
								copy.addBond(oldLsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue()
										.elementAt(i));

						SubDescription sd[] = copy.getSubDescriptions();
						if (sd != null)
							for (int i = 0; i < sd.length; i++) {
								SubDescription oldSd = oldLsa.getSubDescriptionById(sd[i].getId().toString());
								if (oldSd != null)
									if (oldSd.hasBonds()) {
										Vector<String> oldBonds = oldSd
												.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue();
										for (int j = 0; j < oldBonds.size(); j++)
											sd[i].addBond(oldBonds.elementAt(j));

									}
							}
					}

					space.put(copy.getId().toString(), copy);

					if (destroyBonds)
						destroyBonds();

					// Delete to generate BondedLsaUpdateEvent also for
					// synthetic props
					/*
					 * if (generateEvents) {SapereEvent event = new
					 * BondedLsaUpdateEvent(copy);
					 * event.setRequiringAgent(null); notifier.publish(event);}
					 */

					String subjectId = copy.getId().toString();
					// if(destroyBonds)
					// destroyBonds(this.getAllLsa(), this);

					// Update the lsa
					lsa = space.get(subjectId);

					// Generate Events
					if (lsa.hasBonds()) {

						for (int i = 0; i < lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue()
								.size(); i++) {

							if (!oldLsa.hasBondWith(new Id(
									lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue().elementAt(i)))) {

								Lsa bondedLsa = getLsa(
										lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue().elementAt(i));
								AbstractSapereEvent event = new BondAddedEvent(copy.getCopy(),
										lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue().elementAt(i),
										bondedLsa);
								event.setRequiringAgent(new String(space.get(lsaId.toString())
										.getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
										.elementAt(0)));

								notifier.publish(event);
								bondJustAdded = true;

								// generatedBond = true;

							}
						} // end for

					}
					if (lsa.hasSubDescriptions()) {
						SubDescription sd[] = lsa.getSubDescriptions();
						for (int i = 0; i < sd.length; i++) {
							if (sd[i].hasBonds()) {
								Vector<String> bonds = sd[i].getSyntheticProperty(SyntheticPropertyName.BONDS)
										.getValue();
								for (int j = 0; j < bonds.size(); j++) {
									// System.out.println("\t"+bonds.elementAt(j));
									// System.out.println(oldLsa);
									if (!oldLsa.hasBondWith(new Id(bonds.elementAt(j)))) {

										Lsa bondedLsa = getLsa(bonds.elementAt(j));

										AbstractSapereEvent event = new BondAddedEvent(copy.getCopy(),
												bonds.elementAt(j), bondedLsa);
										event.setRequiringAgent(space.get(lsaId.toString())
												.getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
												.elementAt(0));
										notifier.publish(event);
										bondJustAdded = true;

										// generatedBond = true;

									}
								}
							}
						}
					}

				}

		// does not fire the events if a Bond has just been created
		if (!bondJustAdded) {
			AbstractSapereEvent event = new BondedLsaUpdateEvent(copy);
			event.setRequiringAgent(null);
			notifier.publish(event);
		}

		updateConsole();
	}

	/**
	 * Removes a LSA from the LSA space
	 * 
	 * @param lsaId
	 *            the Id of the LSA to be removed
	 * @param creatorId
	 *            the name of the Agent that requests the removal
	 */
	public void remove(Id lsaId, String creatorId) {

		// If the LSA does no longer exist, return
		if (space.get(lsaId.toString()) == null)
			return;

		// used to remove subscriptions to notifications
		String agentCreatorId = space.get(lsaId.toString()).getSyntheticProperty(SyntheticPropertyName.CREATOR_ID)
				.getValue().elementAt(0);

		if (space.containsKey(lsaId.toString()))
			if (space.get(lsaId.toString()).hasSyntheticProperty(SyntheticPropertyName.CREATOR_ID))
				if (space.get(lsaId.toString()).getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
						.elementAt(0).equals(creatorId) || creatorId.equals(EcoLawsEngine.ECO_LAWS)
						|| creatorId.equals(EcoLawsEngine.ECO_LAWS_DECAY)
						|| creatorId.equals(EcoLawsEngine.ECO_LAWS_AGGREGATION)
						|| creatorId.equals(EcoLawsEngine.ECO_LAWS_PROPAGATION)) {

					Lsa oldLsa = space.get(lsaId.toString());
					space.remove(lsaId.toString());
					if (creatorId.equals(EcoLawsEngine.ECO_LAWS_DECAY)) {
						// // Fire event LsaExpired
						// AbstractSapereEvent event = new
						// LsaExpiredEvent(oldLsa);
						// event.setRequiringAgent(oldLsa.getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
						// .elementAt(0).toString());
						// notifier.publish(event);
					}

					if (creatorId.equals(EcoLawsEngine.ECO_LAWS_AGGREGATION)) {

						AbstractSapereEvent event = new AggregationRemovedEvent(oldLsa);
						event.setRequiringAgent(oldLsa.getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue()
								.elementAt(0).toString());

						notifier.publish(event);
					}
				}

		// unsubscribes notifications for creatorId for the removed LSA
		notifier.unsubscribe(agentCreatorId);

		destroyBonds();

		if (!creatorId.equals(EcoLawsEngine.ECO_LAWS_DECAY) && !creatorId.equals(EcoLawsEngine.ECO_LAWS_AGGREGATION)) {
			// notification to component Tracker
			if (console != null)
				console.removeLsa(lsaId.toString());
		}

		updateConsole();
	}

	/**
	 * Reads a LSA from the LSA space
	 * 
	 * @param targetId
	 *            the Id of the LSA to be read
	 * @param requestingId
	 *            the name of the Agent that requests the read
	 */
	public void read(Id targetId, String requestingId) {
		Lsa ret = null;

		if (space.containsKey(targetId.toString())) {
			Lsa target = space.get(targetId.toString());

			ret = target.getCopy();
		}

		// TO DO: check a chain of bonds instead of a single bond

		/*
		 * String targetCreator =
		 * target.getSyntheticProperty(SyntheticPropertyName
		 * .CREATOR_ID).getValue().firstElement(); if
		 * (targetCreator.equals(requestingId)){ ret = target.getCopy(); } else{
		 * Iterator<String> iterator = space.keySet().iterator(); while
		 * (iterator.hasNext()){ String key = (String) iterator.next(); Lsa lsa
		 * = space.get(key); String creatorId =
		 * lsa.getSyntheticProperty(SyntheticPropertyName
		 * .CREATOR_ID).getValue().firstElement(); if
		 * (creatorId.equals(requestingId)) if (lsa.hasBondWith(targetId)){ ret
		 * = target.getCopy(); break;} } } }
		 */

		if (ret != null) {
			AbstractSapereEvent event = new ReadEvent(ret);
			event.setRequiringAgent(requestingId);
			notifier.publish(event);
		}

		// updateConsole();
	}

	private Lsa getLsa(String key) {
		Id id = new Id(key);
		Lsa ret = null;

		if (!id.isSdId(id)) {
			if (space.containsKey(key.substring(0, key.lastIndexOf("#")))) {
				ret = space.get(key.substring(0, key.lastIndexOf("#")));
			}
		} else {
			if (space.containsKey(key)) {
				ret = space.get(key);
			}
		}
		return ret;
	}

	/**
	 * Returns a free Id for LSA to be used
	 * 
	 * @return a free Id
	 */
	public Id getFreshId() {
		while (space.containsKey(name + "#" + nextId)) {
			nextId++;
		}
		return new Id(name + "#" + nextId++).getCopy();
	}

	/**
	 * Returns the Space name
	 * 
	 * @return the Space name
	 */
	public String getName() {
		return name;
	}

	// public void setName(String name) {
	// this.name = name;
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return space.values().toString();
	}

	/**
	 * Returns an array containing all the LSAs in this Space
	 * 
	 * @return an array containing all the LSAs in this Space
	 */
	public Lsa[] getAllLsa() {
		Lsa[] array = new Lsa[space.values().size()];

		return space.values().toArray(array);
	}

	private boolean isLsaId(String s) {
		if (s.indexOf("#") != s.lastIndexOf("#"))
			return false;
		return true;
	}

	private void destroyBonds() {
		for (Lsa lsa : getAllLsa()) {
			if (lsa.hasBonds()) {
				Vector<String> activeBonds = lsa.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue();
				Vector<String> removeBonds = new Vector<String>(); // Bonds to
																	// be
																	// removed
				for (String bond : activeBonds) {
					// Differentiate based on whether bond is with an LSA or
					// sub-description.
					if (isLsaId(bond)) {
						if (!space.containsKey(bond)) {
							removeBonds.add(bond);
						} else {

							// System.out.println("DB\t"+lsa+" - "+bond);
							if (!lsa.matches(getLsa(bond)) && !getLsa(bond).matches(lsa)) {
								removeBonds.add(bond);
								// System.out.println("\t \t remove bond:
								// "+bond);
							}
						}
					} else {
						String lsaId = bond.substring(0, bond.lastIndexOf("#"));
						if (getLsa(lsaId) == null) {
							removeBonds.add(bond);
						} else {
							if (getLsa(lsaId).getSubDescriptionById(bond) == null)
								removeBonds.add(bond);
							else if (!lsa.matches(getLsa(lsaId).getSubDescriptionById(bond))
									&& getLsa(lsaId).getSubDescriptionById(bond) != null
									&& !getLsa(lsaId).getSubDescriptionById(bond).hasBond(lsa.getId().toString()))
								removeBonds.add(bond);
							// System.out.println("\t \t remove bond: "+bond);
						}
					}
				}

				for (String removeBond : removeBonds) {
					lsa.removeBond(removeBond);
					if (!isLsaId(removeBond))
						removeBond = removeBond.substring(0, removeBond.lastIndexOf("#"));
					AbstractSapereEvent event = new BondRemovedEvent(lsa.getCopy(), removeBond);
					event.setRequiringAgent(lsa.getCopy().getSyntheticProperty(SyntheticPropertyName.CREATOR_ID)
							.getValue().elementAt(0));
					notifier.publish(event);

					// try to remove in the other
					Lsa exBondedLsa = getLsa(removeBond);
					if (exBondedLsa != null)
						if (exBondedLsa.hasBondWith(lsa.getId())) {
							exBondedLsa.removeBond(lsa.getId().toString());

							if (!isLsaId(removeBond))
								removeBond = removeBond.substring(0, removeBond.lastIndexOf("#"));
							{
								AbstractSapereEvent event2 = new BondRemovedEvent(exBondedLsa.getCopy(),
										lsa.getId().toString());
								event2.setRequiringAgent(
										exBondedLsa.getCopy().getSyntheticProperty(SyntheticPropertyName.CREATOR_ID)
												.getValue().elementAt(0));
								notifier.publish(event2);

							}

							if (console != null)
								console.removeBond(exBondedLsa.getId().toString(), lsa.getId().toString());

						}
				}
			}
			// Remove subdescription bonds
			for (SubDescription subDesc : lsa.getSubDescriptions()) {
				removeSubBonds(lsa, subDesc);
			}

		}

	}

	// TO DO: check events
	private void removeSubBonds(Lsa anLsa, SubDescription aSubDesc) {
		if (aSubDesc.hasBonds()) {
			Vector<String> activeSubBonds = aSubDesc.getSyntheticProperty(SyntheticPropertyName.BONDS).getValue();
			Vector<String> removeSubBonds = new Vector<String>(); // Bonds to be
																	// removed
			for (String subBond : activeSubBonds) {
				if (isLsaId(subBond)) {
					if (!aSubDesc.matches(getLsa(subBond)))
						removeSubBonds.add(subBond);
				} else {
					String lsaId = subBond.substring(0, subBond.lastIndexOf("#"));
					// System.err.println(SimClock.getIntTime() +
					// ": "+a_subDesc.getId() + " "+getLsa(LsaId) + " " + " " +
					// getLsa(LsaId).getSubDescriptionById(subBond));
					SubDescription bondedSubdescription = getLsa(lsaId).getSubDescriptionById(subBond);
					if (bondedSubdescription == null || !aSubDesc.matches(bondedSubdescription))
						removeSubBonds.add(subBond);
				}
			}

			for (String removeSubBond : removeSubBonds) {
				aSubDesc.removeBond(removeSubBond);
				AbstractSapereEvent event = new BondRemovedEvent(anLsa.getCopy(), removeSubBond);
				event.setRequiringAgent(
						anLsa.getCopy().getSyntheticProperty(SyntheticPropertyName.CREATOR_ID).getValue().elementAt(0));
				notifier.publish(event);
			}

		}
	}

	/**
	 * Returns true if the Visual Inspector is active.
	 * 
	 * @return true if the Visual Inspector is active.
	 */
	public boolean hasVisualInspector() {
		if (console != null)
			return console.hasInspector();
		else
			return false;
	}

	/**
	 * Visualise a Propagation in the Visual Inspector.
	 * 
	 * @param lsa
	 *            the LSA that has been propagated
	 */
	public void visualPropagate(Id lsa) {
		if (console != null)
			console.propagateLsa(lsa.toString());
	}

	/**
	 * Visualize a Bond between LSAs
	 * 
	 * @param from
	 *            The starting LSA
	 * @param to
	 *            The ending LSA
	 * @param bidirectionalBond
	 *            true if the bond is bidirectional, false otherwise
	 */
	public void addVisualBond(Id from, Id to, boolean bidirectionalBond) {

		String fromString = from.toString();
		String toString = to.toString();

		if (!from.isSdId(from))
			fromString = from.toString().substring(0, from.toString().lastIndexOf("#"));
		if (!to.isSdId(to))
			toString = to.toString().substring(0, to.toString().lastIndexOf("#"));

		if (console != null)
			if (bidirectionalBond)
				console.addBidirectionalBond(fromString, toString);
			else
				console.addUnidirectionalBond(fromString, toString);
	}

	/**
	 * @param id
	 * @return
	 */
	public Lsa getLsaById(UUID id) {
		try {
			for (Lsa lsa : this.getAllLsa()) {
				if (lsa.getProperty("uuid") != null
						&& UUID.fromString(lsa.getProperty("uuid").getValue().elementAt(0)).equals(id))
					return lsa;
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

}