import core.ClientListenerThread;
import core.CommandListenerThread;
import core.ServerListenerThread;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class telnetproxy {

    private Socket socket;
    private Socket clientActor;
    private Writer pw;
    private BufferedReader br, bf;

    private ServerSocket serverActor;

    private DataInputStream in;
    private DataOutputStream out;
    private PrintWriter output;

    public telnetproxy (String address, int port){
        try {
            //Server Actor
            serverActor = new ServerSocket(55555);
            System.out.println("Proxy started. Waiting for client...");

            socket = serverActor.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            output = new PrintWriter(socket.getOutputStream(), true); //, StandardCharsets.ISO_8859_1

            String in = JOptionPane.showInputDialog( "Please enter a host address & port:");

            Scanner sc = new Scanner(in);
            String ADDRESS = sc.next();
            int PORT = Integer.parseInt(sc.next()); //towel.blinkenlights.nl 23

            System.out.println(ADDRESS);
            System.out.println(PORT);

            //Client Actor
            clientActor = new Socket(ADDRESS, PORT); //Hard coded to connect to S

            pw = new PrintWriter(clientActor.getOutputStream(), true);

            br = new BufferedReader(new InputStreamReader(clientActor.getInputStream(), StandardCharsets.ISO_8859_1)); //

            ServerListenerThread slt = new ServerListenerThread(socket, clientActor, serverActor);
            slt.start();

            ClientListenerThread clt = new ClientListenerThread(socket, clientActor, serverActor);
            clt.start();



        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String [] args) throws IOException {

        telnetproxy client = new telnetproxy("10.0.0.2", 23);


    }
}