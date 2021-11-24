import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FTPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    //private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR";
    private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\CLIENT_DIR\\";

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

    private void saveFile(File file) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4096];

        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        System.out.println("Start");
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        System.out.println("Soon end");
        fos.flush();
        dis.close();
        System.out.println("End");
    }

    public void sendFile(FileInputStream file) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        FileInputStream fis = file;
        byte[] buffer = new byte[4096];

        int read;
        while ((read=fis.read(buffer)) > 0) {
            dos.write(buffer,0,read);
        }

        fis.close();
        dos.flush();
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
            System.out.println("Waiting a command...");
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
                    String file = client.readCommand();
                    client.out.println(file);
                    client.saveFile(new File(client.DEFAULT_DIRECTION_FOLDER + file));
                    System.out.println("Done getting file.");
                    break;
                }
                case "PUT":{
                    client.out.println("PUT_FILE");
                    client.out.flush();
                    String file =  client.readCommand();
                    client.out.println(file);
                    client.out.flush();
                    client.sendFile(new FileInputStream(client.DEFAULT_DIRECTION_FOLDER + file));
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
