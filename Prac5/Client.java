import java.net.*;
import java.io.*;

public class Client{


	private Socket			socket = null;
	private DataInputStream	input = null;
	private DataOutputStream 	out = null;
	private PrintWriter outprint;
	BufferedReader in;
    BufferedReader stdIn;


	//constructor with port #
	public Client (String address, int port){
		try{

			socket = new Socket(address, port);
			System.out.println("Connected");

			input = new DataInputStream(System.in);

			out = new DataOutputStream(socket.getOutputStream());
			outprint =  new PrintWriter(socket.getOutputStream(), true);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			stdIn = new BufferedReader(new InputStreamReader(System.in));

			
			String userInput;
			String line = "";


			while ((userInput = stdIn.readLine()) != null) {
				outprint.println(userInput);
				System.out.println("echo: " + in.readLine());
			}
	
		}
		catch (UnknownHostException u ){
			System.out.println(u);
		}
		catch (IOException i){
			System.out.println(i);
		}
		
		
	}

	public static void main(String [] args){
		Client client = new Client("127.0.0.1", 389);
	}
}