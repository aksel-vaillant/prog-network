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

    private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR\\";
    //private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR\\";

    public void startConnection(String name, int port) throws IOException {
        clientSocket = new Socket(name, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //out.println("Communication avec le server");
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    private void saveFile() throws IOException {
        String nameFile = readCommand();
        File file = new File(DEFAULT_DIRECTION_FOLDER + nameFile);
        out.println(nameFile);
        System.out.println("Enrengistrement du fichier " + nameFile + " en cours.");

        byte[] buffer = new byte[20000];
        InputStream is = clientSocket.getInputStream();
        FileOutputStream out = new FileOutputStream(file);

        Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

        is.read(buffer, 0, buffer.length);
        w.write(new String(buffer, Charset.defaultCharset()), 0, buffer.length);

        out.close();
        is.close();

        System.out.println("Succès de l'enrengitrement du fichier.");
    }

    public void sendFile() throws IOException {
        String nameFile =  readCommand();
        out.println(nameFile);
        out.flush();

        System.out.println("Envoie du fichier " + nameFile + " au serveur.");

        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        FileInputStream fis = new FileInputStream(DEFAULT_DIRECTION_FOLDER + nameFile);
        byte[] buffer = new byte[4096];

        int read = 1;
        while (read > 0) {
            read=fis.read(buffer);

            System.out.println(read);
            System.out.println(buffer);

            dos.write(buffer,0,read);
        }

        fis.close();
        dos.flush();
        System.out.println("Succès de l'envoie du fichier.");
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String readCommand() throws IOException {
        Scanner scanCmd = new Scanner(System.in);
        System.out.print("[" + DEFAULT_DIRECTION_FOLDER + "\\$] ");
        return scanCmd.nextLine();
    }

    public static void main(String args[]) throws IOException {
        FTPClient client = new FTPClient();
        client.startConnection("localhost", 6666);

        while(true){
            System.out.println("Attente d'une requête à effectuer");
            String cmd = client.readCommand();

            switch (cmd){
                case "LS":{
                    client.out.println("LS_DIR");
                    client.out.flush();
                    System.out.println(client.in.readLine());
                    break;
                }
                case "GET":{
                    client.out.println("GET_FILE");
                    client.out.flush();
                    client.saveFile();
                    break;
                }
                case "PUT":{
                    client.out.println("PUT_FILE");
                    client.out.flush();
                    client.sendFile();
                    break;
                }
                case "STOP":{
                    client.stopConnection();
                    System.out.println("Arrêt de la communication avec le server.");
                    System.exit(1);
                }
            }
        }
    }
}
