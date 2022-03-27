import java.net.*;
import java.io.*;

public class Client{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataOutputStream 	out = null;


	//constructor with port #
	public Client (string address, int port){
		try{

			socket = new Socket(address, port);
			System.out.println("Connected");

			input = new DataInputStream(System.in);

			out = new DataOutputStream(socket.getInputStream());
		}
		catch (UnknownHostException u ){
			System.out.println(u);
		}
		catch (IOException i){
			System.out.println(i);
		}

		String line = "";

		while (!line.equals("Over")) {
				try {
					line = input.readLine();
					out.writeUTF(line);
				}
				catch (IOException i){
					System.out.println(i);
				}
			}
	}

	public static void main(String [] args){
		Client client = new Client(10.0.0.2, 5000);
	}
}