package eu.sapere.middleware.node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import eu.sapere.middleware.agent.RemoteConnectionManager;
import eu.sapere.middleware.node.lsaspace.OperationManager;
import eu.sapere.middleware.node.lsaspace.Space;
import eu.sapere.middleware.node.lsaspace.SpaceRunner;
import eu.sapere.middleware.node.lsaspace.console.AbstractLsaSpaceConsole;
import eu.sapere.middleware.node.lsaspace.console.LsaSpaceConsole;
import eu.sapere.middleware.node.networking.topology.NetworkTopologyManager;
import eu.sapere.middleware.node.networking.transmission.delivery.NetworkDeliveryManager;
import eu.sapere.middleware.node.networking.transmission.receiver.NetworkReceiverManager;
import eu.sapere.middleware.node.notifier.Notifier;
import eu.sapere.middleware.node.timemachine.TimeMachineManager;
import eu.sapere.util.ConfigReader;
import eu.sapere.util.ISystemConfiguration;
import eu.sapere.util.SystemConfigurationFactory;

/**
 * Initializes the Local Sapere Node
 * 
 * @author Gabriella Castelli (UNIMORE)
 */
public final class NodeManager {

	/** The Network Topology Manager */
	private NetworkTopologyManager networkTopologyManager = null;

	/** The Network Delivery Manager */
	private NetworkDeliveryManager networkDeliveryManager = null;

	/** The local LSA space */
	private Space space = null;

	/** The local Notifier */
	private Notifier notifier = null;

	/** The Console of the LSA space */
	private static LsaSpaceConsole console = null;

	/** The name of the local Node */
	private static String spaceName = null;

	/** The SpaceRunner of the Node */
	private SpaceRunner runner = null;

	/**
	 * This is a reference to the one and only instance of this singleton
	 * object.
	 */
	static private NodeManager singleton = null;

	private static long opTime = 5000;
	private static long sleepTime = 100;
	private static boolean hasConsole = false;
	private static boolean hasInspector = false;

	private static ISystemConfiguration systemConfiguration = null;

	/**
	 * This is the only way to access the singleton instance.
	 * 
	 * @return A reference to the singleton.
	 */
	// Provides well-known access point to singleton NodeManager
	static public NodeManager instance() {

		if (singleton == null) {

			systemConfiguration = SystemConfigurationFactory.getSystemConfiguration();

			if (systemConfiguration != null)
				if (systemConfiguration.getProperties() != null) {
					opTime = new Long(systemConfiguration.getProperties().getProperty(ISystemConfiguration.OP_TIME));
					sleepTime = new Long(
							systemConfiguration.getProperties().getProperty(ISystemConfiguration.SLEEP_TIME));
					// spaceName = systemConfiguration.getProperties()
					// .getProperty(ISystemConfiguration.NODE_NAME);
					ConfigReader confReader = new ConfigReader();
					spaceName = confReader.getNodeName();
					hasConsole = Boolean.parseBoolean(
							systemConfiguration.getProperties().getProperty(ISystemConfiguration.CONSOLE));
					hasInspector = Boolean.parseBoolean(
							systemConfiguration.getProperties().getProperty(ISystemConfiguration.INSPECTOR));
				} else {
					// Set missing default values
					spaceName = "node" + Math.random();
				}

			singleton = new NodeManager();

			setupLibraries();

			// Setup the TimeMachine
			new TimeMachineManager();

			runApplicationAgents();
		}
		return singleton;
	}

	/**
	 * This is the only way to access the singleton instance.
	 * 
	 * @param lsc
	 *            The LsaSpaceConsole, if already initialised.
	 * @return A reference to the singleton.
	 */
	static public NodeManager instance(LsaSpaceConsole lsc) {
		console = lsc;
		return instance();
	}

	/**
	 * Starts the Node Manager
	 */
	// Prevents direct instantiation of the event service
	private NodeManager() {

		// Set the Notifier
		notifier = new Notifier();

		if (!ISystemConfiguration.isAndroid) {
			if (hasConsole) {
				String consoleImplementation = systemConfiguration.getProperties()
						.getProperty(ISystemConfiguration.CONSOLE_IMPLEMENTATION);

				if (!consoleImplementation.isEmpty() && consoleImplementation != null) {
					AbstractLsaSpaceConsole abstractconsole = null;
					consoleImplementation = consoleImplementation.trim();
					ClassLoader loader = systemConfiguration.getClassLoader();
					try {
						Class<?> myClass = loader.loadClass(consoleImplementation);
						Constructor<?> constructor = null;
						try {
							constructor = myClass.getConstructor(String.class, boolean.class);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
						console = (AbstractLsaSpaceConsole) constructor.newInstance(spaceName, hasInspector);
						abstractconsole = (AbstractLsaSpaceConsole) console;

					} catch (Exception e) {
						e.printStackTrace();
					}

					if (abstractconsole != null)
						abstractconsole.startConsole();

				}

			}
		} else { // ANDROID
		}

		// Start the LSA Space
		space = new Space(spaceName, notifier, console);

		// Start the Network in and out
		networkDeliveryManager = new NetworkDeliveryManager();
		new NetworkReceiverManager();

		runner = new SpaceRunner(this, opTime, sleepTime);

		// Start the Network Topology Manager
		networkTopologyManager = new NetworkTopologyManager(this);

		// Start the Remote Connection Manager
		new RemoteConnectionManager();

		new Thread(runner).start();

	}

	private static void runApplicationAgents() {
		if (systemConfiguration.getProperties() != null) {
			String agents = systemConfiguration.getProperties().getProperty(ISystemConfiguration.AGENTS);

			if (agents != null) {
				// String classNameP[] =
				// systemConfiguration.getProperties().getProperty(ISystemConfiguration.AGENTS)
				// .split(";");

				// ClassLoader loader = systemConfiguration.getClassLoader();

				try {

					/*
					 * for (String c : classNameP) {
					 * 
					 * String className[] = c.split(",");
					 * 
					 * if (className.length != 2) break;
					 * 
					 * Class<?> myClass = loader
					 * .loadClass(className[0].trim());
					 * 
					 * Constructor<?> constructor = null;
					 * 
					 * try { constructor = myClass.getConstructor(String.class);
					 * } catch (NoSuchMethodException e) { e.printStackTrace();
					 * } catch (SecurityException e) { e.printStackTrace(); }
					 * 
					 * SapereAgent myObject = (SapereAgent) constructor
					 * .newInstance(className[1].trim());
					 * myObject.setInitialLSA(); }
					 */

					// ConfigReader confReader = new ConfigReader();
					// String agent = confReader.getNodeName();

					// Class<?> myClass;
					// Constructor<?> constructor = null;

					// myClass = loader.loadClass("sapere.AgentManager");

					// try {
					// constructor = myClass.getConstructor(String.class);
					// } catch (NoSuchMethodException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// } catch (SecurityException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }

					// start the chemotaxis m
					// SapereAgent aChemo = (SapereAgent) constructor
					// .newInstance(confReader.getNodeName());
					// aChemo.setInitialLSA();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void setupLibraries() {

		if (systemConfiguration.getProperties() != null) {
			String libraries = null;
			libraries = systemConfiguration.getProperties().getProperty(ISystemConfiguration.LIBRARY);

			if (libraries != null && !libraries.isEmpty()) {
				String library[] = systemConfiguration.getProperties().getProperty(ISystemConfiguration.LIBRARY)
						.split(";");

				ClassLoader loader = systemConfiguration.getClassLoader();

				try {

					for (String l : library) {

						Class<?> myClass = loader.loadClass(l.trim());

						Constructor<?> constructor = null;

						try {
							constructor = myClass.getConstructor();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						constructor.newInstance();

					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Retrieves the local Operation Manager
	 * 
	 * @return the local Operation Manager
	 */
	public OperationManager getOperationManager() {
		return runner.getOperationManager();
	}

	/**
	 * Retireves the local Notifier
	 * 
	 * @return the local Notifier
	 */
	public Notifier getNotifier() {
		return notifier;
	}

	/**
	 * Retrieves a reference to the network delivery manager.
	 * 
	 * @return the reference to the network delivery manager
	 */
	public NetworkDeliveryManager getNetworkDeliveryManager() {
		return networkDeliveryManager;
	}

	/**
	 * Returns the network topology manager.
	 * 
	 * @return The network topology manager.
	 */
	public NetworkTopologyManager getNetworkTopologyManager() {
		return networkTopologyManager;
	}

	/**
	 * Returns the name of this Node
	 * 
	 * @return The Name of this Node
	 */
	public static String getSpaceName() {
		return spaceName;
	}

	/**
	 * Returns the space associated with this host.
	 * 
	 * @return The Name of this Node
	 */
	public Space getSpace() {
		return space;
	}

	/**
	 * Stops the local Sapere Node.
	 */
	public void stopServices() {
		networkTopologyManager.stop();
		runner.stop();

	}

	/**
	 * Returns the System Configuration.
	 * 
	 * @return The System Configuration.
	 */
	public static ISystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}
}
