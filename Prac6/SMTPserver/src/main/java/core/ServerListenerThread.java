package core;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread{
    private int port;
    private String webroot;
    private ServerSocket serverSocket;


    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                SMTPConnectionWorkerThread workerThread = new SMTPConnectionWorkerThread(socket);
                workerThread.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with setting socket " + e);
        } finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}

