import java.io.*;
import java.net.*;
import java.util.*;
import java.net.InetAddress;


public class MailServer
{
	private ArrayList<user>users=new ArrayList<user>();
	
	public static void main(String[] args) {
		try {
			new MailServer().start();
		}
		catch(Exception e){

		}
	}

	public void start() throws Exception{

		ServerSocket server = new ServerSocket(25);

		Socket clientSocket;

		while(true)
		{
			clientSocket = server.accept();

			System.out.println("Client connected..");

			new Thread(new SocketHandler(clientSocket)).start();
		}
	}

	public boolean registerId(String id, SocketHandler sh) {
		user n;

		int i;

		for(i=0; i<users.size(); i++) {
			n=users.get(i);

			if(n.id.equals(id)){

				/*if(n.sh != null)
					return false;*/

				n.sh=sh;
				return true;
			}
		}

		user u = new user();

		u.id=id;

		u.sh=sh;

		users.add(u);

		return true;
	}

	public void sendMessage(String id,String to,String mbody) {
		message msg = new message();
		
		msg.from=id;
		
		msg.message=mbody;
		
		user n;
		
		for(int i=0;i<users.size();i++){	
			n=users.get(i);
			
			if(n.id.equals(to))
				n.msgs.add(msg);
		}

		user u = new user();

		u.id = to;
		u.msgs.add(msg);
		users.add(u);
	}

	public ArrayList<message> getMessages (String id){
		for(int i=0;i<users.size();i++) {
			user n=users.get(i);
			if(n.id.equals(id))
				return n.msgs;
		}
		return null;
	}

	public void logout(String id){
		for(int i=0;i<users.size();i++){

			user n=users.get(i);
			
			if(n.id.equals(id))
				n.sh=null;
		}
	}

	class user {
		String id;

		ArrayList<message>msgs=new ArrayList<message>();

		SocketHandler sh;
	}

	class message {
		String from;
		//TODO: addsubject?
		String message;
	}

	class SocketHandler implements Runnable{

		public Socket s;

		private String id;

		public SocketHandler(Socket s) {
			this.s=s;
		}

		public void run() {
			try {
				PrintWriter pw=new PrintWriter(s.getOutputStream(),true);

				BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));

				pw.println("Enter your email-id:");

				id=br.readLine();

				if(registerId(id, this)) {
					pw.println("Hi "+id+", you are successfully logged in.");
				}

				while(true){

					System.out.println("Show Menu\n");

					String opt = br.readLine();

					if (opt.contains("helo")) {
						pw.println("250 localhost Hello " + InetAddress.getLocalHost() + ", pleased to meet you");
					}


					/*switch(opt){

						case 1:	System.out.println("Enter Reciver Address : ");
								String to=br.readLine();
								System.out.println("Enter Message : ");
								String body=br.readLine();

								sendMessage(id,to,body);

								pw.println("Mail sent to: " + to + "\n");
						break;

						case 2	:	
							ArrayList<message>al=getMessages(id);
								if(al.size()==0) {
									pw.println("No messages");
									
								}
								else {
									for(int i=0; i < al.size();i++){
										message m=al.get(i);
										pw.println("From: "+m.from);

										pw.println("Message: "+m.message);
									}
								}

								

						break;
						case 3	:	
								pw.println("You are successfully logged out ..");
								logout(id);
								s.close();
								System.exit(0);
						break;
						default	:	

						break;
					}*/
				}
			} catch(Exception e){}
		}
	}
}