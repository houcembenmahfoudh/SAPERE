package eu.sapere.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author houssem
 *
 */
public class ConfigReader {
	private String[][] neighs;
	private String nodeName;
	private String nodeIp;
	private String node = "A";

	 public static final String CONFIG_FILE="/Users/houssem/Desktop/e-SCT SAPERE/sapere/config.txt";
	/**
	 * Config path
	 */
	//public static final String CONFIG_FILE = "/home/hepia/config.txt";

	/**
	 * Network configuration
	 */
	public ConfigReader() {
		try {

			InputStream fis = new FileInputStream(new File(CONFIG_FILE));
			// System.out.println("Config path file" + CONFIG_FILE);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			node = reader.readLine();
			neighs = null;
			if (node.length() > 1)
				node = "" + node.charAt(0);
			reader.close();
			fis.close();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			node = "A";
		}
		lineTopology();
		// meshTopology();
	}

	@SuppressWarnings("unused")
	private void meshTopology() {
		if (node.equals("A")) {
			nodeName = "Rasp1";
			nodeIp = "192.168.1.1";
			neighs = new String[2][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.2";
			neighs[0][1] = "Rasp2";
			neighs[1] = new String[2];
			neighs[1][0] = "192.168.1.3";
			neighs[1][1] = "Rasp3";
		} else if (node.equals("B")) {
			nodeName = "Rasp2";
			nodeIp = "192.168.1.2";
			neighs = new String[2][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.1";
			neighs[0][1] = "Rasp1";
			neighs[1] = new String[2];
			neighs[1][0] = "192.168.1.3";
			neighs[1][1] = "Rasp3";
		} else if (node.equals("C")) {
			nodeName = "Rasp3";
			nodeIp = "192.168.1.3";
			neighs = new String[3][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.1";
			neighs[0][1] = "Rasp1";
			neighs[1] = new String[2];
			neighs[1][0] = "192.168.1.2";
			neighs[1][1] = "Rasp2";
			neighs[2] = new String[2];
			neighs[2][0] = "192.168.1.4";
			neighs[2][1] = "Rasp4";
		} else if (node.equals("D")) {
			nodeName = "Rasp4";
			nodeIp = "192.168.1.4";
			neighs = new String[1][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.3";
			neighs[0][1] = "Rasp3";
		}
	}

	private void lineTopology() {
		if (node.equals("A")) {
			nodeName = "Rasp1";
			nodeIp = "192.168.1.1";
			neighs = new String[1][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.2";
			neighs[0][1] = "Rasp2";
		} else if (node.equals("B")) {
			nodeName = "Rasp2";
			nodeIp = "192.168.1.2";
			neighs = new String[2][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.1";
			neighs[0][1] = "Rasp1";
			neighs[1] = new String[2];
			neighs[1][0] = "192.168.1.3";
			neighs[1][1] = "Rasp3";
		} else if (node.equals("C")) {
			nodeName = "Rasp3";
			nodeIp = "192.168.1.3";
			neighs = new String[2][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.2";
			neighs[0][1] = "Rasp2";
			neighs[1] = new String[2];
			neighs[1][0] = "192.168.1.4";
			neighs[1][1] = "Rasp4";
		} else if (node.equals("D")) {
			nodeName = "Rasp4";
			nodeIp = "192.168.1.4";
			neighs = new String[1][];
			neighs[0] = new String[2];
			neighs[0][0] = "192.168.1.3";
			neighs[0][1] = "Rasp3";
		}
	}

	/**
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return
	 */
	public String getNodeIp() {
		return nodeIp;
	}

	/**
	 * @return
	 */
	public String[][] getNeighs() {
		return neighs;
	}
}
