package eu.sapere.middleware.lsa.interfaces;

import java.util.Iterator;
import java.util.Set;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.SubDescription;
import eu.sapere.middleware.lsa.exception.MalformedDescriptionException;
import eu.sapere.middleware.lsa.exception.UnresolvedPropertyNameException;

/**
 * Provides an interface for LSAs
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public interface ILsa {

	/**
	 * Adds the given Property to the LSA
	 * 
	 * @param p
	 *            The property to be added
	 * @return The LSA whith the added Property
	 * @throws MalformedDescriptionException
	 */
	public Lsa addProperty(Property p) throws MalformedDescriptionException;

	/**
	 * Adds the given Properties to the LSA
	 * 
	 * @param properties
	 *            The properties to be added
	 * @return The LSA whith the added properties
	 * @throws MalformedDescriptionException
	 */
	public Lsa addProperty(Property... properties);

	/**
	 * Removes the given Property from the LSA
	 * 
	 * @param propertyName
	 *            The name of the property to be removed
	 * @return The LSA whith the removed Property
	 * @throws MalformedDescriptionException
	 */
	public Lsa removeProperty(String propertyName);

	/**
	 * Sets the value of the specified Property
	 * 
	 * @param p
	 *            The new Property
	 * @return The LSA whith the updated Property
	 * @throws UnresolvedPropertyNameException
	 */
	public Lsa setProperty(Property p) throws UnresolvedPropertyNameException;

	/**
	 * Gets the Property with the given name
	 * 
	 * @param name
	 *            The name of the Property
	 * @return The Property
	 */
	public Property getProperty(String name);

	/**
	 * Adds a SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription to be added
	 * @param value
	 *            The content
	 * @return The added SubDescription
	 */
	public SubDescription addSubDescription(String name, Property value);

	/**
	 * Adds a SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription to be added
	 * @param values
	 *            The content
	 * @return The added SubDescription
	 */
	public SubDescription addSubDescription(String name, Property... values);

	/**
	 * Removes the given SubDescription from the LSA
	 * 
	 * @param name
	 *            The name of the SubDescription to be removed
	 * @return The LSA whith the removed SubDescription
	 * @throws MalformedDescriptionException
	 */
	public Lsa removeSubDescription(String name);

	/**
	 * Sets the value of the specified SubDescription
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @param value
	 *            The new Property value
	 * @return The interface to the SubDescription
	 */
	public SubDescription setSubDescription(String name, Property value);

	/**
	 * Gets the SubDescription with the given name
	 * 
	 * @param name
	 *            The name of the SubDescription
	 * @return The SubDescription
	 */
	public SubDescription getSubDescriptionByName(String name);

	/**
	 * Gets the names of the SubDescription of the LSA
	 * 
	 * @return Returns a Set of SubDescriptions of this LSA
	 */
	public Set<String> getSubDescriptionNames();

	/**
	 * Returns an iterator of properties of this LSA
	 * 
	 * @return an iterator of properties of this LSA
	 */
	public Iterator<Property> getProperties();

	/**
	 * Returns an iterator of subdescriptions of this LSA
	 * 
	 * @return an iterator of subdescriptions of this LSA
	 */
	public Iterator<SubDescription> getSDs();

	/**
	 * @param name
	 * @return first property
	 */
	public String getFirstProperty(String name);

}
