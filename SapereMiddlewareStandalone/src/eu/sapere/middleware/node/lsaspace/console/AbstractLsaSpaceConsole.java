package eu.sapere.middleware.node.lsaspace.console;

/**
 * Abstract class for the LsaSpaceConsole
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class AbstractLsaSpaceConsole implements LsaSpaceConsole {

	protected String nodeName;
	protected boolean inspector = false;

	/**
	 * Creates a new SpaceMonitor
	 * 
	 * @param nodeName
	 *            The name of the local Node
	 * @param hasInspector
	 *            True is the Console should display the Visual Inspector
	 */
	public AbstractLsaSpaceConsole(String nodeName, boolean hasInspector) {
		this.nodeName = nodeName;
		this.inspector = hasInspector;
	}

	/**
	 * Launches the Console
	 */
	public abstract void startConsole();

}
