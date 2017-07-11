package sapere;

import java.util.Scanner;
import eu.sapere.util.ConfigReader;
import utils.BulbActuator;

public class SapereStarter {

	/*
	 * initialization
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("SAPERE is starting...");
		BulbActuator bulbActuator = new BulbActuator();
		ConfigReader confReader = new ConfigReader();
		String agentName = confReader.getNodeName();

		// AgentChemotaxisManager agentRead = new
		// AgentChemotaxisManager(agentName);
		AgentRead agentRead = new AgentRead(agentName);
		agentRead.setInitialLSA();

		System.out.println("Destination:");
		Scanner scan = new Scanner(System.in);

		while (scan.hasNext()) {
			String strInput = scan.nextLine();
			if (strInput.equals("0")) {
				bulbActuator.sendingPostRequest("0");
			} else {
				// AgentInjectorGradient agentWrite = new
				// AgentInjectorGradient(str_input);
				AgentWrite agentWrite = new AgentWrite("prop1", "Light" + agentName + "is off", "dest");
				agentWrite.setInitialLSA();
			}
		}
		scan.close();
		// nmnodeManager.stopServices();
	}

}
