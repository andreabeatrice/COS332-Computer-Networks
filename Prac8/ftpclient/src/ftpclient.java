import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 */
public class ftpclient {
    private Socket socket = null;
    public static DataInputStream input = null;
    public static DataOutputStream out = null;
    public static String ADDRESS = "";
    public static String USER = null;
    public static String PASSWORD = null;

    public ftpclient (String address, int port, boolean passive){
        try {
            socket = new Socket(address, port);

            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));



            boolean connectionOpen = true;
            boolean fileWatch = false;

            while (connectionOpen) {
                try {
                    String msg=br.readLine();

                    if (USER != null && PASSWORD != null){
                        if (passive){
                            pw.println("PASV");
                            passive =false;
                        }
                    }

                    if(msg!=null){
                        if (msg.equals("220 (vsFTPd 3.0.3)")){
                            System.out.println(msg);
                            pw.println("OPTS UTF8 ON");
                        }
                        else if (msg.equals("200 Always in UTF8 mode.")){
                            System.out.println(msg);
                            System.out.print("User (10.0.0.2:21): ");

                            USER = bf.readLine();
                            pw.println("USER " + USER);
                        }
                        else if (msg.equals("331 Please specify the password.")){
                            System.out.println(msg);
                            System.out.print("Password: ");

                            PASSWORD = bf.readLine();
                            pw.println("PASS " + PASSWORD);

                        }
                        else if (msg.equals("221 Goodbye.")){
                            System.out.println(msg);
                            connectionOpen = false;
                        }
                        else {
                            System.out.println(msg);
                            System.out.print("ftp> ");

                            String userCmd = bf.readLine();

                            switch (userCmd) {
                                case "bye":
                                    pw.println("QUIT");
                                    break;
                                case "quit":
                                    pw.println("QUIT");
                                    break;
                                case "close":
                                    pw.println("QUIT");
                                    break;
                                case "put":
                                    pw.println("put index.html");
                                    break;
                                case "watch":
                                    fileWatch = true;
                                    break;
                                default:
                                    pw.println(userCmd);
                            }

                        }

                        String filePath = "src/index.html";
                        //Creating the File object
                        File file = new File(filePath);

                        //Getting the last modified time
                        long lastModifiedOriginal = file.lastModified();
                        Date dateComp = new Date(lastModifiedOriginal);

                        while (fileWatch){
                            //System.out.println(msg);
                            //System.out.print("ftp> ");

                            //Getting the last modified time
                            long lastModified = file.lastModified();
                            Date date = new Date(lastModified);

                            if (date.after(dateComp)){
                                System.out.println("Given file was last modified at: ");
                                System.out.println(date);//pw.println(userCmd);
                            }

                        }
                    }

                }
                catch(Exception e){

                }
            }


        }
        catch (UnknownHostException u ){
            System.out.println(u);
        }
        catch (IOException i){
            System.out.println(i);
        }
    }
    public static void main(String [] args){
        input = new DataInputStream(System.in);

        String line = "";
        boolean pass = false;

        while (!line.equals("QUIT")) {

            try {
                line = input.readLine();


                if (line.contains("ftp")){
                    if (line.contains("passive")) {
                        pass = true;
                        ADDRESS = line.substring(14, line.length());
                    }
                    else {
                        ADDRESS = line.substring(4, line.length());
                    }


                    ftpclient client = new ftpclient(ADDRESS, 21, pass);

                }

            }
            catch (IOException i){
                System.out.println(i);
            }
        }



    }
}
