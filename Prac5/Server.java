import java.io.*;
import java.util.*;
import java.net.*;

public class Server{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataInputStream 	in = null;
	private PrintWriter output = null;
            BufferedReader input = null;

	//constructor with port #
	public Server (int port){
		try{

			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for client...");

			socket = server.accept();
			System.out.println("Client accepted");

			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			output = new PrintWriter(socket.getOutputStream(), true);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		/*	output.println("Connected" );
			output.write(13);
			output.write(10);
			output.println("******Server Commands*****");
			output.write(13);
			output.write(10);
			output.println("DISPLAY - Print database");
			
			output.write(13);
			output.write(10);
			output.println("SEARCH (<friend name>) - Searches the database for a record with the given parameter");
			output.write(13);
			output.write(10);
			output.println("INSERT (<friend name>, <friend cell number>) - Adds a record to the database");
			output.write(13);
			output.write(10);
			output.println("DELETE (<friend name> || <friend cell number>) - Deletes a record from the database using the provided parameter");
			output.write(13);
			output.write(10);
			output.println("QUIT - Ends interaction with the database.");
			output.write(13);
			output.write(10);
			
			output.println("UPDATE - Changes a record in the database.");
			output.write(13);
			output.write(10);
			output.write(9);
			output.println("UPDATE -n (<original name>, <new name>)");
			output.write(13);
			output.write(10);
			output.write(9);
			output.println("UPDATE -t (<original name>, <new number>)");
			output.write(13);
			output.write(10);
			
			output.write(27);
			output.println("[0G ");
		*/

			String inputLine;

			while (!(inputLine = reader.readLine()).equals("QUIT")) {
                output.println(inputLine + "/test");
                System.out.println(inputLine);


            }

			


			System.out.println("Close connection");

			socket.close();
			reader.close();

		}
		catch (IOException i){
			System.out.println(i);
		}
	}


	public void insert(String en){
		String result = "";

		try {
      		FileWriter myWriter = new FileWriter("database.txt", true);

      		String entry = en.substring(8, en.length()-1) + "\n";

     		myWriter.write(entry);
      		myWriter.close();
      		//result = ;
      		
      		output.write(13);
			output.write(10);
			output.println("Successfully wrote to the file.");

	    } catch (IOException e) {
	      //result = "An error occurred.";
	    }

		output.write(13);
		output.write(10);
		output.write(27);
		output.println("[0G ");
	}

	public void display(){
		output.println("FRIEND, TELEPHONE NUMBER");


		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNext()){
				output.write(13);
				output.write(10);
				output.println(input.next());
      			//result += nl + input.next();
      		}

      		input.close();

	    } catch (IOException e) {
	      //result = "An error occurred.";
	    }

		//return result;
		output.write(13);
		output.write(10);
		output.write(27);
		output.println("[0G ");
	}


	public void search(String en){
		output.println("SEARCH RESULTS: ");

		String searchterm = en.substring(8, en.length()-1);


		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNextLine()){
      			String line = input.nextLine();
      			if (line.contains(searchterm)){
      				output.write(13);
					output.write(10);
					output.println(line);
      			}
      			
      		}

      		input.close();

	    } catch (IOException e) {
	      //result = "An error occurred.";
	    }


	    output.write(13);
		output.write(10);
		output.write(27);
		output.println("[0G ");
	}

	public void delete(String en){

		String searchterm = en.substring(8, en.length()-1);
		String result = "";
		boolean del = false;
		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNextLine()){
      			String line = input.nextLine();
      			if (!line.contains(searchterm)){
      				result += line +"\n";
      			}
      			else {
      				del = true;
      			}
      			
      		}

      		input.close();

      		FileWriter myWriter = new FileWriter("database.txt");

     		myWriter.write(result);
      		myWriter.close();

      		if (del) {
      			output.write(13);
				output.write(10);
      			output.println("Record Successfully Deleted");
      		}
      		else {
      			output.write(13);
				output.write(10);
      			output.println("Nothing to delete.");
      		}

      		
      		output.write(13);
			output.write(10);
			output.write(27);
			output.println("[0G ");

	    } catch (IOException e) {

	      //return "An error occurred.";
	    }

	}
	
	public String clearConsole() {   
		return "\033[H\033[2J";
	}


	public void update(String en){

		String searchterm = en.substring(11, en.length()-1);
		String updatetype = en.substring(8,9);
		boolean change = false;


		Scanner enTerm = new Scanner(en).useDelimiter(",");
		String entry = enTerm.next(); //<original name>
		String newVal = enTerm.next();

		newVal = newVal.substring(1, newVal.length()-1);
		entry = entry.substring(11, entry.length());

		String result = "";
		
		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNextLine()){
      			String line = input.nextLine();
      			if (!line.contains(entry)){
      				result += line +"\n";
      			}
      			else {
      				change = true;
      				Scanner lin = new Scanner(line).useDelimiter(",");
      				if (updatetype.equals("n")) {
      					lin.next();
      					result += newVal + "," + lin.next()+"\n";
      				}
      				else {
      					result += lin.next() + "," + newVal+"\n";
      				}
      			}
      			
      		}

      		input.close();

      		FileWriter myWriter = new FileWriter("database.txt");

     		myWriter.write(result);
      		myWriter.close();

      		if (change) {
      			output.write(13);
				output.write(10);
      			output.println("Record Successfully Changed");
      		}
      		else {
      			output.write(13);
				output.write(10);
      			output.println("Nothing to change.");
      		}

	    } catch (IOException e) {
	      //return "An error occurred.";
	    }

	}

	public static void main(String [] args){
		Server server = new Server(389);
	}
}
