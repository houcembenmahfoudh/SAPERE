package eu.sapere.middleware.agent;

import eu.sapere.middleware.lsa.Id;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.NodeManager;
import eu.sapere.middleware.node.lsaspace.Operation;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.notifier.event.AbstractSapereEvent;
import eu.sapere.middleware.node.lsaspace.Agent;

/**
 * The implementation provided for Data LSA. Once a Data LSA is injected in the
 * Space, it is no longer on control of the entity that injected it, i.e., the
 * events that happens to the LSA are not reported
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class BasicSapereAgent extends Agent {

	/** The reference to the local node's Operation Manager */
	private OperationManager opMng = null;

	/**
	 * Instantiates a Basic Sapere Agent
	 * 
	 * @param agentName
	 *            The name of the Agent
	 */
	public BasicSapereAgent(String agentName) {
		super(agentName);
		this.opMng = NodeManager.instance().getOperationManager();
	}

	/**
	 * Injects the given LSA in the local Lsa space
	 * 
	 * @param lsa
	 *            The LSA to be injected
	 * @return The id of the injected LSA
	 */
	public Id injectOperation(Lsa lsa) {
		Id lsaId = null;

		Operation op = new Operation().injectOperation((Lsa) lsa,
				getAgentName(), this);
		lsaId = opMng.queueOperation(op);
		return lsaId;
	}

	/**
	 * Updates an LSA
	 * 
	 * @param lsa
	 *            The new LSA
	 * @param lsaId
	 *            The id of the LSA to be updated
	 */
	public void updateOperation(Lsa lsa, Id lsaId) {
		Operation op = new Operation().updateOperation(lsa, lsaId,
				getAgentName(), null);
		opMng.queueOperation(op);
	}

	/**
	 * Removes the specified LSA from the local LSA space
	 * 
	 * @param lsa
	 */
	public void removeLsa(Lsa lsa) {
		Operation op = new Operation().removeOperation(lsa.getId(),
				getAgentName());
		opMng.queueOperation(op);
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {
		// TODO Auto-generated method stub
		
	}

}
