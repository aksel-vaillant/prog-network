import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

public class FTPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String dirFolder;
    private final String DEFAULT_DIRECTION_FOLDER = "H:\\Home\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\SERVEUR_DIR";

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

    public void start(int port, String dirFolder) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        setDirFolder(dirFolder);
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public String readCommand() throws IOException {
        /*String args[] = new String[5];
        String buffer;
        int index = 0;
        while((buffer = scanCmd.nextLine()) != "\n"){
            args[index] = buffer;
            index++;
        }//return args;*/

        String cmd = in.readLine();
        System.out.println(cmd);

        //Scanner scanCmd = new Scanner(System.in);
        //System.out.print("[" + DEFAULT_DIRECTION_FOLDER + "\\$] ");
        //return scanCmd.nextLine();

        return cmd;
    }

    public static void main(String[] args) throws IOException {
        FileInputStream savedFile;

        FTPServer server=new FTPServer();
        server.start(6666);

        System.out.println("Démarrage du serveur");

        while(true){
            // Gestion des 3 commandes et l'arrêt du serveur
            String cmd = server.in.readLine();

            switch (cmd){
                case "GET_FILE":{
                    savedFile = new FileInputStream(server.DEFAULT_DIRECTION_FOLDER + "\\" + server.readCommand());
                    System.out.println("yush");

                    //InputStream input=new s.getInputStream();

                    break;
                }

                case "PUT_FILE":{

                    break;
                }

                case "LS_DIR":{
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
                    System.out.println("Arrêt du serveur et du système.");
                    System.exit(1);
                }
            }

        }

    }
}
