package Serveur;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmiServeur {

	public static void main(String[] args) {
		try {
			// Start the Java RMI registry
			Registry reg = LocateRegistry.createRegistry(Integer.valueOf(args[0]));

			// Create an object
			XmlOperations xmlOD = new XmlOperations();

			// Send the object to the registry
			reg.bind("xmlOP", xmlOD);

			System.out.println("C'est bon " + xmlOD);
		} catch (Exception e) {
			System.out.println("ERREUR");
			e.printStackTrace();
		}
	}

}