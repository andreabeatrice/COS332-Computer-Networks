import java.net.*;
import java.io.*;
import java.util.*;

public class Server{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataInputStream 	in = null;


	//constructor with port #
	public Server (int port){
		try{

			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for client...");

			socket = server.accept();
			System.out.println("Client accepted");

			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			String line = "";

			while (!line.equals("QUIT")) {
				try {
					line = in.readUTF();
					//System.out.println(line);

					if (line.contains("INSERT")) {
						Scanner input = new Scanner(line).useDelimeter("(");
						String entry = input.next();
						System.out.println(insert(entry));
					}
				}
				catch (IOException i){
					System.out.println(i);
				}
			}

			System.out.println("Close connection");

			socket.close();
			in.close();

		}
		catch (IOException i){
			System.out.println(i);
		}
	}


	public String insert(String en){
		String result = "";

		try {
      		FileWriter myWriter = new FileWriter("database.txt");

      		String entry = en + "\n";

     		myWriter.write(entry);
      		myWriter.close();
      		result = "Successfully wrote to the file.";

	    } catch (IOException e) {
	      result = System.out.println("An error occurred.");
	    }

		return result;
	}


	public static void main(String [] args){
		Server server = new Server(5000);
	}
}