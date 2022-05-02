import java.net.*;
import java.io.*;
import java.util.*;

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

			while ((userInput = stdIn.readLine()) != "quit") {
				outprint.println(convertStringToBinary(userInput));
				System.out.println(binaryToText(in.readLine()) + "\n");
			}
	
		}
		catch (UnknownHostException u ){
			System.out.println(u);
		}
		catch (IOException i){
			System.out.println(i);
		}
		
		
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
		Client client = new Client("127.0.0.1", 389);
	}
}