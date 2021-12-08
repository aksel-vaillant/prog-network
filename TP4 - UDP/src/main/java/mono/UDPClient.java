package mono;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket socket;
    private InetSocketAddress inetSockAddress;

    private int port;
    private String address;

    public UDPClient(String address, int port) throws SocketException {
        setPort(port);
        setAddress(address);

        socket = new DatagramSocket();
        socket.setSoTimeout(20);

        inetSockAddress = new InetSocketAddress(address, port);
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

        Scanner in = new Scanner(System.in);

        while(true){
            System.out.println("Entrez un nombre");
            long num = in.nextLong();

            // Conversion entre long et bytes
            byte[] buf = ByteUtils.longToBytes(num);

            // Envoie des données au serveur
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(client.getAddress()), client.getPort());
            client.socket.send(packet);

            // Si on envoie 0, on se prépare à reçevoir une donnée sur la somme
            if(num == 0){
                client.socket.receive(packet);
                // Conversion inverse entre bytes et long
                long sum = ByteUtils.bytesToLong(packet.getData());
                System.out.println("Somme du client... " + sum);
                System.out.println("Arrêt du client.");
                System.exit(-1);
            }
        }
    }
}


