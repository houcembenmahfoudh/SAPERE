package eu.sapere.util;

import java.util.Properties;

/**
 * Provides an interface for the System Configuration.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public interface ISystemConfiguration {
 
	// Property names

	/** The name of the local Sapere node */
	public static String NODE_NAME = "nodeName";

	/** The opTime of the local Sapere node */
	public static String OP_TIME = "opTime";

	/** The sleepTime of the local Sapere node */
	public static String SLEEP_TIME = "sleepTime";

	/** The console of the local Sapere node */
	public static String CONSOLE = "console";

	/** The inspector of the local Sapere node */
	public static String INSPECTOR = "inspector";

	/** The overlay of the local Sapere node */
	public static String OVERLAY = "overlay";

	/** The overlay-mode of the local Sapere node */
	public static String OVERLAY_MODE = "overlay-mode";

	/** The network-port of the local Sapere node */
	public static String NETWORK_PORT = "network-port";

	/** Libraries for the local Sapere node */
	public static String LIBRARY = "library";

	/** Libraries for the local Sapere node */
	public static String CONSOLE_IMPLEMENTATION = "consoleImplementation";

	/** Application Agents to be launched */
	public static String AGENTS = "agents";

	/** The System Properties */
	public static Properties properties = SystemConfigurationFactory
			.getSystemConfiguration().getProperties();

	/** Indicates if running on Android */
	public boolean isAndroid = SystemConfigurationFactory
			.getSystemConfiguration().isAndroid();

	/**
	 * Returns the System Properties.
	 * 
	 * @return The System Properties.
	 */
	public Properties getProperties();

	/**
	 * Returns true is the node is running on an Android device.
	 * 
	 * @return true is the node is running on an Android device, false
	 *         otherwise.
	 */
	public boolean isAndroid();

	/**
	 * Returns the class loader.
	 * 
	 * @return The class loader.
	 */
	public ClassLoader getClassLoader();
}
