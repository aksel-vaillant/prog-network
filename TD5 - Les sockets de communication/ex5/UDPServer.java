package ex5;


import java.net.*;
import java.util.Date;

public class UDPServer {
	
	private static final int PORT = 8967;

	public static void main(String[] args) {
		try {
			// Initialisation 
			System.out.println("Initialisation du serveur echo...");
			DatagramSocket dataSocket = new DatagramSocket(PORT);	
			byte[] data = new byte[100];
			
			// Paquet re�u
			DatagramPacket dataPacket = new DatagramPacket(data, data.length);
			
			while (true) {
				// Attente d'un paquet
				System.out.println("Attente de r�ception du paquet.");
				//long startTime = System.currentTimeMillis();			// D�marrer le compteur 
				
				// Donn�es du paquet
				InetAddress addressPacket = dataPacket.getAddress();
				int portPacket = dataPacket.getPort();

				// Paquet r�ponse								
				dataSocket.receive(dataPacket);							// Envoie d'un paquet
				dataPacket = new DatagramPacket(dataPacket.getData(), dataPacket.getLength(), dataPacket.getSocketAddress());
				dataSocket.send(dataPacket);

				// Affichage du paquet re�u
				String receivedMessage = new String(dataPacket.getData(), 0, dataPacket.getLength());
				System.out.println("Reception d'un message\n  >>>  Contenue du message [" + receivedMessage + "]");
				System.out.println("  >>>  Depuis la machine : " + addressPacket.getCanonicalHostName() + " via le port "
						+ portPacket);
				
				
				// Affichage du ping calcul�
				//long elapsedTime = (new Date()).getTime() - startTime;
				//System.out.println("  >>>  Ping : " + elapsedTime + " ms\n\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}