package com.coderfromscratch.simplehttpserver.core;

import com.coderfromscratch.simplehttpserver.http.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.coderfromscratch.simplehttpserver.http.Operation.*;

public class HttpConnectionWorkerThread extends Thread{
    private Socket socket;

    private static String value1 = "0";
    private static String value2 = "0";
    private static Operation nextOp = NUL;
    public static String result = "0";
    public static String prev = "";

    private static boolean display = false;

    static final String CRLF = "\n\r"; //13 10

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


    public static String calculator() {
        String body = "";
        //body = body + "	<table>  \t \t<tr colspan=\"3\"> <input style=\"text-align:right\" type=\"text\" id=\"display\"  value=\"" +  result +"\" maxlength=\"10\"> </tr>";
        /* body = body + "\t \t <tr>";
                body = body + "\t \t\t<td style=\"width:20px\"><a href=\"1\">1</a></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><a href=\"2\">2</a></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><a href=\"3\">3</a></td>";
                body = body + "\t \t\t<td style=\"width:20px\"><a href=\"ADD\">&#43;</a></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><a href=\"4\">4</a></td>";
                body = body + "\t \t\t<td><a href=\"5\">5</a></td>";
                body = body + "\t \t\t<td><a href=\"6\">6</a></td>";
                body = body + "\t \t\t<td><a href=\"SUB\">&#45;</a></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t<tr>";
                body = body + "\t \t\t<td><a href=\"7\">7</a></td>";
                body = body + "\t \t\t<td><a href=\"8\">8</a></td>";
                body = body + "\t \t\t<td><a href=\"9\">9</a></td>";
                body = body + "\t \t\t<td><a href=\"MUL\">&#215;</a></td>";
            body = body + "\t\t </tr>";
            body = body + "\t\t <tr>";
                body = body + "\t\t\t <td><a href=\"0\">0</a></td>";
                body = body + "\t \t\t<td><a href=\"DIV\">&#247;</a></td>";
                body = body + "\t \t\t<td><a href=\"RES\">&#61;</a></td>";
                body = body + "\t \t\t<td><a href=\"CLR\">CLR</a></td>";
            body = body + "\t \t</tr>";
            body = body + "\t \t</tr>";
        body = body + "\t </table>";*/

        body += "<form action=\"\" method=\"GET\">";
        body += "\t <ul>";
        body += "\t \t <li>";
        body += "\t \t \t<label for=\"name\">Name:</label>";
        body += "\t \t \t <input type=\"text\" id=\"name\" name=\"name\">";
        body += "\t \t </li>";
        body += "\t \t <li>";
        body += "\t \t \t<label for=\"number\">Cellphone Number:</label>";
        body += "\t \t \t <input type=\"text\" id=\"number\" name=\"number\">";
        body += "\t \t </li>";
        body += "\t </ul>";
        body += "</form>";
        return body;

    }

    public static String HtmlBot()
    {
        return "</body>\n</html>\n";
    }

    public static String calculation()
    {
            return "<p>" + value1 + " " + nextOp + " " + value2 + " = " + result + "</p>";
    }

    public static void doOperation(Operation o, String v1, String v2){
        double _v1 = Integer.parseInt(v1);
        double _v2 = Integer.parseInt(v2);

        switch (o) {
            case ADD:
                _v1 += _v2;
                break;
            case SUB:
                _v1 -= _v2;
                break;
            case MUL:
                _v1 *= _v2;
                break;
            case DIV:
                _v1 /= _v2;
                break;
        }

        result = String.valueOf(_v1);

    }



    public static String writeResponse(){
        String html = HtmlTop("332 Practical 3") + calculator() + calculation() + HtmlBot();

        String response =
                "HTTP/1.1 200 OK" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                        "Content-Length: " + html.getBytes().length + CRLF;

       //response += "Refresh: " + "2.5" + CRLF;

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

            if (!parseRequest.next().equals("GET")){
                String response =
                        "HTTP/1.1 501 NOT_IMPLEMENTED" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                                "Content-Length: " + (HtmlTop("332 Practical 4") + calculator() + HtmlBot()).getBytes().length + CRLF + //HEADER
                                CRLF +
                                (HtmlTop("332 Practical 3") + calculator() + HtmlBot()) +
                                CRLF + CRLF;
                os.write(response.getBytes());
            }

            String requestType = parseRequest.next();

            switch (requestType) {
                case "/favicon.ico":
                    //TODO: ignore
                    break;
                case "/ADD":
                    nextOp = ADD;
                    break;
                case "/SUB":
                    nextOp = SUB;
                    break;
                case "/MUL":
                    nextOp = MUL;
                    break;
                case "/DIV":
                    nextOp = DIV;
                    break;
                case "/RES":
                    if (prev.equals("")){
                        doOperation(nextOp, value1, value2);
                        prev = "RES";
                    }
                    if (prev.equals("RES")){
                        prev = "RESRES";
                    }
                    else {
                        nextOp = NUL;
                        value1 = "0";
                        value2 = "0";
                        result = "0";
                        prev = "";
                    }
                    break;
                case "/CLR":
                    nextOp = NUL;
                    value1 = "0";
                    value2 = "0";
                    result = "0";
                    prev = "";
                    break;
                default:
                    if (value1==null) {
                        value1 = requestType.substring(1);
                    }
                    else if (value1 != null && nextOp.equals(NUL)){
                        value1 = value1 + requestType.substring(1);
                    }
                    else if (!nextOp.equals(NUL) && value2==null){
                        value2 = requestType.substring(1);
                    }
                    else if (!nextOp.equals(NUL) && value2!=null){
                        value2 = value2 + requestType.substring(1);
                    }
            }

           // System.out.println(value1 + " " + nextOp + " " + value2 + " = " + result);



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
