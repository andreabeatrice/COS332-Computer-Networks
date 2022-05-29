public class telnetClient {

	public telnetClient (String address, int port){
		Socket clientsocket = new Socket(address, port);

	}

	public static void main(String [] args){
		DataInputStream input = new DataInputStream(System.in);

		String line = input.readLine();


        if (line.contains("telnet")){
        	telnetClient client = new telnetClient("10.0.0.2", 23);

        }

	}
}