package eu.sapere.middleware.lsa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import eu.sapere.middleware.lsa.exception.MalformedDescriptionException;
import eu.sapere.middleware.lsa.exception.UnresolvedPropertyNameException;
import eu.sapere.middleware.lsa.interfaces.ILsa;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * Represents an LSA.
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class Lsa implements Serializable, ILsa {

	private static final long serialVersionUID = 7123251264036747994L;

	/** The id of the LSA */
	private Id id = null;

	/** The content of the LSA */
	protected LsaContent content;

	/**
	 * Retrieves the id of the LSA
	 * 
	 * @return the id of the LSA
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the id of the LSA
	 * 
	 * @param id
	 *            The id of the LSA
	 */
	public void setId(Id id) {
		this.id = id;
		setSdId(id);
	}

	/**
	 * Sets the id for a Subdescription
	 * 
	 * @param id
	 */
	private void setSdId(Id id) {
		SubDescription sd[] = content.getSubDescriptions();

		if (sd != null)
			for (int i = 0; i < sd.length; i++) {
				if (!sd[i].getId().toString().contains("#"))
					sd[i].getId().setIdentifier(id + "#" + sd[i].getId().toString());
			}
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return content.equals(null);

	}

	/**
	 * Sets the id for a Copy of the LSA
	 * 
	 * @param id
	 *            The id
	 */
	private void setIdCopy(Id id) {
		this.id = id;
	}

	/**
	 * Returns a copy of the LSA
	 * 
	 * @param id
	 *            The id of the LSA to be copied
	 * @param content
	 *            The content of the LSA to be copied
	 * @return A copy of the LSA
	 */
	private Lsa LsaCopy(Id id, LsaContent content) {
		Lsa n = new Lsa();
		n.setIdCopy(this.id);
		n.content = content;
		return n;
	}

	/**
	 * Initializes an empty LSA
	 */
	public Lsa() {
		this(null);
	}

	/**
	 * Initializes an empty LSA with the given id
	 * 
	 * @param id
	 */
	public Lsa(Id id) {
		this.id = id;
		this.content = new LsaContent();
	}

	/*
	 * private Lsa (LsaId id, LsaContent content){ this.id = id; this.content =
	 * content; }
	 */

	/**
	 * Adds the given Property to the LSA
	 */
	public Lsa addProperty(Property p) throws MalformedDescriptionException {
		if (PropertyName.isDecay(p.getName())) {
			content.addDecayProperty(p.getName(), p);
		}
		content.addProperty(p.getName(), p);
		return this;
	}

	/**
	 * Adds the given Properties to the LSA
	 */
	public Lsa addProperty(Property... properties) {

		Property p[] = properties;
		for (int i = 0; i < p.length; i++) {
			addProperty(p[i]);
		}

		return this;
	}

	/**
	 * Returns true if the LSA is a neighbour LSA
	 * 
	 * @return true if the LSA is a neighbour LSA, false otherwise
	 */
	public boolean isNeighbour() {
		// return
		// (content.hasProperty(NeighbourLsa.NEIGHBOUR.toString())&&(content.hasProperty("ipAddress")));
		return (content.hasProperty("neighbour"));
	}

	/**
	 * Returns true is the LSA is a Propagate LSA
	 * 
	 * @return true is the LSA is a Propagate LSA, false otherwise
	 */
	public boolean isPropagate() {
		return (hasDiffusionOp() || hasDestination());
	}

	/**
	 * Returns true is the LSA has a Propagation Operator Property
	 * 
	 * @param p
	 *            The Propagation Operator Property Name
	 * @return true is the LSA has a Propagation Operator Property, false
	 *         otherwise
	 */
	public boolean hasPropagatotionOp(String p) {
		return content.hasProperty(p.toString());
	}

	/**
	 * Returns true is the LSA has a Diffusion Operator Property
	 * 
	 * @return true is the LSA has a Diffusion Operator Property, false
	 *         otherwise
	 */
	private boolean hasDiffusionOp() {
		return content.hasProperty(PropertyName.DIFFUSION_OP.toString());
	}

	/**
	 * Returns true is the LSA has a Destination Property
	 * 
	 * @return true is the LSA has a Destination Property, false otherwise
	 */
	private boolean hasDestination() {
		return content.hasProperty(PropertyName.DESTINATION.toString());
	}

	/**
	 * Adds a Bond to the specified LSA id
	 * 
	 * @param lsaId
	 *            The id of the LSA bonded
	 * @return the LSA
	 */
	public Lsa addBond(String lsaId) {
		content.addBond(new Id(lsaId));
		return this;
	}

	/**
	 * Removes the Bond with the specified LSA id
	 * 
	 * @param lsaId
	 *            The id of the LSA to which the bond is removed
	 * @return The LSA
	 */
	public Lsa removeBond(String lsaId) {
		content.removeBond(new Id(lsaId));
		return this;
	}

	/**
	 * Removes a Property from the LSA
	 */
	public Lsa removeProperty(String propertyName) {
		content.removeProperty(propertyName);
		return this;
	}

	/**
	 * Removes a Synthetic Property from the LSA
	 * 
	 * @param propertyName
	 *            The name of the Property to be removed
	 * @return The LSA
	 */
	public Lsa removeSyntheticProperty(SyntheticPropertyName propertyName) {
		content.removeSyntheticProperty(propertyName);
		return this;
	}

	/**
	 * Sets the value of the specified Property
	 */
	public Lsa setProperty(Property p) throws UnresolvedPropertyNameException {

		if (!content.hasProperty(p.getName()))
			throw new UnresolvedPropertyNameException();
		else
			content.setProperty(p);
		return this;
	}

	/**
	 * Adds a SubDescription to the LSA. This SubDescription is made up by a
	 * single Property
	 */
	public SubDescription addSubDescription(String name, Property value) {
		return (SubDescription) content.addSubDescription(name, value);

	}

	/**
	 * Adds a SubDescription to the LSA. The SubDescription is made up by
	 * multiple Properties
	 */
	public SubDescription addSubDescription(String name, Property... values) {
		return (SubDescription) content.addSubDescription(name, values);
	}

	/**
	 * Removes a SubDescription from the LSA.
	 */
	public Lsa removeSubDescription(String name) {
		content.removeSubDescription(name);
		return this;
	}

	/**
	 * Updates the Value of a SubDescription
	 */
	public SubDescription setSubDescription(String name, Property value) {
		return (SubDescription) content.setSubDescription(name, value);
	}

	// To do: setSubDescription(String name, Property... values)

	/**
	 * Adds a SyntheticProperty to the LSA
	 * 
	 * @param p
	 *            The Synthetic Property to be added
	 * @return The LSA
	 */
	public Lsa addProperty(SyntheticProperty p) {
		content.addProperty(SyntheticPropertyName.getSyntheticPropertiesValues(p.getName()), p);
		return this;
	}

	/**
	 * Retrieves the SubDescriptions of the LSA
	 * 
	 * @return An array with the SubDescription of the LSA
	 */
	public SubDescription[] getSubDescriptions() {

		return content.getSubDescriptions();

	}

	/**
	 * Retrieves the SubDescription specified
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescriptionByName(String name) {
		return content.getSubDescriptionByName(name);
	}

	/**
	 * Retrieves the SubDescription specified
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescriptionById(String id) {
		return content.getSubDescriptionById(id);
	}

	/**
	 * Returns true if the LSA has a SubDescription with the specified name
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @return true if the LSA has a SubDescription with the specified name,
	 *         false otherwise
	 */
	public boolean hasSubDescription(String name) {
		SubDescription sd = content.getSubDescriptionByName(name);
		if (sd != null)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the LSA has at least a SubDescription
	 * 
	 * @return true if the LSA has at least a SubDescription, false otherwise
	 */
	public boolean hasSubDescriptions() {
		return content.hasSubDescriptions();
	}

	/**
	 * Returns true if the LSA has the specified Synthetic Property
	 * 
	 * @param name
	 *            The name of the Synthetic Property
	 * @return true if the LSA has at least a Synthetic Property, false
	 *         otherwise
	 */
	public boolean hasSyntheticProperty(SyntheticPropertyName name) {
		return content.SyntheticProperties.containsKey(name); // VISIBILIT�
																// SyntheticProperties
																// sistemare
	}

	/**
	 * Retrieves the specified Synthetic PRoperty
	 * 
	 * @param name
	 *            The name of the Synthetic Property
	 * @return The Property
	 */
	public Property getSyntheticProperty(SyntheticPropertyName name) {
		return content.SyntheticProperties.get(name); // VISIBILIT�
														// SyntheticProperties
														// sistemare
	}

	/**
	 * Returns true if the LSA has a Bond with the specified LSA id. Both LSA
	 * and its SubDescriptions
	 * 
	 * @param LsaId
	 *            the LSA id
	 * @return true if the LSA has a Bond with the specified LSA id, false
	 *         otherwise
	 */

	public boolean hasBondWith(Id LsaId) {
		return content.hasBond(LsaId.toString());
	}

	/**
	 * Returns true if the LSA has at least one bond. Does not check
	 * SubDescriptions
	 * 
	 * @return true if the LSA has at least one bond, false otherwise
	 */
	public boolean hasBonds() {
		return content.hasBonds();
	}

	/**
	 * Returns true if the LSA has a Property with the specified name
	 * 
	 * @param name
	 *            The name of the property
	 * @return true if the LSA has a Property with the specified name, false
	 *         otherwise
	 */
	public boolean hasProperty(String name) {
		if (PropertyName.isDecay(name))
			return hasDecayProperty();
		return content.hasProperty(name);
	}

	/**
	 * Returns true if the LSA has the Decay Property
	 * 
	 * @return true if the LSA has the Decay Property, false otherwise
	 */
	public boolean hasDecayProperty() {
		return content.hasProperty(PropertyName.DECAY.toString());
	}

	/**
	 * Returns the value of the Decay Property of the LSA
	 * 
	 * @return the Integer value of the Decay Property of the LSA
	 */
	public Integer getDecayProperty() {
		return PropertyName.getDecayValue(content.getProperty(PropertyName.DECAY.toString()).getValue().elementAt(0));
	}

	/**
	 * Returns true if the LSA has the Aggregation Operator Property
	 * 
	 * @return true if the LSA has the Aggregation Operator Property, false
	 *         otherwise
	 */
	public boolean hasAggregationOp() {
		return content.hasProperty(PropertyName.AGGREGATION_OP.toString());
	}

	/**
	 * Returns true if the LSA has the Property Field Value
	 * 
	 * @return true if the LSA has the Property Field Value, false otherwise
	 */
	public boolean hasFieldValue() {
		return content.hasProperty(PropertyName.FIELD_VALUE.toString());
	}

	/**
	 * Returns true if the LSA has the Source Property
	 * 
	 * @return true if the LSA has the Source Property, false otherwise
	 */
	public boolean hasSource() {
		return content.hasProperty(PropertyName.SOURCE.toString());
	}

	/**
	 * Returns the value of the Aggregation Operator Property
	 * 
	 * @return the value of the Aggregation Operator Property
	 */
	public String getAggregationOp() {
		return content.getProperty(PropertyName.AGGREGATION_OP.toString()).getValue().elementAt(0);
	}

	/**
	 * Returns the value of the Field Value Property
	 * 
	 * @return the value of the Field Value Property
	 */
	public String getFieldValue() {
		return content.getProperty(PropertyName.FIELD_VALUE.toString()).getValue().elementAt(0);
	}

	/**
	 * Returns the value of the Source Property
	 * 
	 * @return the value of the Source Property
	 */
	public String getSource() {
		return content.getProperty(PropertyName.SOURCE.toString()).getValue().elementAt(0);
	}

	/**
	 * Returns the value of the Previous Property
	 * 
	 * @return the value of the Previous Property
	 */
	public String getPrevious() {
		return content.getProperty(PropertyName.PREVIOUS.toString()).getValue().elementAt(0);
	}

	/**
	 * Returns the specified Property
	 */
	public Property getProperty(String name) {
		if (content.hasProperty(name))
			return content.getProperty(name);
		else
			return null;
	}

	/**
	 * Returns the copy of the LSA
	 * 
	 * @return the copy of the LSA
	 */
	public Lsa getCopy() {
		return LsaCopy(id, content.getCopy());
	}

	/**
	 * Returns a string representation of this LSA
	 */
	public String toString() {
		return "<" + id + "," + content.toString() + ">";
	}

	/**
	 * Returns the Content of this LSA
	 * 
	 * @return the Content of this LSA
	 */
	public LsaContent getContent() {
		return content;
	}

	/**
	 * Returns true if this LSA contains a Property with a Formal value
	 * 
	 * @return true if this LSA contains a Property with a Formal value, false
	 *         otherwise
	 */
	public boolean isFormal() {
		boolean ret = false;
		ret = content.isFormal();
		return ret;
	}

	/**
	 * Returns true if this LSA contains a Property with a Formal ? value
	 * 
	 * @return true if this LSA contains a Property with a Formal ? value, false
	 *         otherwise
	 */
	public boolean isFormalOne() {
		boolean ret = false;
		ret = content.isFormalOne();
		return ret;
	}

	/**
	 * Returns true if this LSA contains a Property with a Formal * value
	 * 
	 * @return true if this LSA contains a Property with a Formal * value, false
	 *         otherwise
	 */
	public boolean isFormalMany() {
		boolean ret = false;
		ret = content.isFormalMany();
		return ret;
	}

	/**
	 * Returns true is this LSA matches the specified LSA
	 * 
	 * @param matcher
	 *            the LSA to check the match with
	 * @return true is this LSA matches the specified LSA, false otherwise
	 */
	public boolean matches(Lsa matcher) {
		boolean ret = false;
		if ((content.Properties.size() == 0) && (content.SubDescriptions.size() == 0))
			return ret;
		ret = content.propertiesMatch(matcher.content);
		return ret;
	}

	/**
	 * Returns true is this LSA matches the specified SubDescription
	 * 
	 * @param matcher
	 *            the SubDescription to check the match with
	 * @return true is this LSA matches the specified SubDescription, false
	 *         otherwise
	 */
	public boolean matches(SubDescription matcher) {
		boolean ret = false;
		if ((content.Properties.size() == 0) && (content.SubDescriptions.size() == 0))
			return ret;
		ret = content.propertiesMatch(matcher);
		return ret;
	}

	/**
	 * Retrieves the name of the SubDescription
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @return The name of the SubDescription
	 */
	public String getSubDescriptionName(Id id) {
		String ret = null;
		ret = content.getSubDescriptionName((Id) id);

		return ret;
	}

	/**
	 * Removes all the Bonds of this LSA
	 * 
	 * @return The LSA
	 */
	public Lsa removeBonds() {
		if (hasBonds()) {
			removeSyntheticProperty(SyntheticPropertyName.BONDS);
			if (hasSubDescriptions()) {
				SubDescription sd[] = getSubDescriptions();
				for (int i = 0; i < sd.length; i++) {
					sd[i].removeBonds();
				}
			}
		}
		return this;
	}

	/**
	 * Returns true if the LSA is subject to the Aggregation eco-law
	 * 
	 * @return true if the LSA is subject to the Aggregation eco-law, false
	 *         otherwise
	 */
	public boolean aggregationApplies() {
		return ((hasAggregationOp() && hasFieldValue() && hasSource()) || (hasAggregationOp() && hasFieldValue()));
	}

	/**
	 * Returns true is the LSA is subject to self Aggregation
	 * 
	 * @return true is the LSA is subject to self Aggregation
	 */
	public boolean explicitAggregationApplies() {
		return (hasAggregationOp() && hasFieldValue() && hasSource());
	}

	/**
	 * Returns true is the LSA is subject to other Aggregation
	 * 
	 * @return true is the LSA is subject to other Aggregation
	 */
	public boolean requestedAggregationApplies() {
		return (hasAggregationOp() && hasFieldValue() && !hasSource());
	}

	/**
	 * Returns true is the LSA has the Aggregation source Property
	 * 
	 * @return true is the LSA has the Aggregation source Property
	 */
	public String getAggregationSource() {

		if (content.hasProperty(PropertyName.SOURCE.toString())) {
			return content.getProperty(PropertyName.SOURCE.toString()).getValue().elementAt(0);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getSubDescriptionNames() {
		return content.getSubDescriptionNames();
	}

	/**
	 * Returns a string representation of this LSA
	 * 
	 * @return the String representation of the LSA
	 */
	public String toVisualString() {
		return "<" + id + content.toVisualString() + ">";
	}

	/**
	 * Provides quick access to the first value of a property in the vector.
	 * 
	 * @param name
	 *            The name of the Property
	 * @return an Optional containing the first value in the vector if it
	 *         exists.
	 */
	public String getSingleValue(String name) {
		final Property property = content.getProperty(name);
		if (property != null && property.getValue() != null && !property.getValue().isEmpty()) {
			return property.getValue().firstElement();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Property> getProperties() {

		Iterator<Property> ret = null;

		ret = this.content.Properties.values().iterator();

		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<SubDescription> getSDs() {

		Iterator<SubDescription> ret = null;

		ret = this.content.SubDescriptions.values().iterator();

		return ret;
	}

	@Override
	public String getFirstProperty(String name) {
		if (content.hasProperty(name))
			return content.getProperty(name).getValue().firstElement();
		else
			return null;
	}
}
