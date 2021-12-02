package multi;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket socket;
    private InetSocketAddress inetSockAddress;

    private int port;
    private String address;

    private Scanner in;

    public UDPClient(String address, int port) throws SocketException {
        setPort(port);
        setAddress(address);

        socket = new DatagramSocket();
        inetSockAddress = new InetSocketAddress(address, port);

        socket.setSoTimeout(20);

        in = new Scanner(System.in);
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public static void main(String[] args) throws IOException {
        UDPClient client = new UDPClient("localhost", 8796);
        boolean running = true;

        while(running == true){
            System.out.println("Entrez un nombre");
            long num = client.in.nextLong();

            if(num == 0){
                System.out.println("Arrêt du client.");
                running = false;
            }

            // Conversion entre long et bytes
            byte[] buf = ByteUtils.longToBytes(num);

            // Envoie des données au serveur
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(client.getAddress()), client.getPort());
            client.socket.send(packet);
        }
    }
}


