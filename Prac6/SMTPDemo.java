import java.io.*;
import java.net.*;
public class SMTPDemo
{

private boolean run = true;

	public static void main(String[] args) {
		try{
			new SMTPDemo().connect();
		}
		catch(Exception e){
		
		}
	}
	
	public void connect() throws Exception{

		Socket s = new Socket("localhost", 25);
		
		PrintWriter pw=new PrintWriter(s.getOutputStream(),true);

		BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));

		BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
			
		new Thread(new Runnable() {
			public void run(){
				while(run) {
					try {
						String msg=br.readLine();
						
						if(msg!=null)
							System.out.println(msg);
						
						else run=false;
					}
					catch(Exception e){

					}
				}
			}

		}).start();

		String id;

		boolean val;
			
		pw.println("");
		
		while(true) {

			String opt = bf.readLine();
			
			pw.println(opt);

			if (opt.toLowerCase().contains("helo")) {
				pw.println(bf.readLine()); 
			}

			        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");


			Session session = Session.getInstance(properties);

        session.setDebug(true);


        // Used to debug SMTP issues


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
	}
}