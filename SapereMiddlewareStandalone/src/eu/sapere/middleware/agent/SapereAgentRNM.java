package eu.sapere.middleware.agent;

import eu.sapere.middleware.agent.SapereAgent;
import eu.sapere.middleware.agent.remoteconnection.ProxySapereAgent;
import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueListener;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.node.lsaspace.Operation;

/**
 * The abstract class that realize a SapereAgent that manages an LSA capable of
 * managing a remote LSA, injected in neighbor space.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class SapereAgentRNM extends SapereAgent implements PropertyValueListener {

	private boolean proxyModeOn = false;

	private ProxySapereAgent proxy = null;

	private String ip = null;

	/**
	 * Instantiates the Agent
	 * 
	 * @param name
	 *            The name of the Agents
	 */
	public SapereAgentRNM(String name) {
		super(name);
	}

	@Override
	// This is the entry for the operations to be exectued
	protected void submitOperation() {

		if (lsa.getProperty(PropertyName.ACTIVE_PROPAGATION.toString()) != null) {

			if (this.proxyModeOn == false) {
				this.proxyModeOn = true;

				setIp(lsa.getProperty(PropertyName.ACTIVE_PROPAGATION.toString()).getValue().elementAt(0));

				proxy = new ProxySapereAgent(this.getAgentName() + "Proxy", ip, this);

			}
			Lsa remoteCopy = ((Lsa) lsa).getCopy();
			remoteCopy.removeProperty(PropertyName.ACTIVE_PROPAGATION.toString());
			proxy.setLsa((Lsa) remoteCopy);
			proxy.forward();
			if (lsaId != null)
				removeLsa(); // remove the LSA from the local node
		} else {

			if (this.proxyModeOn == true) {
				proxy.closeConnection();
				this.proxyModeOn = false;
				lsaId = injectOperation(); // inject so synthetic properties are
											// overwritten
			} else {
				if (lsaId == null)
					lsaId = injectOperation();
				else
					updateOperation();
			}
		}
	}

	@Override
	public void removeLsa() {
		Operation op = new Operation().removeOperation(lsaId, getAgentName());
		// this.lsa = null;
		opMng.queueOperation(op);
	}

	private void setIp(String ip) {
		this.ip = ip;
	}

}
