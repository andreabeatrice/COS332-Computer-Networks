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

    private DataOutputStream tServerOutput = null; //out

	public Proxy(int port){

		try{

			//Connect to windows client
			serverActor = new ServerSocket(port);
			System.out.println("Proxy started. Waiting for connection...");


			clientAcceptor = serverActor.accept();
			System.out.println("Client connected.");

			proxyOutputWriter = new PrintWriter(clientAcceptor.getOutputStream(), true);
			
			BufferedReader wClientInputReader = new BufferedReader(new InputStreamReader(clientAcceptor.getInputStream()));

			try {

				//Connect to telnet server

				clientActor = new Socket("10.0.0.2", 23);

				tServerResponse = new BufferedReader(new InputStreamReader(clientActor.getInputStream()));

				writeToServer = new PrintWriter(clientActor.getOutputStream(),true);

				pw.println("AYT");

			} catch(Exception e){

            }

            System.out.println(tServerResponse.readLine());
            System.out.println("**");

		}
		catch (UnknownHostException u ){
			System.out.println(u);
		}
		catch (IOException i){
			System.out.println(i);
		}

	}


	public static void main(String [] args){

		Proxy prxy = new Proxy(55555);
	}
}