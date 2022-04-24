package com.coderfromscratch.simplehttpserver.core;

import com.coderfromscratch.simplehttpserver.http.Operation;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static com.coderfromscratch.simplehttpserver.http.Operation.*;

public class HttpConnectionWorkerThread extends Thread{
    private Socket socket;

    private static String tblContents = "";


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
        Top+= "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n" +
                "<link href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css\" rel=\"stylesheet\">\n\n";
        Top+= "</head>\n";
        Top+= "<body>\n";

        return Top;

    }


    public static String body() throws FileNotFoundException {
        String body = "<div class=\"container mt-5\">";
        body += "<div class=\"col col-lg-auto\">";

        body += table();

        body += formBody();

        return body;

    }

    public static String table() throws FileNotFoundException {
        String tbl = "";
        tblContents = "";
        int i = 1;
        Scanner dbScanner =new Scanner(new File("src/main/resources/friendsdb.txt")).useDelimiter("\n");
        Scanner lineScanner;

        tbl += "\t<div class=\"col col-lg-auto\">\n" +
                "\t<table class=\"table table-striped table-hover\">\n" +
                "\t\t<thead>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<th scope=\"col\">#</th>\n" +
                "\t\t\t\t<th scope=\"col\">Name</th>\n" +
                "\t\t\t\t<th scope=\"col\">Cellphone Number</th>\n" +
                "\t\t\t\t<th scope=\"col\" colspan=\"2\" class=\"text-center\">Actions</th>\n" +
                "\t\t\t</tr>\n" +
                "\t\t</thead>\n" +
                "\t\t<tbody>";

        while (dbScanner.hasNext()){
            String s =dbScanner.next();
            tbl += "\t\t\t<tr>\n";
            lineScanner = new Scanner(s).useDelimiter(",");
            String n = lineScanner.next();
            String n2 = lineScanner.next();
            tbl += "\t\t\t\t<th scope=\"row\">" + i + "</th>\n";
            tbl += "\t\t\t\t<td>" + n + "</td>\n";
            tbl += "\t\t\t\t<td>" + n2 + "</td>\n";

            tbl += "\t\t\t\t<td>" +
                    "<form method=\"get\" action=\"\">" +
                    "<input type=\"hidden\" value=\""+n+"\" name=\"deleteName\">" +
                    "<button type=\"submit\" class=\"btn btn btn-dark float-right mr-2\" name=\"delete\" value=\"true\">Delete <small><i class=\"fas fa-trash ml-2\"></i></small> </button>" +
                    "</form>";
            tbl +=
                    "<form method=\"get\" action=\"\">" +
                    "<input type=\"hidden\" value=\""+n+"\" name=\"deleteName\">" +
                    "<button type=\"submit\" class=\"btn btn btn-dark float-right mr-2\" name=\"update\" value=\"true\">Update <small><i class=\"fas fa-pen ml-2\"></i></small> </button>" +
                    "</form>" +
                    "</td>\n";
            tbl += "\t\t\t</tr>\n";
            tblContents += s + "\n";
            i++;
        }

        tbl += "\t\t</tbody>\n" +
                "\t</table>\n" +
                "\t</div>\n" +
                "\n" +
                "\t<br/>";

        return tbl;
    }

    public static String formBody()
    {
        String form = "";

        form += "\t<div class=\"card-deck\">\n" +
                "\t\t<div class=\"card border-dark mb-3\" >\n" +
                "  \t\t\n" +
                "\t\t  \t\t<div class=\"card-body text-dark\">\n" +
                "\t\t  \t\t\t<h5 class=\"card-title\">Add Entry</h5>\n" +
                "\t\t\t\t\t<form method=\"get\" action=\"\">\n" +
                "\t\t\t\t\t\t<div class=\"row g-3 mb-2 align-items-center\">\n" +
                "\t\t\t\t\t\t  <div class=\"col-4\">\n" +
                "\t\t\t\t\t\t    <label for=\"new_name\" class=\"col-form-label\">Name: </label>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t  <div class=\"col-8\">\n" +
                "\t\t\t\t\t\t    <input type=\"text\" id=\"new_name\" class=\"form-control\" name=\"name\" required>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t<div class=\"row g-3 align-items-center\">\n" +
                "\t\t\t\t\t\t  <div class=\"col-4\">\n" +
                "\t\t\t\t\t\t    <label for=\"cellnum\" class=\"col-form-label\">Cellphone Number: </label>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\n" +
                "\t\t\t\t\t\t  <div class=\"col-8\">\n" +
                "\t\t\t\t\t\t    <input type=\"tel\" id=\"cellnum\" class=\"form-control\" maxlength=\"10\" minlength=\"10\" name=\"cellnum\" required>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark float-right mt-1\" name=\"add\" value=\"true\">Add Person <small><i class=\"fas fa-user-plus ml-2\"></i></small> </button>\n" +
                "\t\t\t\t\t</form>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</div>\n" +
                "\n" +
                "\t</div>";

        form += "\t<div class=\"card-deck\">\n" +
                "\t\t<div class=\"card border-dark mb-3\">\n" +
                "  \t\t\n" +
                "\t\t <div class=\"card-body text-dark\">\n" +
                "\t\t  \t<h5 class=\"card-title\">Search</h5>\n" +
                "\t\t\t\t<form method=\"get\" action=\"\">\n" +
                "\t\t\t\t\t<div class=\"row g-3 mb-2 align-items-center\">\n" +
                "\t\t\t\t\t\t<div class=\"input-group mb-3 ml-4 mr-4\">\n" +
                "  \t\t\t\t\t\t\t<input type=\"text\" class=\"form-control\" placeholder=\"Recipient's username\" name=\"searchterm\" aria-label=\"Recipient's username\" aria-describedby=\"button-addon2\" required>\n" +
                "  \t\t\t\t\t\t\t<div class=\"input-group-append\">\n" +
                "    \t\t\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark\" id=\"button-addon2\" name=\"search\" value=\"true\">Search <small><i class=\"fas fa-search ml-2\"></i></small> </button>\n" +
                "  \t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t</form>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</div>\n" +
                "\t</div>";


        return form;
    }

    public static void insertToDB(String name,String num) throws IOException {
        if (!tblContents.contains(name)){
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/friendsdb.txt", true));
            bw.write(name + "," + num + "\n");
            bw.close();
        }
    }

    public static void removeFromDB(String name) throws IOException {
        if (tblContents.contains(name)){
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/friendsdb.txt"));

            Scanner dbScanner =new Scanner(tblContents).useDelimiter("\n");
            while (dbScanner.hasNext()){
                String line = dbScanner.next();
                if (line.contains(name)){
                    //
                }
                else {
                    bw.write(line + "\n");
                }

            }
            bw.close();
        }
    }

    public static void searchDB(String searchterm){
        if (!tblContents.contains(searchterm)) {

        }
        else {
            
        }
    }


    public static String HtmlBot()
    {
        String bt = "";

        bt += "<script type='text/javascript'>\n" +
                "\n" +
                "\n" +
                "(function()\n" +
                "{\n" +
                "  if( window.localStorage )\n" +
                "  {\n" +
                "    if( !localStorage.getItem('firstLoad') )\n" +
                "    {\n" +
                "      localStorage['firstLoad'] = true;\n" +
                "      location.replace(\"\");\n" +
                "    }  \n" +
                "    else\n" +
                "      localStorage.removeItem('firstLoad');\n" +
                "  }\n" +
                "})();\n" +
                "\n" +
                "</script>";

        bt += "</body>\n</html>\n";
        return bt;
    }



    public static String writeResponse() throws FileNotFoundException {
        String html = HtmlTop("332 Practical 4") + body() + HtmlBot();

        String response =
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

            if (!parseRequest.next().equals("GET")){
                String response =
                        "HTTP/1.1 501 NOT_IMPLEMENTED" + CRLF + //Status Line   :   HTTP/VERSION RESPONSE_CODE RESPONSE_MESSAGE
                                "Content-Length: " + (HtmlTop("332 Practical 4") + body() + HtmlBot()).getBytes().length + CRLF + //HEADER
                                CRLF +
                                (HtmlTop("332 Practical 4") + body() + HtmlBot()) +
                                CRLF + CRLF;
                os.write(response.getBytes());
            }

            String requestType = parseRequest.next();

            if (requestType.equals("/favicon.ico")){
                //DO Nothing
            }
            else {
                Scanner scanReq = new Scanner(requestType).useDelimiter("&");
                if (requestType.contains("add=true")){
                        String name = scanReq.next();
                        String num = scanReq.next();

                        name = name.substring(7, name.length());
                        name = name.replaceAll("[\\+]", " ");

                        num = num.substring(8, 11) + " " + num.substring(11, 14) + " " + num.substring(14, 18);
                        insertToDB(name, num);
                }
                else if (requestType.contains("update=true")){
                    System.out.println("\nupdate=true");
                }
                else if (requestType.contains("delete=true")){
                    String name = scanReq.next();
                    name = name.substring(13, name.length());
                    name = name.replaceAll("[\\+]", " ");
                    removeFromDB(name);
                }
                else if (requestType.contains("search=true")){
                    String searchTerm = scanReq.next();
                    System.out.println(searchTerm);
                    searchDB(searchTerm);
                }

                System.out.println("");

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
