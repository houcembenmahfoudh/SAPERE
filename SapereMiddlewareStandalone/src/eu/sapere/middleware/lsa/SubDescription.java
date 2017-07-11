package eu.sapere.middleware.lsa;

import java.util.Map;

import eu.sapere.middleware.lsa.values.SyntheticPropertyName;

/**
 * Represents a SubDescription
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public class SubDescription extends Description {

	protected String name = null;

	/**
	 * Initializes a SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param id
	 *            The id of the SubDescription
	 * @param p
	 *            The content of the SubDescription
	 */
	public SubDescription(String name, Id id, Property p) {
		super(id, p);
		this.name = name;
	}

	/**
	 * Initializes a SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param id
	 *            The id of the SubDescription
	 * @param properties
	 *            The content of the SubDescription
	 */
	public SubDescription(String name, Id id, Property[] properties) {
		super(id, properties);
		this.name = name;
	}

	protected SubDescription(String name, Id id, Map<String, Property> properties,
			Map<SyntheticPropertyName, Property> syntheticProperties) {
		super(id, properties, syntheticProperties);
		this.name = name;
	}

	@Override
	public SubDescription getCopy() {
		Description description = super.getCopy();
		SubDescription sd = new SubDescription(name, description.getId().getCopy(), description.Properties,
				description.SyntheticProperties);
		return sd;
	}

	/**
	 * Returns the name of this SubDescription
	 * 
	 * @return the name of this SubDescription
	 */
	public String getName() {
		return name;
	}

	// /**
	// * Returns the content of this SubDescription
	// * @return an iterator of properties contained in this subdescription
	// */
	// public Iterator<Property> getProperties() {
	//
	// Iterator<Property> ret = null;
	//
	// ret = this.Properties.values().iterator();
	//
	// return ret;
	// }

	/**
	 * Returns the content of this SubDescription
	 * 
	 * @return an array of properties contained in this subdescription
	 */
	public Property[] getProperties() {

		Property[] ret = new Property[Properties.size()];

		int i = 0;
		for (Property p : Properties.values()) {
			ret[i] = p;
			i++;
		}
		return ret;
	}

	/**
	 * Returns the specified property
	 * 
	 * @param name
	 *            The name of the property
	 * @return The property
	 */
	public Property getProperty(String name) {
		return Properties.get(name);
	}

	/**
	 * Checks a SubDescription for property containment.
	 * 
	 * @param name
	 *            the name of the property.
	 * @return true if the property exists, false otherwise.
	 */
	public boolean hasProperty(String name) {
		return Properties.containsKey(name);
	}

}
