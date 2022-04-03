package com.coderfromscratch.simplehttpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

        /*  int _byte;

            while ((_byte = is.read()) >= 0){
                System.out.print((char) _byte);
            }*/

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>Page was served using simple Java HTTP server</h1></body></html>";

            final String CRLF = "\n\r"; //13 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + //HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            os.write(response.getBytes());


        }catch (IOException e){
            System.out.println("Problem with communication ");
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
