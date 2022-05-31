package core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientListenerThread extends Thread{

    private Socket clientSocket, clientActor;
    private ServerSocket serverActor;
    private PrintWriter pw;
    private PrintWriter output;
    private BufferedReader bf;

    BufferedReader reader;

    StringBuilder clientData = new StringBuilder();;

    String command = "";


    public ClientListenerThread(Socket clientSocket, Socket clientActor, ServerSocket serverActor) throws IOException {
        this.clientSocket = clientSocket;
        this.clientActor = clientActor;
        this.serverActor = serverActor;

        pw = new PrintWriter(clientActor.getOutputStream(), false,StandardCharsets.ISO_8859_1); //,
        bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.ISO_8859_1));
        output = new PrintWriter(clientSocket.getOutputStream(), true);

        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run(){
        System.out.println("Client Listener started");
        try {
            int prevE = 0;
            while (serverActor.isBound() && !serverActor.isClosed()) {

                int red = -1;

                byte[] buffer = new byte[5*1024]; // a read buffer of 5KiB
                byte[] redData;
                String redDataText;


                red = bf.read();
                redData = new byte[red];
                System.arraycopy(buffer, 0, redData, 0, red);


                String x = String.valueOf(((char) red));

                //if lowercase character, append to client data
                if (red >= 97 && red <= 122 || red == 20){
                    clientData.append((char) red);
                }
                else if (red == 13) {  //if CR character, see what's coming next
                    command = clientData.toString();
                    clientData = new StringBuilder();

                    if (command.startsWith("ps") ){
                        System.out.println("ps command registered");
                        pw.write(127);
                        pw.flush();
                        pw.write(127);
                        pw.flush();
                    }

                    else if (command.equals("exit")){
                        pw.write(red);
                        pw.flush();
                        serverActor.close();
                        clientActor.close();
                        clientSocket.close();
                        System.exit(0);
                    }

                }
                else if (red == 127 && clientData.length() != 0){ //backspace
                    clientData.deleteCharAt(clientData.length() - 1);

                }

                if (command.startsWith("ps")){

                }
                else if (command.contains(" ps")) {

                }
                else {
                    pw.write(red);
                    pw.flush();
                }

                command = "";




            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (serverActor!=null){
                try {
                    serverActor.close();
                } catch (IOException e) {}
            }
        }
    }
}
