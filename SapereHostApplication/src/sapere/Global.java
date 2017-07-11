package sapere;

public class Global {
	public static final String CONFIG_FILE = "sdcard/sapere/config.txt";
	public static final int ARRAY_ASCII_START = 65;
	public static final int ARRAY_ASCII_END = 91;

	public static String[] stringMessages() {
		String[] result = new String[ARRAY_ASCII_END - ARRAY_ASCII_START];
		for (int i = 0; i < ARRAY_ASCII_END - ARRAY_ASCII_START; i++) {
			int ch = (ARRAY_ASCII_START + i);
			result[i] = Character.toString((char) ch);
		}
		return result;
	}
}
