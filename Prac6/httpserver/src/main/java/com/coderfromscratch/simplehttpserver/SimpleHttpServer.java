package com.coderfromscratch.simplehttpserver;

import com.coderfromscratch.simplehttpserver.config.Configuration;
import com.coderfromscratch.simplehttpserver.config.ConfigurationManager;
import com.coderfromscratch.simplehttpserver.core.ServerListenerThread;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.Logger;


/**
 *
 *	Driver class for HttpServer
 *	@author: andrea
 **/

public class SimpleHttpServer{

    //private static System LoggerFactory;
    //private final static System.Logger LOGGER = LoggerFactory.getLogger(String.valueOf(SimpleHttpServer.class));
    //public final static Logger = new System.Logger();
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

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/smtp.json");
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
