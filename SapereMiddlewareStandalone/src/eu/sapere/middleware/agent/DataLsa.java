package eu.sapere.middleware.agent;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.interfaces.ILsa;
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
public class DataLsa extends Agent {

	/**
	 * Injects the provided LSA into the local LSA space
	 * 
	 * @param lsa
	 *            LSa to be injected
	 */
	public DataLsa(ILsa lsa) {

		super("DataLsa");
		OperationManager opMng = NodeManager.instance().getOperationManager();
		Operation op = new Operation().injectOperation((Lsa) lsa, agentName,
				this);
		opMng.queueOperation(op);
	}

	@Override
	public void onNotification(AbstractSapereEvent event) {
		// TODO Auto-generated method stub
		
	}

}
