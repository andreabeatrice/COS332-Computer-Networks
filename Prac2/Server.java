import java.io.*;
import java.util.*;
import java.net.*;

public class Server{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataInputStream 	in = null;
	private DataOutputStream output = null;


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
						System.out.println(insert(line));
					}
					if (line.contains("DISPLAY")) {
						System.out.println(display());
					}
					if (line.contains("SEARCH")) {
						System.out.println(search(line));
					}
					if (line.contains("DELETE")) {
						System.out.println(delete(line));
					}
					if (line.contains("UPDATE")) {
						System.out.println(update(line));
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


	public String update(String en){

		String searchterm = en.substring(11, en.length()-1);
		String updatetype = en.substring(8,9);


		Scanner enTerm = new Scanner(en).useDelimiter(",");
		String entry = enTerm.next(); //<original name>
		String newVal = enTerm.next();

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