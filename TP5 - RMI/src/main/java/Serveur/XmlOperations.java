package Serveur;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import common.XmlOperationsI;
import jdk.internal.util.xml.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

public class XmlOperations extends UnicastRemoteObject implements XmlOperationsI {
	String pathname = "clients.xml";
	File fileClient = new File(pathname);
	ArrayList<String[]> data = new ArrayList<String[]>();

	protected XmlOperations() throws RemoteException {
	}

	public Boolean pseudoExist(String pseudo) throws RemoteException {
		if(data.size()==0) return false;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i)[0].equals(pseudo)) {
				return true;
			}
		}
		return false;
	}

	public Boolean userExist(String pseudo, String mdp) throws RemoteException {
		if(data.size()==0) return false;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i)[0].equals(pseudo) && data.get(i)[1].equals(mdp)) {
				return true;
			}
		}
		return false;
	}

	public String addUser(String pseudo, String mdp) throws IOException, ParserConfigurationException, SAXException {
		if(!this.userExist(pseudo, mdp)){

			File inputFile = new File("clientsTest.xml");
			System.out.println(inputFile.createNewFile());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(inputFile);
			document.getDocumentElement().normalize();
			System.out.println("Root element :" + document.getDocumentElement().getNodeName());

			/*DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(fileClient);*/

			String[] userData = new String[]{pseudo, mdp};
			data.add(userData);

			Element racine = document.createElement("clients");
			for(int i=0; i< data.size();i++){
				Element newClient = document.createElement("client");

				Element newPseudo = document.createElement("pseudo");
				newPseudo.appendChild(document.createTextNode(data.get(i)[0]));

				Element newPassword = document.createElement("mdp");
				newPassword.appendChild(document.createTextNode(data.get(i)[0]));

				newClient.appendChild(newPseudo);
				newClient.appendChild(newPassword);

				racine.appendChild(newClient);
			}
			document.appendChild(racine);
			return "Utilisateur ajouté";
		}
		return "Utilisateur existe déjà";
	}
	public String removeUser(String pseudo) throws IOException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException {
		if(this.pseudoExist(pseudo)){
			// Remove User from data
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i)[0].equals(pseudo)) {
					data.remove(data.get(i));
				}
			}

			//
			if (data.size() != 0) {
				String[] userData = data.get(data.size() - 1);

				data.remove(data.size() - 1);

				this.addUser(userData[0], userData[1]);
				return "Utilisateur " + pseudo + " supprimé";
			}
		}
		return  "Utilisateur existe pas";

	}

}