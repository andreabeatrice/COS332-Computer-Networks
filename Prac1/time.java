import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * time.java<p>
 *
 * <p>
 * Usage:  This library of java functions, which Pat L. Durante has encapsulated inside
 *         a class called cgi_lib as class (static) member functions,
 *         attempts to duplicate the standard PERL CGI library (cgi-lib.pl).
 *
 *
 *         (You are only allowed to use code that you found on the Web and elsewhere if
 *         the licence conditions of that code allows you to use it. If you use such code, it
 *         should be clearly documented that you used the code (as well as where you got it from)).<p>
 * 
 *         Methods provided by Pat L. Durante:
 *            {@link #Header() Header}
 *            {@link #HtmlTop(String Title) Title}
 *            {@link #urlDecode(String in) urlDecode}
 *            {@link #ReadParse(InputStream inStream) ReadParse}
 *            {@link #Variables(Hashtable form_data) Variables}
 *            {@link #HtmlBot() HtmlBot}
 *            {@link #MethGet() MethGet}
 *            {@link #MethPost() MethPost}
 *            {@link #MyBaseURL() MyBaseURL}
 *            {@link #MyFullURL() MyFullURL}
 *            {@link #Environment() Environment}
 *         
 *         @see https://www.infoworld.com/article/2076863/write-cgi-programs-in-java.html#:~:text=A%20CGI%20program%20can%20be,interactive%20applications%20to%20Web%20sites
 *
 * @version 1.0
 * @author Pat L. Durante
 *
 */

@SuppressWarnings("unchecked")
class time {
 
 /**
 *
 * Generate a standard HTTP HTML header.
 *
 * @return A String containing the standard HTTP HTML header.
 *
 */
	public static String Header(){
    return "Content-type: text/html\n\n";
	}

 /**
 *
 * Generate some vanilla HTML that you usually
 * want to include at the top of any HTML page you generate.
 *
 * @param Title The title you want to put on the page.
 *
 * @return A String containing the top portion of an HTML file.
 *
 */
	public static String HtmlTop(String Title){

		String Top = new String();
		Top = "<!DOCTYPE HTML>\n";
		Top += "<html>\n";
		Top += "<head>\n";
		Top += "<meta charset='UTF-8'>\n";
		Top += "<meta name='description' content='COS 332 PA1 CGI Result Page'>\n";
		Top += "<meta name='keywords' content='HTML, Common Gateway Interface, Java'>\n";
		Top += "<meta name='author' content='Andrea Blignaut'>\n";
		Top += "<title>\n";
      		Top += Title;
		Top += "</title>\n";
		Top += "</head>\n";
		Top += "<body>\n";

		return Top;
	}

  /**
   *
   * URL decode a string.<p>
   *
   * Data passed through the CGI API is URL encoded by the browser.
   * All spaces are turned into plus characters (+) and all "special"
   * characters are hex escaped into a %dd format (where dd is the hex
   * ASCII value that represents the original character).  You probably
   * won't ever need to call this routine directly; it is used by the
   * ReadParse method to decode the form data.
   *
   * @param in The string you wish to decode.
   *
   * @return The decoded string.
   *
   */

  public static String urlDecode(String in){

      StringBuffer out = new StringBuffer(in.length());
      int i = 0;
      int j = 0;

      while (i < in.length())
      {
         char ch = in.charAt(i);
         i++;
         if (ch == '+') ch = ' ';
         else if (ch == '%')
         {
            ch = (char)Integer.parseInt(in.substring(i,i+2), 16);
            i+=2;
         }
         out.append(ch);
         j++;
      }
      return new String(out);
  }

/**
   *
   * Parse the form data passed from the browser into
   * a Hashtable.  The names of the input fields on the HTML form will
   * be used as the keys to the Hashtable returned.  If you have a form
   * that contains an input field such as this,<p>
   *
   * <pre>
   *   &ltINPUT SIZE=40 TYPE="text" NAME="email" VALUE="pldurante@tasc.com"&gt
   * </pre>
   *
   * then after calling this method like this,<p>
   *
   * <pre>
   *    Hashtable form_data = cgi_lib.ReadParse(System.in);
   * </pre>
   *
   * you can access that email field as follows:<p>
   *
   * <pre>
   *    String email_addr = (String)form_data.get("email");
   * </pre>
   *
   * @param inStream The input stream from which the form data can be read.
   * (Only used if the form data was posted using the POST method. Usually,
   * you will want to simply pass in System.in for this parameter.)
   *
   * @return The form data is parsed and returned in a Hashtable
   * in which the keys represent the names of the input fields.
   *
   */
  public static Hashtable ReadParse(InputStream inStream){

      Hashtable form_data = new Hashtable();

      String inBuffer = "";

      if (MethGet())
      {
          inBuffer = System.getProperty("cgi.query_string");
      }
      else
      {
          //
          //  TODO:  I should probably use the cgi.content_length property when
          //         reading the input stream and read only that number of
          //         bytes.  The code below does not use the content length
          //         passed in through the CGI API.
          //
          DataInput d = new DataInputStream(inStream);
          String line;
          try
          {
              while((line = d.readLine()) != null)
              {
                  inBuffer = inBuffer + line;
              }
          }
          catch (IOException ignored) { }
      }

      //
      //  Split the name value pairs at the ampersand (&)
      //
      StringTokenizer pair_tokenizer = new StringTokenizer(inBuffer,"&");

      while (pair_tokenizer.hasMoreTokens())
      {
          String pair = urlDecode(pair_tokenizer.nextToken());
          //
          // Split into key and value
          //
          StringTokenizer keyval_tokenizer = new StringTokenizer(pair,"=");
          String key = new String();
          String value = new String();
          if (keyval_tokenizer.hasMoreTokens())
            key = keyval_tokenizer.nextToken();
          else ; // ERROR - shouldn't ever occur
          if (keyval_tokenizer.hasMoreTokens())
            value = keyval_tokenizer.nextToken();
          else ; // ERROR - shouldn't ever occur
          //
          // Add key and associated value into the form_data Hashtable
          //
          form_data.put(key,value);
      }

      return form_data;

  }

  /**
   *
   * Neatly format all of the form data using HTML.
   *
   * @param form_data The Hashtable containing the form data which was
   * parsed using the ReadParse method.
   *
   * @return A String containing an HTML representation of all of the 
   * form variables and the associated values.
   *
   */
  public static String Variables(Hashtable form_data) {

      String returnString;

      returnString = "<dl compact>\n";

      for (Enumeration e = form_data.keys() ; e.hasMoreElements() ;)
      {
          String key = (String)e.nextElement();
          String value = (String)form_data.get(key);
          returnString += "<dt><b>" + key + "</b> <dd>:<i>" +
                          value +
                          "</i>:<br>\n";
      } 

      returnString += "</dl>\n";

      return returnString;

  }


  /**
   *
   * Generate some vanilla HTML that you usually
   * want to include at the bottom of any HTML page you generate.
   *
   * @return A String containing the bottom portion of an HTML file.
   *
   */
  public static String HtmlBot(){

      return "</body>\n</html>\n";
  }

  /**
   *
   * Determine if the REQUEST_METHOD used to
   * send the data from the browser was the GET method.
   *
   * @return true, if the REQUEST_METHOD was GET.  false, otherwise.
   *
   */
  public static boolean MethGet() {
     String RequestMethod = System.getProperty("cgi.request_method");
     boolean returnVal = false;

     if (RequestMethod != null)
     {
         if (RequestMethod.equals("GET") ||
             RequestMethod.equals("get"))
         {
             returnVal=true;
         }
     }
     return returnVal;
  }

  /**
   *
   * Determine if the REQUEST_METHOD used to
   * send the data from the browser was the POST method.
   *
   * @return true, if the REQUEST_METHOD was POST.  false, otherwise.
   *
   */
  public static boolean MethPost() {
     String RequestMethod = System.getProperty("cgi.request_method");
     boolean returnVal = false;

     if (RequestMethod != null)
     {
         if (RequestMethod.equals("POST") ||
             RequestMethod.equals("post"))
         {
             returnVal=true;
         }
     }
     return returnVal;
  }

  /**
   *
   * Determine the Base URL of this script.
   * (Does not include the QUERY_STRING (if any) or PATH_INFO (if any).
   *
   * @return The Base URL of this script as a String.
   *
   */
  public static String MyBaseURL() {
      String returnString = new String();
      returnString = "http://" +
                     System.getProperty("cgi.server_name");
      if (!(System.getProperty("cgi.server_port").equals("80")))
          returnString += ":" + System.getProperty("cgi.server_port");
      returnString += System.getProperty("cgi.script_name");

      return returnString;
  }

  /**
   *
   * Determine the Full URL of this script.
   * (Includes the QUERY_STRING (if any) or PATH_INFO (if any).
   *
   * @return The Full URL of this script as a String.
   *
   */
  public static String MyFullURL() {
      String returnString;
      returnString = MyBaseURL();
      returnString += System.getProperty("cgi.path_info");
      String queryString = System.getProperty("cgi.query_string");
      if (queryString.length() > 0)
         returnString += "?" + queryString;
      return returnString;
  }

  /**
   *
   * Neatly format all of the CGI environment variables
   * and the associated values using HTML.
   *
   * @return A String containing an HTML representation of the CGI environment
   * variables and the associated values.
   *
   */
  public static String Environment(){
      String returnString;

      returnString = "<dl compact>\n";
      returnString += "<dt><b>CONTENT_TYPE</b> <dd>:<i>" +
                      System.getProperty("cgi.content_type") +
                      "</i>:<br>\n";
      returnString += "<dt><b>CONTENT_LENGTH</b> <dd>:<i>" +
                      System.getProperty("cgi.content_length") +
                      "</i>:<br>\n";
      returnString += "<dt><b>REQUEST_METHOD</b> <dd>:<i>" +
                      System.getProperty("cgi.request_method") +
                      "</i>:<br>\n";
      returnString += "<dt><b>QUERY_STRING</b> <dd>:<i>" +
                      System.getProperty("cgi.query_string") +
                      "</i>:<br>\n";
      returnString += "<dt><b>SERVER_NAME</b> <dd>:<i>" +
                      System.getProperty("cgi.server_name") +
                      "</i>:<br>\n";
      returnString += "<dt><b>SERVER_PORT</b> <dd>:<i>" +
                      System.getProperty("cgi.server_port") +
                      "</i>:<br>\n";
      returnString += "<dt><b>SCRIPT_NAME</b> <dd>:<i>" +
                      System.getProperty("cgi.script_name") +
                      "</i>:<br>\n";
      returnString += "<dt><b>PATH_INFO</b> <dd>:<i>" +
                      System.getProperty("cgi.path_info") +
                      "</i>:<br>\n";

      returnString += "</dl>\n";

      return returnString;
  }

  /**
   *
   * Get and print the time at moment of execution
   *
   *
   */
  static void getTime() throws FileNotFoundException{

    Scanner input = new Scanner(new File("GMT.txt"));
    int current = input.nextInt();

    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
    String strDate = dateFormat.format(date);
    TimeZone tz = TimeZone.getDefault();

    if (current == 2){
      System.out.println(dateFormat.format(date) + " " + tz.getDisplayName());
    }
    else {
      SimpleDateFormat sdfGhana = new SimpleDateFormat("hh:mm:ss");
      TimeZone tzInGhana = TimeZone.getTimeZone("Africa/Accra");
      sdfGhana.setTimeZone(tzInGhana);

      String sDateInGH = sdfGhana.format(date); // Convert to String first
      System.out.println(sDateInGH + " Ghanaian Standard Time");
    }
	}

	public static void main( String args[] ) throws FileNotFoundException {

    //
    // Parse the form data into a Hashtable.
    //
      Hashtable form_data = ReadParse(System.in);

    //
    // Get the action the user chose
    //
      String decision = (String)form_data.get("action");

    //
    // Add basic HTML Layout
    //
      System.out.println(Header());
      System.out.println(HtmlTop(decision));
		
    if (decision.equals("display")){
        getTime();
    }
    else {
      try {
        FileWriter myWriter = new FileWriter("GMT.txt");
            
        if (decision.equals("ghana"))
          myWriter.write("0");

        if (decision.equals("za"))
          myWriter.write("2");
         				 
        myWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }

      getTime();
    }

    System.out.print("<form method='POST' action='/cgi-bin/ghanaian.cgi'>\n");
    System.out.print("<input type='hidden' value='ghana' name='action'>\n");
    System.out.print(" <input type='submit'  name='act' value='Switch to Ghanaian Time'/>\n");
    System.out.print("</form>\n");

    System.out.print("<br/>\n");

    System.out.print("<form method='POST' action='/cgi-bin/southafrican.cgi'>\n");
    System.out.print("<input type='hidden' value='za' name='action'>\n");
    System.out.print(" <input type='submit'  name='act' value='Switch to South African Time'/>\n");
    System.out.print("</form>\n");

    System.out.println(HtmlBot());
  }
}
