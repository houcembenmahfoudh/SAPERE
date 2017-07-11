package eu.sapere.middleware.lsa;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import eu.sapere.middleware.lsa.exception.IllegalSyntheticPropertyException;
import eu.sapere.middleware.lsa.exception.MalformedDescriptionException;
import eu.sapere.middleware.lsa.exception.MalformedPropertyException;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * Represent the Content of an LSA
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class LsaContent extends Description {

	private static final long serialVersionUID = 1L;

	/** Contains the SubDescriptions of an LSA */
	protected Map<String, SubDescription> SubDescriptions = null;

	/** Next SubDescription id */
	private long nextSdId = -1L;

	/**
	 * Intializes the LSA Content
	 */
	public LsaContent() {
		this.Properties = new HashMap<String, Property>();
		this.SubDescriptions = new HashMap<String, SubDescription>();
		this.SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();
	}

	/**
	 * Initializes the LSA Content
	 * 
	 * @param Properties
	 *            The Properties of this LSA Content
	 * @param SubDescriptions
	 *            The SubDescriptions of this LSA Content
	 * @param SyntheticProperties
	 *            The Synthetic Properties of this LSA Content
	 */
	// Note: SYNTHETIC
	private LsaContent(Map<String, Property> Properties, Map<String, SubDescription> SubDescriptions,
			Map<SyntheticPropertyName, Property> SyntheticProperties) {
		this.Properties = new HashMap<String, Property>();
		this.SubDescriptions = new HashMap<String, SubDescription>();
		this.SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();

		this.Properties = Properties;
		this.SubDescriptions = SubDescriptions;
		this.SyntheticProperties = SyntheticProperties;

	}

	/**
	 * Returns the name of the specified SubDescription id
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @return The name of the SubDescription
	 */
	public String getSubDescriptionName(Id id) {
		String ret = null;

		Iterator<String> iterator = this.SubDescriptions.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (this.SubDescriptions.get(key).getId().equals(id.toString())) {
				return key;
			}

		}

		return ret;
	}

	/**
	 * Returns the name of this LSA Content
	 * 
	 * @return The Copy of this LSA Content
	 */
	public LsaContent getCopy() {
		Map<String, Property> Properties = new HashMap<String, Property>();
		Map<String, SubDescription> SubDescriptions = new HashMap<String, SubDescription>();
		Map<SyntheticPropertyName, Property> SyntheticProperties = new HashMap<SyntheticPropertyName, Property>();

		@SuppressWarnings("rawtypes")
		Iterator iterator = this.Properties.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Properties.put(new String(key), this.Properties.get(key).getCopy());
		}

		iterator = this.SubDescriptions.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			SubDescriptions.put(new String(key), this.SubDescriptions.get(key).getCopy());
		}

		iterator = this.SyntheticProperties.keySet().iterator();
		while (iterator.hasNext()) {
			SyntheticPropertyName key = (SyntheticPropertyName) iterator.next();
			SyntheticProperties.put(key, this.SyntheticProperties.get(key).getCopy());
		}

		return new LsaContent(Properties, SubDescriptions, SyntheticProperties);
	}

	/**
	 * Adds the specified Property to this LSA Content
	 * 
	 * @param name
	 *            The name of the Property
	 * @param value
	 *            The Property to be added
	 * @throws MalformedDescriptionException
	 */
	public void addProperty(String name, Property value) throws MalformedDescriptionException {
		boolean hasQueryValues = value.contains(ValueType.FORMAL_MANY) || value.contains(ValueType.FORMAL_ONE);

		if (hasQueryValues) {
			Iterator<String> iterator = this.Properties.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				if ((Properties.get(key).getValue().contains(ValueType.FORMAL_MANY)
						|| Properties.get(key).getValue().contains(ValueType.FORMAL_ONE)))
					throw new MalformedDescriptionException();
			}
		}

		Properties.put(name, value);

	}

	/**
	 * Add a Decay Property with the specified initial value
	 * 
	 * @param name
	 *            The name of the Decay Property
	 * @param value
	 *            The initial value of the Decay field
	 * @throws MalformedPropertyException
	 */
	public void addDecayProperty(String name, Property value) throws MalformedPropertyException {

		if (((value.getValue().size()) == 1) && (PropertyName.isDecayValue(value.getValue().elementAt(0)))) {

			addProperty(PropertyName.DECAY.toString(), value);
		} else
			throw new MalformedPropertyException();
	}

	/**
	 * Adds a Synthetic Property to this Content LSA
	 * 
	 * @param name
	 *            The name of the Synthetic Property
	 * @param value
	 *            The value of the Property
	 */
	public void addProperty(SyntheticPropertyName name, SyntheticProperty value) {
		// SyntheticProperties.put(name, value);
		addSyntheticProperty(name, value);
	}

	/**
	 * Adds a Property to this LSA Content
	 * 
	 * @param name
	 *            The name of the Property
	 * @param value
	 *            The value of the Property
	 * @throws IllegalSyntheticPropertyException
	 */
	public void addProperty(String name, SyntheticProperty value) throws IllegalSyntheticPropertyException {

		SyntheticPropertyName spv = null;
		spv = SyntheticPropertyName.getSyntheticPropertiesValues(name);
		if (spv == null)
			throw new IllegalSyntheticPropertyException();

		else
			addSyntheticProperty(spv, value);
		// SyntheticProperties.put(spv, value);

	}

	/**
	 * Adds a Synthetic Property to this LSA Content
	 * 
	 * @param name
	 *            The name of the Synthetic Property
	 * @param value
	 *            The Synthetic Property
	 */
	public void addSyntheticProperty(SyntheticPropertyName name, Property value) {

		if (name.equals(SyntheticPropertyName.BONDS)) {

			Vector<String> v = value.getValue();
			for (int i = 0; i < v.size(); i++)
				addBond(new Id(v.elementAt(i)));
		} else
			SyntheticProperties.put(name, value);
	}

	/**
	 * Adds a bond to the correspondent Synthetic Property Bond
	 * 
	 * @param lsaId
	 *            The id of the LSA to be added
	 */
	public void addBond(Id lsaId) {
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS)) {

			if (!SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().contains(lsaId))
				SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().add(lsaId.toString());
		} else
			SyntheticProperties.put(SyntheticPropertyName.BONDS,
					new SyntheticProperty(SyntheticPropertyName.BONDS, lsaId.toString()));

	}

	/**
	 * Removes a bond to the specified LSA id
	 * 
	 * @param lsaId
	 *            The id of the LSA to be removed
	 */
	public void removeBond(Id lsaId) {
		if (SyntheticProperties.containsKey(SyntheticPropertyName.BONDS)) {

			if (SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().contains(lsaId))
				SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().remove(lsaId.toString());
		}
		if (SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().isEmpty())
			SyntheticProperties.remove(SyntheticPropertyName.BONDS);

	}

	/**
	 * Removes a Synthetic Property
	 * 
	 * @param name
	 *            The name of the Synthetic Property
	 */
	public void removeSyntheticProperty(SyntheticPropertyName name) {
		if (SyntheticProperties.containsKey(name))
			SyntheticProperties.remove(name);
	}

	/**
	 * Updates a Synthetic Property with the specified value
	 * 
	 * @param p
	 *            The Synthetic Property value
	 */
	public void setSyntheticProperty(Property p) {
		if (SyntheticProperties.containsKey(p.getName()))
			addProperty(p.getName(), p);

	}

	/**
	 * Returns true if this LSA Content has a Property with the specified name
	 * 
	 * @param name
	 *            The name of the Property
	 * @return true if this LSA Content has a Property with the specified name,
	 *         false otherwise
	 */
	public boolean hasProperty(String name) {
		return Properties.containsKey(name);
	}

	/**
	 * Retrieves the specified Property
	 * 
	 * @param name
	 *            The name of the Property
	 * @return The Property
	 */
	public Property getProperty(String name) {
		return Properties.get(name);
	}

	/**
	 * Returns true if this LSA Content has a Synthetic Property with the
	 * specified name
	 * 
	 * @param name
	 *            The name of the specified Synthetic Property
	 * @return true if this LSA Content has a Synthetic Property with the
	 *         specified name, false otherwise
	 */
	public boolean hasSyntheticProperty(String name) {
		return SyntheticProperties.containsKey("#" + name);
	}

	/**
	 * Retrieves the specified Synthetic Property
	 * 
	 * @param name
	 *            The name of the specified Synthetic Property
	 * @return The Synthetic Property
	 */
	public Property getSyntheticProperty(String name) {
		return SyntheticProperties.get("#" + name);
	}

	/**
	 * Returns true if this LSA Content has a Bond with the specified LSA id
	 * 
	 * @param LsaId
	 *            The id of the LSA
	 * @return true if this LSA Content has a Bond with the specified LSA id,
	 *         false otherwise
	 */
	public boolean hasBond(String LsaId) {
		boolean ret = false;

		if (hasBonds()) {
			if (SyntheticProperties.get(SyntheticPropertyName.BONDS).getValue().contains(LsaId))
				ret = true;
		}

		if (ret == false) {

			Iterator<String> iterator = this.SubDescriptions.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				SubDescription sd = SubDescriptions.get(key);
				if (sd.hasBond(LsaId)) {
					ret = true;
					break;
				}

			}
		}

		return ret;
	}

	/**
	 * Adds a SubDescription with a single Property to this LSA Content
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param value
	 *            The Property
	 * @return The added SubDescription
	 */
	public SubDescription addSubDescription(String name, Property value) {
		SubDescriptions.put(name.toString(), new SubDescription(name, getSdId(), value)); 
		return SubDescriptions.get(name);
	}

	/**
	 * Updates the value of a SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param value
	 *            The SubDescription
	 * @return The updated SubDescription
	 */
	public SubDescription setSubDescription(String name, Property value) {
		if (SubDescriptions.containsKey(name))
			SubDescriptions.put(name, new SubDescription(name, SubDescriptions.get(name).getId(), value));

		return SubDescriptions.get(name);

	}

	/**
	 * Adds a SubDescription with multiple Properties to this LSA Content
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param values
	 *            The Properties
	 * @return The added subdescription
	 */
	public SubDescription addSubDescription(String name, Property... values) {

		SubDescription sd = new SubDescription(name, getSdId(), values);
		if (!sd.isMalformed()) {

			SubDescriptions.put(name, new SubDescription(name, getSdId(), values));
			return SubDescriptions.get(name);
		} else
			return null;
	}

	/**
	 * Retrieves the SubDescriptions of this LSA Content
	 * 
	 * @return The array of SubDescriptions
	 */
	public SubDescription[] getSubDescriptions() {
		SubDescription s[] = new SubDescription[0];
		if (!SubDescriptions.isEmpty()) {
			s = new SubDescription[SubDescriptions.size()];
			Iterator<String> iterator = this.SubDescriptions.keySet().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				s[i] = SubDescriptions.get(key);
				i++;
			}
		}
		return s;
	}

	/**
	 * Retrieves the specified SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescriptionByName(String name) {

		if (!SubDescriptions.isEmpty()) {
			Iterator<String> iterator = this.SubDescriptions.keySet().iterator();

			while (iterator.hasNext()) {

				String key = (String) iterator.next();
				if (key.equals(name)) {
					return SubDescriptions.get(key);
				}
			}

		}

		return null;
	}

	/**
	 * Retrieves the specified SubDescription
	 * 
	 * @param id
	 *            The id of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescriptionById(String id) {
		if (!SubDescriptions.isEmpty()) {
			Iterator<String> iterator = this.SubDescriptions.keySet().iterator();

			while (iterator.hasNext()) {

				String key = (String) iterator.next();
				if (SubDescriptions.get(key).getId().equals(id)) {

					return SubDescriptions.get(key);
				}
			}

		}

		return null;
	}

	/**
	 * Retrieves the specified SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescription(Id name) {
		// to do

		return null;
	}

	/**
	 * Returns true if this LSA Content has SubDescriptions
	 * 
	 * @return true if this LSA Content has SubDescriptions, false otherwise
	 */
	public boolean hasSubDescriptions() {
		if (SubDescriptions.isEmpty())
			return false;
		else
			return true;
	}

	/**
	 * Removes the specified SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 */
	public void removeSubDescription(String name) {
		if (SubDescriptions.containsKey(name)) {
			SubDescriptions.remove(name);
		}
	}

	/**
	 * Returns a string representation of this LSA Content
	 */
	public String toString() {
		// Edit 18/10/12 - Graeme: rewritten for alphabetical ordering of
		// properties
		StringBuilder ret = new StringBuilder();
		// Add properties
		{
			List<String> properties = new LinkedList<String>(Properties.keySet());
			Collections.sort(properties);
			for (String property : properties) {
				ret.append(property + "=" + Properties.get(property) + ", ");
			}
		}

		// Add sub-descriptions properties
		{
			List<String> properties = new LinkedList<String>(SubDescriptions.keySet());
			Collections.sort(properties);
			for (String property : properties) {
				ret.append(property + "=" + SubDescriptions.get(property) + ", ");
			}
		}

		// Add synthetic properties
		{
			List<SyntheticPropertyName> properties = new LinkedList<SyntheticPropertyName>(
					SyntheticProperties.keySet());
			Collections.sort(properties);
			for (SyntheticPropertyName property : properties) {
				ret.append(property + "=" + SyntheticProperties.get(property) + ", ");
			}
		}
		return ret.toString();
	}

	/**
	 * Returns the next SubDescription id
	 * 
	 * @return SubDescriptionId
	 */
	public Id getSdId() {
		nextSdId++;
		return new Id("" + nextSdId).getCopy();
	}

	/**
	 * Returns true if this LSA Content matches the specified LSA Content
	 * 
	 * @return true if this LSA Content matches the specified LSA Content, false
	 *         otherwise
	 */
	protected boolean propertiesMatch(LsaContent matcherContent) {
		boolean ret = true;

		Iterator<String> iterarThis = Properties.keySet().iterator();
		while (iterarThis.hasNext()) {
			String key = (String) iterarThis.next();
			Property p = Properties.get(key);
			if (matcherContent.Properties.containsKey(key)) {
				if (!p.matches(matcherContent.Properties.get(key)))
					ret = false;

			} else {
				ret = false;
				break;
			}

		}

		return ret;
	}

	/**
	 * Returns true if this LSA Content matches the specified SubDescription
	 * 
	 * @return true if this LSA Content matches the specified SubDescription,
	 *         false otherwise
	 */
	protected boolean propertiesMatch(SubDescription matcherContent) {
		boolean ret = true;

		Iterator<String> iterarThis = Properties.keySet().iterator();
		while (iterarThis.hasNext()) {
			String key = (String) iterarThis.next();
			Property p = Properties.get(key);
			if (matcherContent != null) {
				if (matcherContent.Properties.containsKey(key)) {

					if (!p.matches(matcherContent.Properties.get(key)))
						ret = false;

				} else {
					ret = false;
					break;
				}
			} else
				ret = false;

		}

		return ret;
	}

	/**
	 * @return
	 * 
	 */
	public Set<String> getSubDescriptionNames() {
		return SubDescriptions.keySet();

	}

	/**
	 * Returns a string representation of this LSA Content
	 * 
	 * @return The string representation of this Lsa content
	 */
	public String toVisualString() {
		String ret = new String();

		if (!Properties.isEmpty())
			ret += ",\n" + Properties.toString().replaceAll("\\{|\\}", "");
		if (!SubDescriptions.isEmpty())
			ret += ",\n " + SubDescriptions.toString().replaceAll("\\{|\\}", "");
		/*
		 * if(! SyntheticProperties.isEmpty()) ret +=
		 * ",\n "+SyntheticProperties.toString().replaceAll("\\{|\\}", "");
		 */

		return ret;
	}

}
