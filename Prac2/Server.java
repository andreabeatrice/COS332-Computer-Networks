import java.io.*;
import java.util.*;
import java.net.*;

public class Server{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataInputStream 	in = null;
	private PrintWriter output = null;


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


			output.println(" Connected" );
			output.println("\033[2;0H ******Server Commands*****");
			output.println("\033[3;0H DISPLAY - Print database");
			
			output.println("\033[4;0H SEARCH (<friend name>) - Searches the database for a record with the given parameter");
			output.println("\033[5;0H INSERT (<friend name>, <friend cell number>) - Adds a record to the database");
			output.println("\033[6;0H DELETE (<friend name> || <friend cell number>) - Deletes a record from the database using the provided parameter to find the record");
			output.println("\033[7;0H QUIT - Ends interaction with the database.");
			output.println("\033^J UPDATE - Changes a record in the database.");
			output.println("\033[9;0H \033^I UPDATE -n (<original name>, <new name>)");
			output.println("\033[10;0H \033^I UPDATE -t (<original name>, <new number>)");

			String line = "";

			while (!line.equals("QUIT")) {
				try {
					line = reader.readLine();
					//System.out.println(line);
					System.out.println(line);

					if (line.contains("INSERT")) {
						
						output.println(insert(line));
					}
					if (line.contains("DISPLAY")) {
						System.out.println(display());
						output.println(display());
					}
					if (line.contains("SEARCH")) {
						output.println(search(line));
					}
					if (line.contains("DELETE")) {
						output.println(delete(line));
					}
					if (line.contains("UPDATE")) {
						output.println(update(line));
					}
					if (line.contains("CLEAR")){
						output.println(clearConsole());
						output.flush();
					}
				}
				catch (IOException i){
					output.println(i);
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
      		FileWriter myWriter = new FileWriter("database.txt", true);

      		String entry = en.substring(8, en.length()-1) + "\n";

     		myWriter.write(entry);
      		myWriter.close();
      		result = "Successfully wrote to the file.";

	    } catch (IOException e) {
	      result = "An error occurred.";
	    }

		return result;
	}

	public String display(){
		String result = "\nFRIEND, TELEPHONE NUMBER \n";

		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNext()){
      			result += input.next() +"\n";
      		}

      		input.close();

	    } catch (IOException e) {
	      result = "An error occurred.";
	    }

		return result;
	}


	public String search(String en){
		String result = "\nSEARCH RESULTS: \n";
		String searchterm = en.substring(8, en.length()-1);


		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNextLine()){
      			String line = input.nextLine();
      			if (line.contains(searchterm)){
      				result += line +"\n";
      			}
      			
      		}

      		input.close();

	    } catch (IOException e) {
	      result = "An error occurred.";
	    }



		return result;
	}

	public String delete(String en){
		if (search(en).equals("\nSEARCH RESULTS: \n")){
			return "No record to delete.";
		}

		String searchterm = en.substring(8, en.length()-1);
		String result = "";
		try {
      		Scanner input = new Scanner(new File("database.txt"));
      		input.useDelimiter("\n");
      		while (input.hasNextLine()){
      			String line = input.nextLine();
      			if (!line.contains(searchterm)){
      				result += line +"\n";
      			}
      			
      		}

      		input.close();

      		FileWriter myWriter = new FileWriter("database.txt");

     		myWriter.write(result);
      		myWriter.close();

      		return "Record Successfully Deleted \n";

	    } catch (IOException e) {
	      return "An error occurred.";
	    }

	}
	
	public String clearConsole() {   
		
		return "\033[H\033[2J";
	}


	public String update(String en){

		String searchterm = en.substring(11, en.length()-1);
		String updatetype = en.substring(8,9);


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

      		return "Record Successfully Changed \n";

	    } catch (IOException e) {
	      return "An error occurred.";
	    }

	}

	public static void main(String [] args){
		Server server = new Server(5000);
	}
}
