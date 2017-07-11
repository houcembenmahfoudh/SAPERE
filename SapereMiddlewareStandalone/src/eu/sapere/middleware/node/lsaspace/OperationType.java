package eu.sapere.middleware.node.lsaspace;

import java.io.Serializable;

/**
 * Enumeration for the possible type of Operation over a space
 * 
 * @author Matteo Desanti (UNIBO)
 * @author Gabriella Castelli (UNIMORE)
 * 
 */

public enum OperationType implements Serializable {
	/**
	 * Inject Operation
	 */
	Inject,

	// Observe,

	/**
	 * Remove Operation
	 */
	Remove,

	/**
	 * Update Operation
	 */
	Update,

	/**
	 * Update of Bonds
	 */
	BondUpdate,

	/**
	 * Update Operation
	 */
	UpdateParametrized,
	/**
	 * Update of Bonds
	 */
	BondUpdateParametrized,

	/**
	 * Read Operation
	 */
	Read
}
