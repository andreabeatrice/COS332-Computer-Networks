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

    public static String HtmlTop(String Title)
    {
        String Top = new String();
        Top = "<html>\n";
        Top+= "<head>\n";
        Top+= "<title>\n";
        Top+= Title;
        Top+= "\n";
        Top+= "</title>\n";
        Top+= "</head>\n";
        Top+= "<body>\n";

        return Top;

    }

    public static String calculator()
    {
        String body = "\t<table> ";
        body = body + "\t \t<tr colspan=\"3\"> <input type=\"number\" id=\"display\" name=\"display\"  maxlength=\"10\"> </tr>";
            body = body + "\t \t <tr>";
                body = body + "\t \t\t<td><a href=\"./VALUE=1\">1</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=2\">2</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=3\">3</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=A\">&#43;</a></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><a href=\"./VALUE=4\">4</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=5\">5</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=6\">6</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=S\">&#45;</a></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><a href=\"./VALUE=7\">7</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=8\">8</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=9\">9</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=M\">&#10005;</a></td>";
            body = body + "\t\t </tr>";
            body = body + "\t\t <tr>";
                body = body + "\t\t\t <td><a href=\"./VALUE=0\">0</a></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=D\">&#247</a></td>";
                body = body + "\t \t\t<td></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t</tr>";
        body = body + "\t </table>";


        return body;

    }

    public static String HtmlBot()
    {
        return "</body>\n</html>\n";
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

            String html = HtmlTop("332 Practical 3") + calculator() + HtmlBot();

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
