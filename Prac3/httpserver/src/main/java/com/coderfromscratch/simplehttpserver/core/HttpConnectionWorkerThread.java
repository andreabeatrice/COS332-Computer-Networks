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

        String body = "<form action=\"\" method=\"\" name=\"calcForm\">";
        body += "\t<table> ";
        body = body + "\t \t<tr colspan=\"3\"> <input type=\"number\" id=\"display\" name=\"display\" value=\"0\" maxlength=\"10\"> </tr>";
            body = body + "\t \t <tr>";
                body = body + "\t \t\t<td style=\"width:20px\"><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"1\">1</button></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"2\">2</button></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"3\">3</button></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"A\">&#43;</button></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"4\">4</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"5\">5</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"6\">6</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"S\">&#45;</button></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"7\">7</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"8\">8</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"9\">9</button></td>";
                body = body + "\t \t\t<td><a href=\"./VALUE=M\">&#10005;</a></td>";
            body = body + "\t\t </tr>";
            body = body + "\t\t <tr>";
                body = body + "\t\t\t <td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"0\">0</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"D\">&#247;</button></td>";
                body = body + "\t \t\t<td><button type=\"submit\" name=\"value\" for=\"calcForm\" value=\"E\">&#247;</button></td>";
                body = body + "\t \t\t<td></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t</tr>";
        body = body + "\t </table></form>";


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
