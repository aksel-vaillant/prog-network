package mono;

import java.io.IOException;
import java.net.*;

public class UDPServer extends Thread{
    private DatagramSocket socket;
    private int port;

    long sum = 0;
    byte[] buf = new byte[8];

    public UDPServer(int port) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
    }

    public void run() {
        boolean running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(ByteUtils.bytesToLong(packet.getData()) != 0){
                System.out.print(sum + "+" + ByteUtils.bytesToLong(packet.getData()));
                sum += ByteUtils.bytesToLong(packet.getData());
                System.out.println("=" + sum);
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws SocketException {
        new UDPServer(8796).start();
    }
}