package com.coderfromscratch.simplehttpserver.core;

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
        ServerSocket serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run(){
        try {

            Socket socket = serverSocket.accept();

            InputStream is =  socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>Page was served using simple Java HTTP server</h1></body></html>";

            final String CRLF = "\n\r"; //13 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + //HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            os.write(response.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
