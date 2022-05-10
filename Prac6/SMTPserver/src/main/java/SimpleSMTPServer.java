import config.Configuration;
import config.ConfigurationManager;
import core.ServerListenerThread;

import java.io.IOException;

public class SimpleSMTPServer {
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
