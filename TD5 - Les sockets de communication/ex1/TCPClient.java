package ex1;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPClient {
	public static final int PORT = 1252;

	public static void main(String[] arguments) {
		try {
			System.out.println("Je vais essayer de me connecter...");
			Socket service = new Socket("localhost", PORT);
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(service.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(service.getInputStream()));
			
			Scanner scanner = new Scanner(System.in);

			while(true) {
				// Ecriture d'un message
				System.out.print("Enter text: ");	    
		        String message = scanner.nextLine();
		        
		        // Envoie du message
		        out.println(message);
		        out.flush();
		        
		        // Si on envoie STOP => on arrête la communication avec le serveur
		        if ("STOP".equalsIgnoreCase(message)) {
					System.out.println("Arrêt de la communication serveur");
		            break;
		        }
		        
		        // Affichage de la réponse du serveur
		        String response = in.readLine();
		        System.out.println(response);
			}
			
			out.close();
			in.close();
			service.close();
		} catch (Exception e) {
			System.err.println("Erreur sérieuse : " + e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}