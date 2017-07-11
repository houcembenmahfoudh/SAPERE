package eu.sapere.middleware.node.lsaspace;

import java.io.Serializable;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;

/**
 * Represents an operation submitted to the LSA space.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class Operation implements Serializable {

	private static final long serialVersionUID = -5118578211958268938L;
	private Lsa lsa = null;
	private Id lsaId = null;
	private OperationType opType = null;
	private String requestingId = null;

	private Agent requestingAgent = null;

	/**
	 * Gets the Lsa involved in the Operation
	 * 
	 * @return the lsa
	 */
	public Lsa getLsa() {
		return lsa;
	}

	/**
	 * Sets the lsa involved in the Operation
	 * 
	 * @param lsa
	 *            the lsa to set
	 */
	public void setLsa(Lsa lsa) {
		this.lsa = lsa;
	}

	/**
	 * Gets the lsaId of the lsa involved in the Operation
	 * 
	 * @return lsaId the lsaId of the involved Lsa
	 */
	public Id getLsaId() {
		return lsaId;
	}

	/**
	 * Sets the lsaId of the lsa involved in the Operation
	 * 
	 * @param lsaId
	 *            the lsaId of the involved Lsa
	 */
	public void setLsaId(Id lsaId) {
		this.lsaId = lsaId;
		this.lsa.setId(lsaId);
	}

	/**
	 * Gets the type of the Operation
	 * 
	 * @return opType the type of the Operation
	 */
	public OperationType getOpType() {
		return opType;
	}

	/**
	 * Sets the type of the Operation
	 * 
	 * @param opType
	 *            the type of the Operation
	 */
	public void setOpType(OperationType opType) {
		this.opType = opType;
	}

	/**
	 * Gets the name of the Agent that requested the Operation
	 * 
	 * @return the name of the Agent that requested the Operation
	 */
	public String getRequestingId() {
		return requestingId;
	}

	/**
	 * Gets the reference to the Agent that requested the Operation
	 * 
	 * @return the reference to the Agent that requested the Operation
	 */
	public Agent getRequestingAgent() {
		return requestingAgent;
	}

	/**
	 * Sets the name of the Agent that requested the Operation
	 * 
	 * @param requestingId
	 *            the name of the Agent that requested the Operation
	 */
	public void setRequestingId(String requestingId) {
		this.requestingId = requestingId;
	}

	/**
	 * An Operation to inject an LSA in the local space
	 * 
	 * @param lsa
	 *            the LSA to be injected
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @param requestingAgent
	 *            the reference to the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation injectOperation(Lsa lsa, String requestingId, Agent requestingAgent) {

		this.opType = OperationType.Inject;
		this.lsaId = null; // To be decided where should be fixed
		this.lsa = lsa.getCopy();
		this.requestingId = requestingId;
		this.requestingAgent = requestingAgent;

		return this;
	}

	/**
	 * An Operation to update the content of an LSA in the local space
	 * 
	 * @param lsa
	 *            the new LSA content
	 * @param lsaId
	 *            the id of the LSA to be updated with the new content
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @param requestingAgent
	 *            the reference to the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation updateOperation(Lsa lsa, Id lsaId, String requestingId, Agent requestingAgent) {
		this.opType = OperationType.Update;
		this.lsaId = new Id(lsaId.toString());
		this.lsa = lsa.getCopy();
		this.requestingId = requestingId;
		this.requestingAgent = requestingAgent;

		return this;
	}

	/**
	 * An Operation to update the bonds of an LSA in the local space
	 * 
	 * @param lsa
	 *            the new LSA content
	 * @param lsaId
	 *            the id of the LSA to be updated with the new content
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation updateBondOperation(Lsa lsa, Id lsaId, String requestingId) {
		this.opType = OperationType.BondUpdate;
		this.lsaId = new Id(lsaId.toString());
		this.lsa = lsa.getCopy();
		this.requestingId = new String(requestingId);

		return this;
	}

	// /**
	// * An Operation to update the content of an LSA in the local space
	// * @param lsa the new LSA content
	// * @param lsaId the id of the LSA to be updated with the new content
	// * @param requestingId the name of the Agent that creates the Operation
	// * @return the reference to the Operation
	// */
	// public Operation updateParametrizedOperation(Lsa lsa, Id lsaId, String
	// requestingId){
	// this.opType = OperationType.UpdateParametrized;
	// this.lsaId = new Id(lsaId.toString());
	// this.lsa = lsa.getCopy();
	// this.requestingId = new String(requestingId);
	//
	// return this;
	// }

	/**
	 * An Operation to update the bonds of an LSA in the local space
	 * 
	 * @param lsa
	 *            the new LSA content
	 * @param lsaId
	 *            the id of the LSA to be updated with the new content
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation updateBondParametrizedOperation(Lsa lsa, Id lsaId, String requestingId) {
		this.opType = OperationType.BondUpdateParametrized;
		this.lsaId = new Id(lsaId.toString());
		this.lsa = lsa.getCopy();
		this.requestingId = new String(requestingId);

		return this;
	}

	/**
	 * An Operation to remove a LSA from the local space
	 * 
	 * @param lsaId
	 *            the Id of the LSA to be removed
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation removeOperation(Id lsaId, String requestingId) {

		this.opType = OperationType.Remove;
		this.lsaId = new Id(lsaId.toString());
		this.requestingId = new String(requestingId);

		return this;
	}

	/**
	 * An Operation to read a LSA from the local space
	 * 
	 * @param lsaId
	 *            the Id of the LSA to be read
	 * @param requestingId
	 *            the name of the Agent that creates the Operation
	 * @param requestingAgent
	 *            the reference to the Agent that creates the Operation
	 * @return the reference to the Operation
	 */
	public Operation readOperation(Id lsaId, String requestingId, Agent requestingAgent) {

		this.opType = OperationType.Read;
		this.lsaId = new Id(lsaId.toString());
		this.requestingId = new String(requestingId);
		this.requestingAgent = requestingAgent;

		return this;
	}

	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public String toString(){
	// String res = "operation[type="+opType;
	// if(this.lsaId != null)
	// res+=",lsaId="+this.lsaId;
	// if(lsa != null)
	// res+=",lsa="+lsa;
	// if (requestingId != null)
	// res+=",requestedBy="+requestingId;
	// return res+="]";
	// }

}