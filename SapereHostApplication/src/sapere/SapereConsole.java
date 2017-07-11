package sapere;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.node.lsaspace.console.AbstractLsaSpaceConsole;

public class SapereConsole extends AbstractLsaSpaceConsole {

	public SapereConsole(String nodeName, boolean hasInspector) {
		super(nodeName, hasInspector);
	}

	@Override
	public void update(Lsa[] list) {
		// System.out.println("Updates LSA space");
		// for (Lsa lsa: list)
		// {System.out.println(lsa.getId());}
	}

	@Override
	public void removeLsa(String lsaId) {

	}

	@Override
	public void removeBond(String from, String to) {

	}

	@Override
	public void propagateLsa(String lsaId) {

	}

	@Override
	public void addBidirectionalBond(String from, String to) {

	}

	@Override
	public void addUnidirectionalBond(String from, String to) {

	}

	@Override
	public boolean hasInspector() {
		return false;
	}

	@Override
	public void startConsole() {
	}
}
