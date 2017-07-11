package eu.sapere.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides the System Configuration singleton for Android devices. The
 * configuration file is locates in res/raw/settings
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public final class SystemConfiguration extends AbstractSystemConfiguration {

	private SystemConfiguration() {
		properties = new Properties();
		try {
			 InputStream fis = (InputStream) new FileInputStream("res/settings.txt");
			//InputStream fis = this.getClass().getClassLoader().getResourceAsStream("res/settings.txt");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.err.println("file error " + e.toString());
		} catch (IOException e) {
			System.err.println("error " + e.toString());
		}
	}

	/**
	 * Retrieves current system configuration.
	 * 
	 * @return The configuration.
	 */
	public final static SystemConfiguration getInstance() {
		if (instance == null)
			instance = new SystemConfiguration();
		return (SystemConfiguration) instance;
	}

}