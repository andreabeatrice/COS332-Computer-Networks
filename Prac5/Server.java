import java.io.*;
import java.util.*;
import java.net.*;

public class Server{


	private Socket			socket = null;
	private ServerSocket	server = null;
	private DataInputStream 	in = null;
	private PrintWriter output = null;
    private BufferedReader input = null;
    private boolean welcomeMessage = false;

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

			String inputLine;

			while (!(inputLine = reader.readLine()).equals("quit")) {
				if (inputLine.contains("ldapmodify") && inputLine.contains("-a")) { //The ldapmodify tool enables you to act on attributes. 
																				//Denotes that entries are to be added, and that the input file is in LDIF format.
					//ldapmodify [arguments] -f [filename]
					Scanner scanargs = new Scanner(inputLine).useDelimiter("\"");
					scanargs.next();
					String args = scanargs.next();
					String fName = scanargs.next();

					output.println(ldapadd(args, fName));
				}
				else if (inputLine.contains("ldapcompare")){  //The ldapcompare command-line tool enables you to match attribute values you 
															//specify in the command line with the attribute values in the directory entry.
					output.println(inputLine + "~from Server.java");
                	System.out.println(inputLine);
				}

            }

			System.out.println("Close connection");

			socket.close();
			reader.close();

		}
		catch (IOException i){
			System.out.println(i);
		}
	}

	public String ldapadd(String args, String fName){
		String returnString = "";
		String file = fName.substring(fName.lastIndexOf(" ") + 1);
		System.out.println(args + " " + fName.substring(fName.lastIndexOf(" ") + 1));

		try {
			//cn=Peter Blignaut,ou=Family,o=Address Book,telephonenumber:(082) 411-4548
			Scanner scanEntry = new Scanner(args).useDelimiter(",");
      		FileWriter myWriter = new FileWriter(file, true);

      		String cn = scanEntry.next();
      		String sn = cn.substring(cn.lastIndexOf(" ") + 1);
      		String ou = scanEntry.next();
      		String o = scanEntry.next();
      		String telephonenumber = scanEntry.next();

      		myWriter.write("dn: " + cn + "," + ou + "," + o + "\n");

      		cn = cn.substring(3, cn.length());
      		ou = ou.substring(3, ou.length());
      		telephonenumber = telephonenumber.substring(16, telephonenumber.length());

      		myWriter.write("objectclass: top\n");
      		myWriter.write("objectclass: person\n");
      		myWriter.write("objectClass: organizationalPerson\n");
      		myWriter.write("objectClass: inetOrgPerson\n");
      		myWriter.write("cn: " + cn + "\n");
      		myWriter.write("sn: " + sn + "\n");
      		myWriter.write("ou: " + ou + "\n");
      		myWriter.write("telephonenumber: " + telephonenumber + "\n\n");
      		myWriter.close();

      		System.out.println("dn: " + cn + ou + o + "\n");

      	} catch (IOException e) {
	    	returnString = "An error occurred.";
	    }

		return "added to database\n";
	}

	public String ldapcompare(){

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
