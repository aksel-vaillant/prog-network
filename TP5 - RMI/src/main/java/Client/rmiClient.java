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

public class rmiClient {

	public static void main(String[] args) throws IOException, NotBoundException, XMLStreamException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException {
		// Locate the Registry
		Registry reg = LocateRegistry.getRegistry(args[0],Integer.parseInt(args[1]));

		// Link to the Registry
		XmlOperationsI xmlODI = (XmlOperationsI) reg.lookup("xmlOP");

		// Create a Scanner object
		Scanner scan = new Scanner(System.in);

		//System.out.println("OD=" + xmlODI);
		//System.out.println(xmlODI.addUser("kais","mdp"));

		while(true){
			System.out.println("Enter commands");
			String cmds = scan.nextLine();  				// Read commands input
			String[] result = cmds.split(" ");		// Split datas

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
				System.out.println("Arret du client");
				System.exit(-1);
			}
			else{
				System.out.println("Commande inconnu");
			}
		}
	}
}