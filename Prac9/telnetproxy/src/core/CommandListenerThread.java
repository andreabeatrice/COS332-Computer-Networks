package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandListenerThread extends Thread{

    BufferedReader reader;
    ServerSocket serverActor;


    public CommandListenerThread(BufferedReader br, ServerSocket serverActor) throws IOException {
        this.reader = br;
        this.serverActor = serverActor;


    }

    @Override
    public void run() {
        System.out.println("Server Listener started");
        String line = "";
        try {
            while (serverActor.isBound() && !serverActor.isClosed()) {
                line = reader.readLine();
                System.out.println(line);

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
