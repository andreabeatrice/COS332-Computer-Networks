package core;

import smtp.SMTPCommand;
import smtp.SMTPStatusCode;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPConnectionWorkerThread  extends Thread{
    private Socket socket;



    static final String CRLF = "\n\r"; //13 10

    public SMTPConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    public static String writeResponse() throws FileNotFoundException {

        String response = String.valueOf(SMTPStatusCode.RESPONSE_220_SERVER_READY.STATUS_CODE);

        return response;
    }

    public static String writeResponse(int code) throws UnknownHostException {
        String response = "";

        switch (code){
            case 220:
                response = SMTPStatusCode.RESPONSE_220_SERVER_READY.STATUS_CODE + " " + InetAddress.getLocalHost() + " " + SMTPStatusCode.RESPONSE_220_SERVER_READY.MESSAGE + CRLF;
            break;
            case 250:
                response = "S: " + SMTPStatusCode.RESPONSE_250_ACTION_TAKEN_AND_COMPLETED.STATUS_CODE + " ";
                break;


        }
        return response;
    }

    public void yourAssignment() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "25");


        Session session = Session.getInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("alarm@localhost.com"));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress("u19130938@tuks.co.za"));

            message.setSubject("This is the Subject Line!");

            message.setText("This is actual message");

            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);

            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int _byte;

            os.write(writeResponse(220).getBytes());

            String request = "";



            while(true){

                String opt = br.readLine();

                if (!opt.equals(null)){
                    Scanner parseCommand = new Scanner(opt);

                    String command = parseCommand.next();

                    for (SMTPCommand cmd : SMTPCommand.values()) {
                        if ((cmd.NAME).equalsIgnoreCase(command)){
                            switch (cmd.NAME) {
                                case "HELO":
                                    os.write(writeResponse(250).getBytes());
                                    break;
                                case "MAIL":
                                    os.write(writeResponse().getBytes());
                                    break;
                                case "RCPT":
                                    System.out.println((cmd.NAME));
                                    break;
                                case "DATA":
                                    System.out.println((cmd.NAME));
                                    break;
                                case "QUIT":
                                    System.out.println((cmd.NAME));
                                    break;
                                default:

                                    break;
                            }

                        }
                        else
                            yourAssignment();
                    }
                }



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
