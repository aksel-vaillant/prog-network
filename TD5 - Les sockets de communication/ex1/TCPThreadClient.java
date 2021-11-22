package ex1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPThreadClient extends Thread{
	
	private Socket client;
	private String name;
	
	public TCPThreadClient(Socket socket, String name) {
		this.client = socket;
		this.name = name;
	}
	
	public void run() {
		try {		
			PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
