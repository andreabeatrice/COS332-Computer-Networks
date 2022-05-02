import java.io.*;
import java.util.*;
import java.net.*;

public class Server{

	private static int PORT = 389;
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

			while (!binaryToText((inputLine = reader.readLine())).equals("quit")) {
				inputLine = binaryToText(inputLine);

				if (inputLine.contains("ldapmodify") && inputLine.contains("-a")) { //The ldapmodify tool enables you to act on attributes. 
																				//Denotes that entries are to be added, and that the input file is in LDIF format.
					//ldapmodify [arguments] -f [filename]
					Scanner scanargs = new Scanner(inputLine).useDelimiter("\"");
					scanargs.next();
					String args = scanargs.next();
					String fName = scanargs.next();

					output.println(ldapadd(args, fName));
				}
				else if (inputLine.contains("ldapsearch")){  //The ldapsearch command-line tool enables you to search for and retrieve specific entries in the directory. 
					output.println(ldapsearch(inputLine));
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

      		System.out.println("New Entry:\n dn: " + cn + "," + ou + "," + o + "\n");

      	} catch (IOException e) {
	    	returnString = "An error occurred.";
	    }

		return convertStringToBinary("added to database\n");
	}

	public String ldapsearch(String args){
		//	ldapsearch -b "" -s base -v "objectclass=*"
		//	-b specifies base DN for the search, root in this case.
		//	-s specifies whether the search is a base search (base), one level search (one) or subtree search (sub).
		//	"objectclass=*" specifies the filter for search

		Scanner scanArgs = new Scanner(args).useDelimiter("-");
		scanArgs.next();
		
		String b = scanArgs.next();
		b = b.substring(3, b.length()-2);
		String s = scanArgs.next();
		s = s.substring(2, s.length()-1);
		String v = scanArgs.next();
		v = v.substring(6, v.length()-2);

		System.out.println("searched for: " + v);

		try {
			File myObj = new File("addressbook.ldif");
		     
		    Scanner myReader = new Scanner(myObj).useDelimiter("dn:");
		      
		    while (myReader.hasNext()) {
				String data = "dn:" + myReader.next();
				
				String[] parts = data.split("\n");

				if (parts[0].contains(b) && parts[0].contains(v)) {
					System.out.println("found: " + parts[9]);
					return convertStringToBinary(parts[9]);
				}

				
			}

		} catch (IOException e) {
	    	return convertStringToBinary("An error occurred.");
	    }

		return convertStringToBinary("not found");

	}

	public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }

    public static String binaryToText(String binary) {
    	return Arrays.stream(binary.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
                 .parallel()
                 .map(eightBits -> (char)Integer.parseInt(eightBits, 2))
                 .collect(
                                 StringBuilder::new,
                                 StringBuilder::append,
                                 StringBuilder::append
                 ).toString();
    }
	

	public static void main(String [] args){
		Server server = new Server(PORT);
	}

	
}
