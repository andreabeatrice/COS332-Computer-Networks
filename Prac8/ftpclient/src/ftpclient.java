import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 */
public class ftpclient {
    private Socket socket = null;
    public static DataInputStream input = null;
    public static DataOutputStream out = null;

    public static BufferedReader br = null;

    public static BufferedReader bf = null;

    public static PrintWriter pw = null;
    public static String ADDRESS = "";

    private static boolean DEBUG = false;
    public static String USER = null;
    public static String PASSWORD = null;

    public ftpclient (String address, int port, boolean passive){
        try {
            socket = new Socket(address, port);

             pw = new PrintWriter(socket.getOutputStream(),true);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            bf = new BufferedReader(new InputStreamReader(System.in));



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

                            //Getting the last modified time
                            long lastModified = file.lastModified();
                            Date date = new Date(lastModified);

                            if (date.after(dateComp)){
                                System.out.println("index.html was updated at: " + date);
                                //System.out.println();//pw.println(userCmd);
                                dateComp = new Date(lastModified);

                                System.out.println(stor(new FileInputStream(file), file.getName()));
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


    public boolean stor(InputStream inputStream, String filename) throws IOException {

        BufferedInputStream input = new BufferedInputStream(inputStream);

        sendLine("PASV");
        String response = readLine();
        if (!response.startsWith("227 ")) {
            throw new IOException("SimpleFTP could not request passive mode: "
                    + response);
        }

        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                        + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256
                        + Integer.parseInt(tokenizer.nextToken());
            } catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: "
                        + response);
            }
        }

        pw.println("STOR " + filename);

        Socket dataSocket = new Socket(ip, port);

        response = br.readLine();

        System.out.println(response);

        BufferedOutputStream output = new BufferedOutputStream(dataSocket
                .getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();

        response = readLine();
        return response.startsWith("226 ");
    }

    private void sendLine(String line) throws IOException {
        if (socket == null) {
            throw new IOException("SimpleFTP is not connected.");
        }
        pw.write(line + "\r\n");
        pw.flush();
        if (DEBUG) {
            System.out.println("> " + line);
        }
    }

    private String readLine() throws IOException {
        String line = br.readLine();
        if (DEBUG) {
            System.out.println("< " + line);
        }
        return line;
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
