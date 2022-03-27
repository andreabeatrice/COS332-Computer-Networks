import java.net.*;
import java.io.*;

public class Client{


	private Socket			socket = null;
	private DataInputStream	input = null;
	private DataOutputStream 	out = null;


	//constructor with port #
	public Client (String address, int port){
		try{

			socket = new Socket(address, port);
			System.out.println("Connected");
			System.out.println("******Server Commands***** \n\nDISPLAY - Print database");
			
			System.out.println("SEARCH (<friend name> || <friend cell number>) - Searches the database for a record with the given parameter");
			System.out.println("INSERT (<friend name>, <friend cell number>) - Adds a record to the database");
			System.out.println("DELETE (<friend name> || <friend cell number>) - Deletes a record from the database using the provided parameter to find the record");
			System.out.println("QUIT - Ends interaction with the database");
			System.out.println("UPDATE - Changes a record in the database. Enter ? to see options \n");
			//System.out.println("UPDATE (<original number>, <new number>) - Changes a record in the database");

			input = new DataInputStream(System.in);

			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException u ){
			System.out.println(u);
		}
		catch (IOException i){
			System.out.println(i);
		}

		String line = "";

		while (!line.equals("QUIT")) {

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
		Client client = new Client("10.0.0.2", 5000);
	}
}