import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FTPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\CLIENT_DIR";

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

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String readCommand(){
        Scanner scanCmd = new Scanner(System.in);
        System.out.print("[" + DEFAULT_DIRECTION_FOLDER + "\\$] ");
        return scanCmd.nextLine();
    }

    public static void main(String args[]) throws IOException {
        FTPClient client = new FTPClient();
        client.startConnection("localhost", 6666);

        while(true){
            String cmd = client.readCommand();

            switch (cmd){
                case "LS":{
                    client.out.println("LS_DIR");
                    client.out.flush();

                    System.out.println(client.in.readLine());

                    break;
                }
                case "GET":{
                    String file = client.readCommand();
                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));


                    break;
                }
                case "PUT_FILE":{

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
