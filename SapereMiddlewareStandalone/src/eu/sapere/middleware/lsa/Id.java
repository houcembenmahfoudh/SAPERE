package eu.sapere.middleware.lsa;

import java.io.Serializable;

/**
 * Class that represent the identifier of an LSA. It contains the real
 * identifier and an optional namespace, both values are Strings
 * 
 * @author Matteo Desanti (UNIBO)
 * 
 */
public class Id implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String namespace = null;
	protected String identifier = null;

	/**
	 * Creates a new LsaId with the specified parameters
	 * 
	 * @param namespace
	 *            namespace for the identifier
	 * @param identifier
	 *            the real identifier
	 */
	public Id(String namespace, String identifier) {
		this.namespace = namespace;
		this.identifier = identifier;
	}

	/**
	 * Creates a new LsaId based on the passed parameter string. The constuctor
	 * will recognize a pattern of type n:i where n is set as namespace and i as
	 * identifier.
	 * 
	 * @param string
	 *            identifier
	 */
	public Id(String string) {
		String[] sa = string.split(":", 2);
		if (sa.length == 1) {
			this.namespace = null;
			this.identifier = sa[0];
		} else {
			this.namespace = sa[0];
			this.identifier = sa[1];
		}

	}

	/**
	 * Getter for the namespace
	 * 
	 * @return the namespace of this LsaId
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Setter for the identifier
	 * 
	 * @return the identifier of this LsaId
	 */
	public String getIdentifier() {
		return identifier;
	}

	public String toString() {
		if (namespace == null)
			return identifier;
		else
			return namespace + ":" + identifier;
	}

	/**
	 * Returns an exact copy of this LsaId
	 * 
	 * @return a copy of this LsaId
	 */
	public Id getCopy() {
		return new Id(namespace, identifier);
	}

	@Override
	public boolean equals(Object p) {
		if (p instanceof Id) {
			Id ob = (Id) p;
			return (namespace == ob.getNamespace())
					&& (identifier == ob.getIdentifier());
		} else if (p instanceof String) {
			String ob = (String) p;
			return (this.toString().equals(ob));
		} else
			return false;
	}

	/**
	 * Return true if the specified LSA id is a SubDescriptionId
	 * 
	 * @param lsaId
	 * @return true if the specified LSA id is a SubDescriptionId, false
	 *         otherwise
	 */
	public boolean isSdId(Id lsaId) {
		// if (lsaId instanceof SubDescriptionId)
		// return true;
		// TO BE FIXED
		if (lsaId.toString().lastIndexOf("#") == lsaId.toString().indexOf("#"))
			return true;
		else
			return false;
	}

	/**
	 * @param identifier
	 *            The identifier of this SubDescriptionId
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
