import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;

public class FTPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String dirFolder;
    //private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\SERVEUR_DIR\\";
    private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\SERVEUR_DIR\\";

    public String getDirFolder() {
        return dirFolder;
    }
    public void setDirFolder(String dirFolder) {
        this.dirFolder = dirFolder;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        setDirFolder(DEFAULT_DIRECTION_FOLDER);
    }

    public void stop() throws IOException {
        System.out.println("Arrêt du serveur et du système.");
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    private void saveFile() throws IOException {
        System.out.println("Réception d'un fichier en prevenance du serveur.");
        String nameFile = in.readLine();

        // Création du nom du fichier
        File file = new File(DEFAULT_DIRECTION_FOLDER + nameFile);

        System.out.println("Enrengistrement du fichier " + nameFile + " en cours.");

        // Reception de la taille du fichier
        int sizeFile = Integer.parseInt(in.readLine());
        System.out.println("Taille du fichier : " + sizeFile + " byte(s)");

        // Création du buffer à l'aide de la taille du fichier
        byte[] buffer = new byte[sizeFile];

        // Lecture de chaque bit envoyer par le server et le stocker dans le tableau
        InputStream is = clientSocket.getInputStream();
        is.read(buffer, 0, buffer.length);

        // Une fois l'ensemble des bytes envoyés
        // Ecriture de l'ensemble du buffer dans le nouveau fichier
        FileOutputStream outFile = new FileOutputStream(file);
        outFile.write(buffer, 0, buffer.length);

        // Fermeture des fluxs concernant le fichier
        outFile.flush();
        is.close();

        System.out.println("Succès de l'enrengitrement du fichier.");
    }

    public void sendFile() throws IOException {
        // Nom du fichier à envoyer au client
        String nameFile = in.readLine();
        System.out.println("Envoie du fichier " + nameFile + " au client.");

        // Ouverture du flux concernant le fichier
        FileInputStream fis = new FileInputStream(DEFAULT_DIRECTION_FOLDER + nameFile);

        // Envoie au client la taille du fichier
        int sizeFile = (int)fis.getChannel().size();
        System.out.println("Taille du fichier " + sizeFile + " bytes");
        out.println(sizeFile);

        // Création du buffer à l'aide de la taille du fichier
        byte[] buffer = new byte[sizeFile];

        // Lecture de chaque bit dans le fichier et le stocker dans le tableau
        fis.read(buffer, 0 , buffer.length);

        // Envoie de chaque byte dans le buffer au client
        OutputStream outData = clientSocket.getOutputStream();
        outData.write(buffer, 0, buffer.length);

        outData.flush();
        fis.close();

        System.out.println("Succès de l'envoie du fichier.");
        //clientSocket = serverSocket.accept();
    }

    public static void main(String[] args) throws IOException {
        FTPServer server=new FTPServer();
        server.start(6666);

        System.out.println("Démarrage du serveur");
        int i = 0;
        while(true){
            // Gestion des 3 commandes et l'arrêt du serveur
            System.out.println("Attente d'une requête client");

            server.out.flush();
            String cmd = server.in.readLine();
            System.out.println("Commande " + i + "\t" +cmd);

            switch (cmd){
                case "GET_FILE":{
                    server.sendFile();
                    break;
                }

                case "PUT_FILE":{
                    server.saveFile();
                    break;
                }
                case "LS_DIR":{
                    System.out.println("Affichage des fichiers dans la racine server.");
                    String [] pathnames;
                    File readerPath = new File(server.getDirFolder());
                    pathnames = readerPath.list();

                    String files = "";
                    for (String pathname : pathnames) {
                        files += pathname + "\t";
                    }
                    server.out.flush();
                    server.out.println(files);
                    System.out.println(files);
                    break;
                }

                case "STOP":{
                    server.stop();
                    System.exit(1);
                }
            }
            server.out.flush();

            //server.clientSocket = server.serverSocket.accept();
            i++;
        }


    }
}
