import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientListenerThread extends Thread{

    private Socket clientSocket, clientActor;
    private ServerSocket serverActor;
    private PrintWriter pw;
    private DataInputStream in;
    private BufferedReader bf;


    public ClientListenerThread(Socket clientSocket, Socket clientActor, ServerSocket serverActor) throws IOException {
        this.clientSocket = clientSocket;
        this.clientActor = clientActor;
        this.serverActor = serverActor;

        pw = new PrintWriter(clientActor.getOutputStream(), false,StandardCharsets.ISO_8859_1); //,
        bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.ISO_8859_1));

    }

    @Override
    public void run(){
        System.out.println("Client Listener started");
        try {
            while (serverActor.isBound() && !serverActor.isClosed()) {
                int red = -1;
                byte[] buffer = new byte[5*1024]; // a read buffer of 5KiB
                byte[] redData;
                StringBuilder clientData = new StringBuilder();
                String redDataText;
                red = bf.read();
                redData = new byte[red];
                System.arraycopy(buffer, 0, redData, 0, red);
                //output.write(redData);

                redDataText = new String(redData); // assumption that client sends data UTF-8 encoded
                System.out.println("CLT-" + red + " ");
                clientData.append(redDataText);
                pw.println(red);
                pw.flush();
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
