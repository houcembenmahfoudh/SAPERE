package eu.sapere.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The System Configuration Factory, uses reflection.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public class SystemConfigurationFactory {

	/**
	 * Instantiates the System Configuration.
	 * 
	 * @return An object implementing the ISystemConfiguration interface.
	 */
	public static ISystemConfiguration getSystemConfiguration() {

		Class<?> c = null;
		try {
			c = Class.forName("eu.sapere.util.SystemConfiguration");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Method m = null;
		try {
			Class<?>[] cp = null;
			m = c.getDeclaredMethod("getInstance", cp);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		m.setAccessible(true); // if security settings allow this
		Object o = null;
		try {
			Object o1 = null;
			Object o2[] = null;
			o = m.invoke(o1, o2);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return (ISystemConfiguration) o;
	}

}