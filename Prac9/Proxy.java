import java.io.*;
import java.util.*;
import java.net.*;

public class Proxy{

	private ServerSocket serverActor = null;
	private Socket clientActor = null;
	private Socket clientAcceptor = null;


	private PrintWriter proxyOutputWriter = null;  //output

    private BufferedReader wClientInputReader = null; //input //bf

    private PrintWriter writeToServer = null; //pw

    private BufferedReader tServerResponse = null; //br

	public Proxy(int port){

		try{

			//Connect to windows client
			serverActor = new ServerSocket(port);
			System.out.println("Proxy started. Waiting for connection...");


			clientAcceptor = serverActor.accept();
			System.out.println("Client connected.");

			proxyOutputWriter = new PrintWriter(clientAcceptor.getOutputStream(), true);
			
			BufferedReader wClientInputReader = new BufferedReader(new InputStreamReader(clientAcceptor.getInputStream()));


			//Connect to telnet server

			clientActor = new Socket("10.0.0.2", 23);

			writeToServer = new PrintWriter(clientActor.getOutputStream(),true);

			tServerResponse = new BufferedReader(new InputStreamReader(clientActor.getInputStream()));

			String msg = tServerResponse.readLine();

			//System.out.println(msg);
			proxyOutputWriter.println(msg);


		}
		catch (IOException i){
			System.out.println(i);
		}

	}


	public static void main(String [] args){

		Proxy prxy = new Proxy(55555);
	}
}