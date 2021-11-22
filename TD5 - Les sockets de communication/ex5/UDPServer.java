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
			
			// Paquet reçu
			DatagramPacket dataPacket = new DatagramPacket(data, data.length);
			
			while (true) {
				// Attente d'un paquet
				System.out.println("Attente de réception du paquet.");
				//long startTime = System.currentTimeMillis();			// Démarrer le compteur 
				
				// Données du paquet
				InetAddress addressPacket = dataPacket.getAddress();
				int portPacket = dataPacket.getPort();

				// Paquet réponse								
				dataSocket.receive(dataPacket);							// Envoie d'un paquet
				dataPacket = new DatagramPacket(dataPacket.getData(), dataPacket.getLength(), dataPacket.getSocketAddress());
				dataSocket.send(dataPacket);

				// Affichage du paquet reçu
				String receivedMessage = new String(dataPacket.getData(), 0, dataPacket.getLength());
				System.out.println("Reception d'un message\n  >>>  Contenue du message [" + receivedMessage + "]");
				System.out.println("  >>>  Depuis la machine : " + addressPacket.getCanonicalHostName() + " via le port "
						+ portPacket);
				
				
				// Affichage du ping calculé
				//long elapsedTime = (new Date()).getTime() - startTime;
				//System.out.println("  >>>  Ping : " + elapsedTime + " ms\n\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}