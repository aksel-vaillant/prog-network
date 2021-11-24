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
    private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\SERVEUR_DIR\\";
    //private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\SERVEUR_DIR\\";

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
        String nameFile = in.readLine();
        File file = new File(DEFAULT_DIRECTION_FOLDER + nameFile);
        System.out.println("Enrengistrement du fichier " + nameFile + " en cours.");

        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4096];

        int filesize = 15123; // Send file size in separate msg
        int read = 1;
        int totalRead = 0;
        int remaining = filesize;
        while(read > 0) {
            System.out.println(read);
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
            read = dis.read(buffer, 0, Math.min(buffer.length, remaining));
        }

        fos.close();
        dis.close();
        clientSocket = serverSocket.accept();
        System.out.println("Succès de l'enrengitrement du fichier.");
    }

    public void sendFile() throws IOException {
        String nameFile = in.readLine();
        System.out.println("Envoie du fichier " + nameFile + " au client.");

        FileInputStream fis = new FileInputStream(DEFAULT_DIRECTION_FOLDER + nameFile);
        byte[] buffer = new byte[20000];
        fis.read(buffer, 0 , buffer.length);

        OutputStream out = clientSocket.getOutputStream();
        out.write(buffer, 0, buffer.length);

        out.close();
        fis.close();

        //clientSocket = serverSocket.accept();
        System.out.println("Succès de l'envoie du fichier.");
    }

    public static void main(String[] args) throws IOException {
        FTPServer server=new FTPServer();
        server.start(6666);

        System.out.println("Démarrage du serveur");

        while(true){
            // Gestion des 3 commandes et l'arrêt du serveur
            System.out.println("Attente d'une requête client");
            String cmd = server.in.readLine();

            switch (cmd){
                case "GET_FILE":{
                    server.sendFile();  break;
                }

                case "PUT_FILE":{
                    server.saveFile();  break;
                }
                case "LS_DIR":{
                    System.out.println("Affichage des fichiers dans la racine server.");
                    String [] pathnames;
                    File readerPath = new File(server.getDirFolder());
                    pathnames = readerPath.list();
                    System.out.print("["+ server.DEFAULT_DIRECTION_FOLDER + "\\$] ");
                    String files = "";
                    for (String pathname : pathnames) {
                        System.out.print(pathname + "\t");
                        files += pathname + " ";
                    }

                    server.out.println(files);
                    server.out.flush();
                    System.out.println();
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
