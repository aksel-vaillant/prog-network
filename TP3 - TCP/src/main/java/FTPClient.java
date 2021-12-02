import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FTPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private InputStream is;
    private OutputStream os;
    private FileInputStream fis;
    private FileOutputStream fos;

    private final String DEFAULT_DIRECTION_FOLDER = "*à remplir avec un double \\ à la fin*";

    public void startConnection(String name, int port) throws IOException {
        clientSocket = new Socket(name, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        is = clientSocket.getInputStream();
        os = clientSocket.getOutputStream();
    }

    public void stopConnection() throws IOException {
        is.close();
        os.close();
        fis.close();
        fos.close();

        in.close();
        out.close();
        clientSocket.close();
    }

    private void saveFile() throws IOException {
        System.out.println("Veuillez entrer un fichier à enregistrer.");
        String nameFile = readCommand();

        // Envoie du nom du fichier au server pour l'enregistrer
        out.println(nameFile);

        // Création du nom du fichier
        File file = new File(DEFAULT_DIRECTION_FOLDER + nameFile);

        System.out.println("Enregistrement du fichier " + nameFile + " en cours.");

        // Reception de la taille du fichier
        int sizeFile = Integer.parseInt(in.readLine());
        System.out.println("Taille du fichier : " + sizeFile + " octets");

        // Création du buffer à l'aide de la taille du fichier
        byte[] buffer = new byte[sizeFile];

        // Lecture de chaque bit envoyer par le server et le stocker dans le tableau
        is = clientSocket.getInputStream();
        is.read(buffer, 0, buffer.length);

        // Une fois l'ensemble des octets envoyés
        // Écriture de l'ensemble du buffer dans le nouveau fichier
        fos = new FileOutputStream(file);
        fos.write(buffer, 0, buffer.length);

        // Fermeture des flux concernant le fichier
        fos.flush();
        //is.reset();

        System.out.println("Succès de l'enregistrement du fichier.");
    }

    public void sendFile() throws IOException {
        // Nom du fichier à envoyer au client
        System.out.println("Nom du fichier à envoyer au server.");
        String nameFile = readCommand();
        System.out.println("Envoie du fichier " + nameFile + " au client.");
        out.println(nameFile);

        // Ouverture du flux concernant le fichier
        fis = new FileInputStream(DEFAULT_DIRECTION_FOLDER + nameFile);

        // Envoie au client la taille du fichier
        int sizeFile = (int)fis.getChannel().size();
        System.out.println("Taille du fichier " + sizeFile + " octets");
        out.println(sizeFile);

        // Création du buffer à l'aide de la taille du fichier
        byte[] buffer = new byte[sizeFile];

        // Lecture de chaque bit dans le fichier et le stocker dans le tableau
        fis.read(buffer, 0 , buffer.length);

        // Envoi de chaque octet dans le buffer au client
        os = clientSocket.getOutputStream();
        os.write(buffer, 0, buffer.length);

        os.flush();
        //fis.reset();

        System.out.println("Succès de l'envoie du fichier.");
    }

    public String readCommand() {
        Scanner scanCmd = new Scanner(System.in);
        return scanCmd.nextLine();
    }

    public static void main(String args[]) throws IOException {
        // Création du client FTP avec choix du nom et du port
        FTPClient client = new FTPClient();
        client.startConnection("localhost", 6666);

        while(true){
            // Gestion des commandes
            System.out.println("Attente d'une requête à effectuer");

            client.out.flush();
            String cmd = client.readCommand().toUpperCase();

            switch (cmd){
                case "LS":{
                    client.out.println("LS_DIR");
                    System.out.println(client.in.readLine());
                    break;
                }
                case "GET":{
                    client.out.println("GET_FILE");
                    client.saveFile();
                    break;
                }
                case "PUT":{
                    client.out.println("PUT_FILE");
                    client.sendFile();
                    break;
                }
                case "STOP":{
                    client.out.println("STOP");
                    client.stopConnection();
                    System.out.println("Arrêt de la communication avec le server.");
                    System.exit(1);
                }
                case "HELP":{
                    System.out.println("Liste des commandes disponibles...");
                    System.out.println("LS\t\tPermet la redirection de fichier");
                    System.out.println("GET\t\tPermet d'obtenir un fichier serveur");
                    System.out.println("PUT\t\tPermet d'envoyer un fichier au serveur");
                    System.out.println("STOP\t\tPermet l'arrêt de la communication");
                    break;
                }
                default:{
                    System.out.println("Oups... Une erreur est survenue. Consulte l'assistance avec HELP si besoin.");
                }
            }
        }
    }
}
