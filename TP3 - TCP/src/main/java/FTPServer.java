/*
*   Programme réalisé par Aksel Vaillant sous Maven/Intelij et jdk 1.8
*
*   Pour faire fonctionner le programme, il faut préalablement remplir le DEFAULT_DIRECTION_FOLDER
*   qui contient les fichiers serveur. Dans le main, il faut ainsi, créer l'objet FTPServer pour
*   commencer à lancer le serveur. Les diverses commandes s'effectuent correctement avec le client
*   cependant, il y a encore des problèmes de socket non résolus qui empêche le bon déroulement
*   du programme suite à des fermetures inattendus.
*
*   Egalement, le choix de ne pas vouloir split la commande en plusieurs sous parties est voulu pour
*   une expérience plus compréhensible.
*
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private InputStream is;
    private OutputStream os;
    private FileInputStream fis;
    private FileOutputStream fos;

    private String dirFolder;
    private final String DEFAULT_DIRECTION_FOLDER = "*à remplir avec un double \\ à la fin*";

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

        is = clientSocket.getInputStream();
        os = clientSocket.getOutputStream();

        setDirFolder(DEFAULT_DIRECTION_FOLDER);
    }

    public void stop() throws IOException {
        System.out.println("Arrêt du serveur et du système.");
        is.close();
        os.close();
        fis.close();
        fos.close();
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

        System.out.println("Enregistrement du fichier " + nameFile + " en cours.");

        // Reception de la taille du fichier
        int sizeFile = Integer.parseInt(in.readLine());
        System.out.println("Taille du fichier : " + (sizeFile+1) + " octets");

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
        String nameFile = in.readLine();
        System.out.println("Envoie du fichier " + nameFile + " au client.");

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

    public static void main(String[] args) throws IOException {
        // Création du serveur FTP avec choix du port
        FTPServer server=new FTPServer();
        server.start(6666);

        System.out.println("Démarrage du serveur");
        while(true){
            // Gestion des 3 commandes et l'arrêt du serveur
            System.out.println("Attente d'une requête client");

            server.out.flush();
            String cmd = server.in.readLine();

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
        }
    }
}
