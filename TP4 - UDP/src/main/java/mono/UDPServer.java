package mono;

import java.io.IOException;
import java.net.*;

public class UDPServer extends Thread{
    private DatagramSocket socket;
    private int port;

    public UDPServer(int port) throws SocketException {
        setPort(port);
        socket = new DatagramSocket(port);
    }

    public void setPort(int port){
        this.port = port;
    }

    public void run() {
        long sum = 0;
        byte[] buf = new byte[8];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            System.out.println("Attente de reception d'un paquet.");

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            long newNum = ByteUtils.bytesToLong(packet.getData());
            System.out.println("Du client " + packet.getPort() + "... " + newNum);

            sum += newNum;
            if(newNum == 0){
                buf = ByteUtils.longToBytes(sum);

                InetSocketAddress isa = new InetSocketAddress(packet.getAddress().getHostAddress(), packet.getPort());
                DatagramPacket newPacket = new DatagramPacket(buf, buf.length, isa);
                try {
                    socket.send(newPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Envoie de la somme au client... " + sum);
                System.out.println("Arrêt du serveur");
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        new UDPServer(8796).start();
    }
}