package Client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import common.XmlOperationsI;
import jdk.internal.util.xml.XMLStreamException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class rmiClient {

	public static void main(String[] args) throws IOException, NotBoundException, XMLStreamException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException, TransformerException {
		// Localisation du registry
		Registry reg = LocateRegistry.getRegistry(args[0],Integer.parseInt(args[1]));

		// Récupérer l'objet permettant de réaliser les opérations
		XmlOperationsI xmlODI = (XmlOperationsI) reg.lookup("xmlOP");
		System.out.println("OD=" + xmlODI);

		Scanner scan = new Scanner(System.in);

		while(true){
			System.out.println("Enter commands");

			// Récupération des commandes et des informations dans un tableau
			String cmds = scan.nextLine();
			String[] result = cmds.split(" ");

			// Vérification de la commande et de la validité de toutes les informations
			if(result[0].equalsIgnoreCase("ADD") && result.length==3){
				System.out.println(xmlODI.addUser(result[1],result[2]));
			}
			else if(result[0].equalsIgnoreCase("REMOVE") && result.length==2){
				System.out.println(xmlODI.removeUser(result[1]));
			}
			else if(result[0].equalsIgnoreCase("P_EXIST") && result.length==2){
				System.out.println(xmlODI.pseudoExist(result[1]));
			}
			else if(result[0].equalsIgnoreCase("U_EXIST") && result.length==3){
				System.out.println(xmlODI.userExist(result[1], result[2]));
			}
			else if(result[0].equalsIgnoreCase("STOP")){
				System.out.println("Arrêt du client");
				System.exit(-1);
			}
			else if(result[0].equalsIgnoreCase("HELP")){
				System.out.println("ADD nom_client mdp_client\t\tPermet d'ajouter un utilisateur");
				System.out.println("REMOVE nom_client\t\t\t\tPermet de supprimer un utilisateur");
				System.out.println("P_EXIST nom_client\t\t\t\tPermet de tester l'existence d'un pseudo dans la BDD");
				System.out.println("U_EXIST nom_client mdp_client\tPermet de tester l'existence d'un utilisateur dans la BDD");
				System.out.println("STOP\t\t\t\t\t\t\tArrêt du client et de la communication server");
			}
			else{
				System.out.println("Commande inconnu");
			}
		}
	}
}