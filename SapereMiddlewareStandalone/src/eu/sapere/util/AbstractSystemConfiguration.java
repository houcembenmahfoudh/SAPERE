package eu.sapere.util;

import java.util.Properties;

import eu.sapere.middleware.node.NodeManager;

/**
 * Abstract class for the System Configuration. Provides an implementation for
 * common methods.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class AbstractSystemConfiguration implements
		ISystemConfiguration {

	// String GLOBAL_PROP_FILE_NAME = "./conf/settings.properties";

	protected static Properties properties = null;

	protected static AbstractSystemConfiguration instance;

	@Override
	public Properties getProperties() {
		return properties;
	}

	public boolean isAndroid() {
		return System.getProperty("java.vendor.url").contains("android");
	}

	public ClassLoader getClassLoader() {
		ClassLoader ret = null;
		if (isAndroid()) {
			ret = NodeManager.class.getClassLoader();
			Thread.currentThread().setContextClassLoader(ret);
		} else
			ret = ClassLoader.getSystemClassLoader();
		return ret;
	}

}
