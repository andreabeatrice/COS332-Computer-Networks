package core;

import smtp.SMTPCommand;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SMTPConnectionWorkerThread  extends Thread{
    private Socket socket;

    private static String tblContents = "";
    private static String searchTBL = "";

    private static boolean display = false;

    static final String CRLF = "\n\r"; //13 10

    public SMTPConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    public static String writeResponse() throws FileNotFoundException {

        String html = "";
        String response ="";

       // html = HtmlTop("332 Practical 4") + body() + HtmlBot();

        response =
                "HTTP/1.1 200 OK" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                        "Content-Length: " + html.getBytes().length + CRLF;
        response +=     CRLF +
                html +
                CRLF + CRLF;


        return response;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            int _byte;

            os.write(writeResponse().getBytes());

            String request = "";

            while ((_byte = is.read()) >= 0){
                request += ((char) _byte);
            }

            Scanner parseRequest = new Scanner(request);

            if (!SMTPCommand.valueOf(parseRequest.next()).equals()){
                String response =
                        "HTTP/1.1 501 NOT_IMPLEMENTED" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                                "Content-Length: " + ("").getBytes().length + CRLF + //HEADER
                                CRLF +
                                ("") +
                                CRLF + CRLF;
                os.write(response.getBytes());
            }

            String requestType = parseRequest.next();

            if (requestType.equals("/favicon.ico")){
                //DO Nothing
            }
            else {


            }

        }catch (IOException e){
            System.out.println("Problem with communication " + e);
        } finally {
            if (is!=null){
                try{
                    is.close();
                }catch (IOException e){}
            }
            if (os!=null){
                try{
                    os.close();
                }catch (IOException e){}
            }
            if (socket!=null){
                try{
                    socket.close();
                }catch (IOException e){}
            }
        }


    }
}
