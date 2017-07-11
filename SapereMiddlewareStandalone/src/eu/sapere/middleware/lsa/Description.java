package eu.sapere.middleware.lsa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.sapere.middleware.lsa.exception.MalformedDescriptionException;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * 
 * Represents a SubDescription
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Description implements Serializable {

	private static final long serialVersionUID = 7319728270982187084L;

	/** The SubDescription id */
	private Id id = null;

	/** The Properties of the SubDescription */
	protected Map<String, Property> Properties = null;

	/** The Synthetic Properties of the SubDescription. Only Bonds is admitted */
	protected Map<SyntheticPropertyName, Property> SyntheticProperties = null;

	/** The status of the SubDescription */
	private boolean isMalformed = false;

	/**
	 * Returns true if this SubDescription is malformed
	 * 
	 * @return Returns true if this SubDescription is malformed, false otherwise
	 */
	public boolean isMalformed() {
		return isMalformed;
	}

	/**
	 * Instantiates a Description
	 */
	public Description() {
		Properties = new HashMap<String, Property>();
		SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();
	}

	/**
	 * Initializes this SubDescription with the specified id and Property
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @param p
	 *            The Property contained in this SubDescription
	 */
	public Description(Id id, Property p) {

		this.id = id;
		Properties = new HashMap<String, Property>();
		SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();
		Properties.put(p.getName(), p);

	}

	/**
	 * Initializes this SubDescription with the specified id and Properties
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @param properties
	 *            The Properties contained in this SubDescription
	 */
	public Description(Id id, Property... properties) {

		Property p[] = properties;
		this.id = id;
		Properties = new HashMap<String, Property>();
		SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();

		for (int i = 0; i < p.length; i++) {
			addProperty(p[i]);
		}
	}

	/**
	 * Adds the specified Property to this Description
	 * 
	 * @param p
	 *            The property to be added
	 * @return This Subdescription updated
	 * @throws MalformedDescriptionException
	 */
	public Description addProperty(Property p)
			throws MalformedDescriptionException {

		boolean hasQueryValues = p.contains(ValueType.FORMAL_MANY)
				|| p.contains(ValueType.FORMAL_ONE);

		if (hasQueryValues) {
			@SuppressWarnings("rawtypes")
			Iterator iterator = this.Properties.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				if ((Properties.get(key).getValue()
						.contains(ValueType.FORMAL_MANY) || Properties.get(key)
						.getValue().contains(ValueType.FORMAL_ONE)))
					throw new MalformedDescriptionException();
			}
		}
		Properties.put(p.getName(), p);

		return this;
	}

	/**
	 * Adds the specified Properties to this SubDescription
	 * 
	 * @param properties
	 *            The properties to be added
	 * @return This Subdescription updated
	 * @throws MalformedDescriptionException
	 */
	public Description addProperty(Property... properties) {
		Property p[] = properties;
		for (int i = 0; i < p.length; i++) {
			addProperty(p[i]);
		}
		return this;
	}

	/**
	 * Removes the Property with the specified name from this SubDescription
	 * 
	 * @param propertyName
	 *            The name of the PRoperty
	 * @return This SubDescription updated
	 */
	public Description removeProperty(String propertyName) {
		if (Properties.containsKey(propertyName))
			Properties.remove(propertyName);
		return this;
	}

	/**
	 * Removes all the bonds from this AubDescription
	 * 
	 * @return This subdescription updated
	 */
	public Description removeBonds() {
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS))
			SyntheticProperties.remove(SyntheticPropertyName.BONDS);
		return this;
	}

	/**
	 * Updates the content of the specified Property
	 * 
	 * @param p
	 *            The Property to be updated
	 * @return This SubDescription updated
	 */
	public Description setProperty(Property p) {
		if (Properties.containsKey(p.getName()))
			Properties.put(p.getName(), p);
		return this;
	}

	/**
	 * Add a bond to the specified id
	 * 
	 * @param id
	 *            The id that has been bonded
	 */
	public void addBond(String id) {
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS)) {
			// devo controllare i duplicati
			if (!SyntheticProperties.get(SyntheticPropertyName.BONDS)
					.getValue().contains(id))
				SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue()
						.add(id);
		} else {
			SyntheticProperties.put(SyntheticPropertyName.BONDS,
					new SyntheticProperty(SyntheticPropertyName.BONDS, id));
		}
	}

	/**
	 * Retrieves a SyntheticProperty
	 * 
	 * @param name
	 *            The name of the SyntheticProperty
	 * @return The Property
	 */
	public Property getSyntheticProperty(SyntheticPropertyName name) {
		return SyntheticProperties.get(name);
	}

	/**
	 * Returns true if this Description contains a Property with a Formal value
	 * 
	 * @return true if this Description contains a Property with a Formal value,
	 *         false otherwise
	 */
	public boolean isFormal() {
		boolean ret = false;

		@SuppressWarnings("rawtypes")
		Iterator i = Properties.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			if (Properties.get(key).getPropertyValue().isFormal())
				ret = true;
		}

		return ret;
	}

	/**
	 * Returns true if this SubDescription contains a Property with a Formal ?
	 * value
	 * 
	 * @return true if this SubDescription contains a Property with a Formal ?
	 *         value, false otherwise
	 */
	public boolean isFormalOne() {
		boolean ret = false;

		Iterator<String> i = Properties.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			if (Properties.get(key).contains(ValueType.FORMAL_ONE.toString()))
				ret = true;
		}

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

		Iterator<String> i = Properties.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			if (Properties.get(key).contains(ValueType.FORMAL_MANY.toString()))
				ret = true;
		}

		return ret;
	}

	/**
	 * Returns true if this SubDescription has bonds
	 * 
	 * @return true if this SubDescription has bonds, false otherwise
	 */
	public boolean hasBonds() {
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS)) {

			if ((SyntheticProperties.get(SyntheticPropertyName.BONDS)
					.getValue().size() >= 1))
				return true;
			else
				return false;
		} else
			return false;

	}

	/**
	 * Returns true if this SubDescription has a bond with the specified id
	 * 
	 * @param id
	 *            The id
	 * @return true if this SubDescription has a bond with the specified id,
	 *         false otherwise
	 */
	public boolean hasBond(String id) {
		boolean ret = false;
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS))
			if (SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue()
					.contains(id))
				ret = true;
		return ret;
	}

	/**
	 * Removes the bond with the specified id
	 * 
	 * @param id
	 *            The id of the LSA to which the bonds has to been removed
	 * @return This SubDescription updated
	 */
	public boolean removeBond(String id) {
		boolean ret = false;
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS)) {
			ret = SyntheticProperties.get(SyntheticPropertyName.BONDS)
					.getValue().remove(id);
			if (SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue()
					.size() == 0)
				SyntheticProperties.remove(SyntheticPropertyName.BONDS);
		}

		return ret;
	}

	/**
	 * Returns a string representation of this SubDescription
	 */
	public String toString() {
		return "<" + id.toString() + ", " + Properties.toString() + ", "
				+ SyntheticProperties.toString() + "> ";
	}

	/**
	 * Initializes a SubDescription with the specified content
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @param Properties
	 *            The Properties
	 * @param SyntheticProperties
	 *            The SyntheticProperties
	 */
	protected Description(Id id, Map<String, Property> Properties,
			Map<SyntheticPropertyName, Property> SyntheticProperties) {
		this.id = id;
		this.Properties = new HashMap<String, Property>();
		this.SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();

		this.Properties = Properties;
		this.SyntheticProperties = SyntheticProperties;

	}

	/**
	 * Returns the copy of this SubDescription
	 * 
	 * @return The copy of this SubDescription
	 */
	public Description getCopy() {
		Id id = null;
		Map<String, Property> Properties = new HashMap<String, Property>();
		Map<SyntheticPropertyName, Property> SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();
		;

		id = this.getId().getCopy();

		@SuppressWarnings("rawtypes")
		Iterator iterator = this.Properties.keySet().iterator();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Properties.put(new String(key), this.Properties.get(key).getCopy());
		}

		iterator = this.SyntheticProperties.keySet().iterator();
		while (iterator.hasNext()) {
			SyntheticPropertyName key = (SyntheticPropertyName) iterator.next();
			SyntheticProperties.put(key, this.SyntheticProperties.get(key)
					.getCopy());
		}

		/*
		 * iterator = this.SyntheticProperties.keySet().iterator(); while
		 * (iterator.hasNext()){ SyntheticPropertyName key =
		 * (SyntheticPropertyName) iterator.next(); SyntheticProperties.put(key,
		 * this.SyntheticProperties.get(key).getCopy()); }
		 */

		return new Description(id, Properties, SyntheticProperties);
	}

	/**
	 * Returns true is this SubDescription matches the specified LSA
	 * 
	 * @param matcher
	 *            the LSA to check the match with
	 * @return true is this SubDescription matches the specified LSA, false
	 *         otherwise
	 */
	public boolean matches(Lsa matcher) {
		boolean ret = true;

		if (matcher == null)
			return false;
		Iterator<String> iterarThis = Properties.keySet().iterator();
		while (iterarThis.hasNext()) {
			String key = (String) iterarThis.next();
			Property p = Properties.get(key);
			if (matcher.getContent().Properties.containsKey(key)) {

				if (!p.matches(matcher.getContent().Properties.get(key)))
					ret = false;

			} else {
				ret = false;
				break;
			}

		}

		return ret;
	}

	/**
	 * Returns true is this SubDescription matches the specified SubDescription
	 * 
	 * @param matcher
	 *            the SubDescription to check the match with
	 * @return true is this SubDescription matches the specified SubDescription,
	 *         false otherwise
	 */
	public boolean matches(Description matcher) {
		boolean ret = true;

		Iterator<String> iterarThis = Properties.keySet().iterator();
		while (iterarThis.hasNext()) {
			String key = (String) iterarThis.next();
			Property p = Properties.get(key);
			if (matcher.Properties.containsKey(key)) {

				if (!p.matches(matcher.Properties.get(key)))
					ret = false;

			} else {
				ret = false;
				break;
			}

		}

		return ret;
	}

	/**
	 * Retrieves the id of this Subdescription
	 * 
	 * @return The id of this description
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the id of this SubDescription
	 * 
	 * @param id
	 *            The id to be set
	 */
	public void setId(Id id) {
		this.id = id;
	}

}
