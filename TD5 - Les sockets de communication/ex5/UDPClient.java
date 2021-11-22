package ex5;
import java.net.*;

public class UDPClient {
	
	public static final int PORT = 8967;
	//public static final String ADDRESS = "localhost";
	
	public static void main(String[] args) {
		try {
			DatagramSocket dataSocket = new DatagramSocket();			
			InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", PORT);
			long startTime, elapsedTime;
			int correctNum = 0, errorNum = 0;
			dataSocket.setSoTimeout(20);
			dataSocket.send();
			
			for(int num = 0; num<10000 ;num++) {
				byte[] data = String.valueOf(num).getBytes();
				DatagramPacket dataPacketSend = new DatagramPacket(data, data.length, inetSocketAddress);
				byte[] dataRecu = new byte[100];
				DatagramPacket dataPacketGet  = new DatagramPacket(data, data.length);


			}

			
			dataSocket.send(dataPacket);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
