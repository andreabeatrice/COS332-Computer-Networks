package core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerListenerThread extends Thread {

    private Socket clientSocket, clientActor;
    private PrintWriter output;
    private ServerSocket serverActor;

    private BufferedReader br;

    public ServerListenerThread(Socket clientSocket, Socket clientActor, ServerSocket serverActor) throws IOException {
        this.clientSocket = clientSocket;
        this.clientActor = clientActor;
        this.serverActor = serverActor;

        output = new PrintWriter(clientSocket.getOutputStream(), false,StandardCharsets.ISO_8859_1); //,
        br = new BufferedReader(new InputStreamReader(clientActor.getInputStream(),StandardCharsets.ISO_8859_1));
    }

    @Override
    public void run(){
        System.out.println("Server Listener started");
        try {
            while (serverActor.isBound() && !serverActor.isClosed()) {
                int red = -1;
                byte[] buffer = new byte[5*1024]; // a read buffer of 5KiB
                byte[] redData;
                StringBuilder clientData = new StringBuilder();
                String redDataText;
                red = br.read();
                redData = new byte[red];
                System.arraycopy(buffer, 0, redData, 0, red);
                //output.write(redData);

                clientData.append(red);
                output.write(red);
                output.flush();

            }
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with setting socket " + e);
        } finally {
            if (serverActor!=null){
                try {
                    serverActor.close();
                } catch (IOException e) {}
            }
        }
    }
}