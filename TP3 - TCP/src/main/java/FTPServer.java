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
    //private final String DEFAULT_DIRECTION_FOLDER = "C:\\Users\\aksel\\Documents\\GitHub\\prog-network\\TP3 - TCP\\src\\main\\resources\\SERVEUR_DIR";
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

    private void saveFile(File file) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4096];

        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        fos.flush();
        dis.close();
    }

    public void sendFile(FileInputStream file) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        FileInputStream fis = file;
        byte[] buffer = new byte[4096];

        System.out.println("Start sending");
        int read;
        while ((read=fis.read(buffer)) > 0) {
            dos.write(buffer,0,read);
        }

        fis.close();
        dos.flush();
        System.out.println("End sending");

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
                    String nameFile = server.in.readLine();
                    System.out.println("Le client souhaite posséder le fichier... " + nameFile);
                    FileInputStream savedFile = new FileInputStream(server.DEFAULT_DIRECTION_FOLDER+ nameFile);
                    server.sendFile(savedFile);
                    System.out.println("Succès de l'envoie du fichier " + nameFile);
                    break;
                }

                case "PUT_FILE":{
                    String nameFile = server.in.readLine();
                    File file = new File(server.DEFAULT_DIRECTION_FOLDER + nameFile);
                    server.saveFile(file);
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
