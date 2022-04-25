package com.coderfromscratch.simplehttpserver.core;

import com.coderfromscratch.simplehttpserver.http.Operation;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

import static com.coderfromscratch.simplehttpserver.http.Operation.*;

public class HttpConnectionWorkerThread extends Thread{
    private Socket socket;

    private static String tblContents = "";
    private static String searchTBL = "";

    private static boolean display = false;

    static final String CRLF = "\n\r"; //13 10

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    public static String HtmlTop(String Title) {
        String Top = new String();
        Top = "<html>\n" +
                "\t<head>\n" +
                "\t\t<title> 332 Practical 4</title>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "\t\t<link href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css\" rel=\"stylesheet\">\n" +
                "\t\t<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" +
                "\t</head>\n" +
                "\t<body>\n";


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

            tbl += "\t\t\t\t<td>\n" +
                    "\t\t\t\t\t<form method=\"get\" action=\"\">\n" +
                    "\t\t\t\t\t\t<input type=\"hidden\" value=\""+n+"\" name=\"deleteName\">\n" +
                    "\t\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark float-right mr-2\" name=\"delete\" value=\"true\">Delete <small><i class=\"fas fa-trash ml-2\"></i></small> </button>\n" +
                    "\t\t\t\t\t</form>\n";
            tbl +=
                    "\t\t\t\t\t<input type=\"hidden\" value=\""+n+"\" name=\"edit\">" +
                    "\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark float-right mr-2\" name=\"update\" value=\"true\"><span>Edit</span><small><i class=\"fas fa-pen ml-2\"></i></small> </button>" +
                    "\t\t\t\t</td>\n";
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

    public static String formBody() throws FileNotFoundException {
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
                "\t\t<div class=\"card border-dark mb-3\" >\n" +
                "  \t\t\n" +
                "\t\t  \t\t<div class=\"card-body text-dark\">\n" +
                "\t\t  \t\t\t<h5 class=\"card-title\">Edit Entry</h5>\n" +
                "\t\t\t\t\t<form method=\"get\" action=\"\">\n" +
                "\t\t\t\t\t\t<div class=\"row g-3 mb-2 align-items-center\">\n" +
                "\t\t\t\t\t\t  <div class=\"col-4\">\n" +
                "\t\t\t\t\t\t    <label for=\"nameredo\" class=\"col-form-label\">Name: </label>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t  <div class=\"col-8\">\n" +
                "\t\t\t\t\t\t    <input type=\"text\" id=\"nameredo\" class=\"form-control\" name=\"nameredo\" required>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t<div class=\"row g-3 align-items-center\">\n" +
                "\t\t\t\t\t\t  <div class=\"col-4\">\n" +
                "\t\t\t\t\t\t    <label for=\"cellnumredo\" class=\"col-form-label\">Cellphone Number: </label>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\n" +
                "\t\t\t\t\t\t  <div class=\"col-8\">\n" +
                "\t\t\t\t\t\t    <input type=\"tel\" id=\"cellnumredo\" class=\"form-control\" maxlength=\"10\" minlength=\"10\" name=\"cellnumredo\" required>\n" +
                "\t\t\t\t\t\t  </div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark float-right mt-1\" name=\"update\" value=\"true\">Update Person <small><i class=\"fas fa-user-edit ml-2\"></i></small> </button>\n" +
                "\t\t\t\t\t\t<input type=\"hidden\" name=\"usernum\" value=\"\" id=\"usernum\">\n" +
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
                "  \t\t\t\t\t\t\t<input type=\"text\" class=\"form-control\" placeholder=\"Recipient's username\" name=\"searchterm\" aria-label=\"Search Term\" aria-describedby=\"button-addon2\" required>\n" +
                "  \t\t\t\t\t\t\t<div class=\"input-group-append\">\n" +
                "    \t\t\t\t\t\t\t<button type=\"submit\" class=\"btn btn btn-dark\" id=\"button-addon2\" name=\"search\" value=\"true\">Search <small><i class=\"fas fa-search ml-2\"></i></small> </button>\n" +
                "  \t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\n" +
                "\t\t\t\t\t</form>\n";

        if (searchTBL.equals("")){

        }
        else {
            form += "\t\t  \t<h5 class=\"card-title\">Search Results</h5>\n" ;
            form += searchTable();
        }

        form += "\t\t\t\t</div>\n" +
                "\t\t\t</div>\n" +
                "\t</div>";


        return form;
    }

    public static String searchTable() throws FileNotFoundException {
        String tbl = "";
        Scanner dbScanner = new Scanner(searchTBL).useDelimiter("\n");

        tbl += "\t<div class=\"col col-lg-auto\">\n" +
                "\t<table class=\"table table-striped table-hover\">\n" +
                "\t\t<thead>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<th scope=\"col\">Name</th>\n" +
                "\t\t\t\t<th scope=\"col\">Cellphone Number</th>\n" +
                "\t\t\t</tr>\n" +
                "\t\t</thead>\n" +
                "\t\t<tbody>";

        while (dbScanner.hasNext()){
            String str = dbScanner.next();

            tbl += "\t\t\t<tr>\n";
            Scanner lineScanner = new Scanner(str).useDelimiter(",");
            String n = lineScanner.next();
            String n2 = lineScanner.next();

            tbl += "\t\t\t\t<td>" + n + "</td>\n";
            tbl += "\t\t\t\t<td>" + n2 + "</td>\n";

            tbl += "\t\t\t</tr>\n";
        }

        tbl += "\t\t</tbody>\n" +
                "\t</table>\n" +
                "\t</div>\n" +
                "\n" +
                "\t<br/>";

        searchTBL = "";

        return tbl;
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

    public static void searchDB(String searchterm) throws FileNotFoundException {
        //searchTBL
        FileReader input = new FileReader("src/main/resources/friendsdb.txt");
        Scanner dbScanner =new Scanner(input).useDelimiter("\n");
        while (dbScanner.hasNext()) {
            String db = dbScanner.next();
            if (db.toLowerCase().contains(searchterm.toLowerCase())){
                searchTBL += db + "\n";
                System.out.println(db);
            }
        }

    }

    public static void updateRecord(int line, String name,String num) throws IOException {
        System.out.println(line + " " + name + " " + num);
        //if (tblContents.contains(name)){
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/friendsdb.txt"));

            Scanner dbScanner =new Scanner(tblContents).useDelimiter("\n");
            int i = 1;

            while (dbScanner.hasNext()){
                String l = dbScanner.next();
                if (i == line){
                    bw.write(name + "," + num + "\n");
                }
                else {
                    bw.write(l + "\n");
                }
                i++;
            }
            bw.close();
        //}

    }

    public static String HtmlBot() {
        String bt = "";

        bt += "\t<script type=\"text/javascript\">\n" +
                "\t\t(function()\n" +
                "\t\t{\n" +
                "\t\t  if( window.localStorage )\n" +
                "\t\t  {\n" +
                "\t\t    if( !localStorage.getItem('firstLoad') )\n" +
                "\t\t    {\n" +
                "\t\t      localStorage['firstLoad'] = true;\n" +
                "\t\t      location.replace(\"\");\n" +
                "\t\t    }  \n" +
                "\t\t    else\n" +
                "\t\t      localStorage.removeItem('firstLoad');\n" +
                "\t\t  }\n" +
                "\t\t})();\n" +
                "\n" +
                "\t\t$('button:contains(Edit)').on('click', function(){\n" +
                "\t\t\t//$(this).children().closest('span').text(\"Done\");\n" +
                "\t\t\t$('button:contains(Edit)').attr('disabled', 'disabled');\n" +
                "\t\t\tconsole.log($(this).parent().siblings('td')[0].innerHTML);\n" +
                "\n" +
                "\t\t\t$('#nameredo').attr(\"value\", $(this).parent().siblings('td')[0].innerHTML);\n" +
                "\t\t\t$('#cellnumredo').attr(\"value\", $(this).parent().siblings('td')[1].innerHTML);\n" +
                "\t\t\t$('#usernum').attr(\"value\", $(this).parent().siblings('th').text());" +
                "\t\t});\n" +
                "\t</script>";

        bt += "\t</body>\n</html>\n";
        return bt;
    }

    public static String writeResponse() throws FileNotFoundException {

        String html = "";
        String response ="";

        html = HtmlTop("332 Practical 4") + body() + HtmlBot();

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
                    System.out.println(requestType);
                    String name = scanReq.next();
                    name = name.substring(11, name.length());
                    name = name.replaceAll("[\\+]", " ");
                    String num = scanReq.next();
                    num = num.substring(12, num.length());
                    num = num.replaceAll("[\\+]", " ");
                    String update = scanReq.next();
                    String usernum = scanReq.next();
                    usernum = usernum.substring(8);

                    updateRecord(Integer.parseInt(usernum), name, num);
                }
                else if (requestType.contains("delete=true")){
                    String name = scanReq.next();
                    name = name.substring(13, name.length());
                    name = name.replaceAll("[\\+]", " ");
                    removeFromDB(name);
                }
                else if (requestType.contains("search=true")){
                    String searchTerm = scanReq.next();
                    searchTerm = searchTerm.substring(13, searchTerm.length());
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
