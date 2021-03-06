package Serveur;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlOperations extends UnicastRemoteObject implements XmlOperationsI {
	String pathname = "src//main//resources//clients.xml";
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

	public String addUser(String pseudo, String mdp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		if(!this.userExist(pseudo, mdp)){
			// Création d'un document

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();

			String[] userData = new String[]{pseudo, mdp};
			data.add(userData);

			// Utilisation de DOM : création d'objet
			Element racine = document.createElement("clients");
			for(int i=0; i< data.size();i++){
				Element newClient = document.createElement("client");

				Element newPseudo = document.createElement("pseudo");
				newPseudo.appendChild(document.createTextNode(data.get(i)[0]));

				Element newPassword = document.createElement("mdp");
				newPassword.appendChild(document.createTextNode(data.get(i)[1]));

				newClient.appendChild(newPseudo);
				newClient.appendChild(newPassword);

				racine.appendChild(newClient);
			}
			document.appendChild(racine);

			// Création du fichier XML
			// Transformer les objets DOM en fichier XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// DOMSource est le document que l'on vient de créer en DOM
			DOMSource domSource = new DOMSource(document);
			// StreamResult est le document que l'on souhaite avoir en XML
			StreamResult streamResult = new StreamResult(new File(pathname));

			// Paramétrages de la transformation avec l'indentation, l'encodage, etc
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");							// Auto indent
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");						// Encode with UTF-8
			transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "8");	// Indent amount

			// Transformation du document
			transformer.transform(domSource, streamResult);

			return "Utilisateur ajouté";
		}
		return "Utilisateur existe déjà";
	}
	public String removeUser(String pseudo) throws IOException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException, TransformerException {
		if(this.pseudoExist(pseudo)){
			// Suppression de l'user
			if(data.size() > 1){
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i)[0].equals(pseudo)) {
						data.remove(data.get(i));
					}
				}
				String[] userData = data.get(data.size() - 1);
				data.remove(data.size() - 1);
				this.addUser(userData[0], userData[1]);
			}else{
				// S'il n'y a plus d'user, on supprime le document
				File xmlFile = new File(pathname);
				xmlFile.delete();
			}
			return "Utilisateur " + pseudo + " supprimé";
		}
		return  "Utilisateur existe pas";

	}

}