package ex1;
import java.net.*;
import java.io.*;

public class TCPServer {
	public static final int PORT = 1252;

	public static void main(String[] arguments) {
		try {
			ServerSocket socketAttente = new ServerSocket(PORT);
			Socket client = socketAttente.accept();
			
			// Création de 10 clients
			for(int i=0; i<10; i++) {
				TCPThreadClient ThreadClient = new TCPThreadClient(client, "Client " + i);
			}

			System.out.println("Server prêt\n-----------------------------");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

			while(true) {
				// Connection d'un client
				String message = in.readLine();
				System.out.println("From Client: " + message);
				
		        // Si le client envoie STOP => on arrête la communication
				if("STOP".equalsIgnoreCase(message)) {
					System.out.println("Arrêt de la communication client");
					break;
				}
				
				// Echo le message du client pour l'envoyer
				String echo = "echo " + message;
				out.println(echo);	
				out.flush();
			}
			
			client.close();
			socketAttente.close();
			out.close();
			in.close();
		} catch (Exception e) {
			System.err.println("Erreur : " + e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}