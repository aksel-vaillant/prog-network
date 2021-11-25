import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class FTPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    //private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR\\";
    private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR\\";

    public void startConnection(String name, int port) throws IOException {
        clientSocket = new Socket(name, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    private void saveFile() throws IOException {
        System.out.println("Veuillez entrer un fichier à enrengistrer.");
        String nameFile = readCommand();

        // Envoie du nom du fichier au server pour l'enrengistrer
        out.println(nameFile);

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
        System.out.println("Nom du fichier à envoyer au server.");
        String nameFile = readCommand();
        System.out.println("Envoie du fichier " + nameFile + " au client.");
        out.println(nameFile);

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

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String readCommand() {
        Scanner scanCmd = new Scanner(System.in);
        return scanCmd.nextLine();
    }

    public static void main(String args[]) throws IOException {
        FTPClient client = new FTPClient();
        client.startConnection("localhost", 6666);

        while(true){
            System.out.println("Attente d'une requête à effectuer");

            client.out.flush();
            String cmd = client.readCommand();

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
                    client.stopConnection();
                    System.out.println("Arrêt de la communication avec le server.");
                    System.exit(1);
                }
            }

            client.out.flush();
        }
    }
}
