package common;

import jdk.internal.util.xml.XMLStreamException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface XmlOperationsI extends Remote{

	public Boolean pseudoExist(String pseudo) throws RemoteException;
	public Boolean userExist(String pseudo, String mdp) throws RemoteException;
	public String addUser(String pseudo, String mdp) throws IOException, IOException, XMLStreamException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException;
	public String removeUser(String pseudo) throws IOException, XMLStreamException, javax.xml.stream.XMLStreamException, ParserConfigurationException, SAXException;
}
