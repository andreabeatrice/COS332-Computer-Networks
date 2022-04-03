package com.coderfromscratch.simplehttpserver;

import com.coderfromscratch.simplehttpserver.config.Configuration;
import com.coderfromscratch.simplehttpserver.config.ConfigurationManager;
import com.coderfromscratch.simplehttpserver.core.ServerListenerThread;

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
            ServerListenerThread thread1 = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            thread1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
