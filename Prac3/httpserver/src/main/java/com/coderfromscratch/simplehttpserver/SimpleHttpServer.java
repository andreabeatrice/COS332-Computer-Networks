package com.coderfromscratch.simplehttpserver;

import com.coderfromscratch.simplehttpserver.config.Configuration;
import com.coderfromscratch.simplehttpserver.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *
 *	Driver class for HttpServer
 *	@author: andrea
 **/

public class SimpleHttpServer{

    public static void main(String [] args){

        /**
         *
         * ****Requirements:*****
         * 		Read configuration files
         * 		Open a socket to Listen at a Port
         * 		Read Request messages
         * 		Open and read files from the filesystem
         * 		Write Response messages
         *
         *
         **/
        System.out.println("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Using port: " + conf.getPort());
        System.out.println("Using WebRoot: " + conf.getWebroot());

        try {
            ServerSocket serverSocket = new ServerSocket(conf.getPort());
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
