import java.io.*;
import java.net.*;
public class MailSender
{

private boolean run = true;

	public static void main(String[] args) {
		try{
			new MailSender().connect();
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

		boolean val, alarm;
			
		do {
			id=bf.readLine();

			String emailreg="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[_A-Za-z]{2,})$";

			val=id.matches(emailreg);
			alarm = id.equals("alarm");

			if(!val && !alarm )System.out.println("Wrong format email-id.please re-enter.");
		} while(!val && !alarm );

		pw.println(id);

		if (!alarm) {
			System.out.println("\n Menu\n1)Send Mail\n2)Inbox\n3)Logout");
		}
		
			
		while(true) {
			//System.out.println("\n Menu\n1)Send Mail\n2)Inbox\n3)Logout");

			String opt = bf.readLine();
			
			pw.println(opt);

			if (opt.toLowerCase().contains("helo")) {
				pw.println(bf.readLine()); 
			}
			else {
				int o = Integer.parseInt(opt);
				if (!alarm){
					switch(o) {
						case 1: System.out.println("Send To:");
								pw.println(bf.readLine());
								System.out.println("Message:");
								pw.println(bf.readLine()); 
						break;
						
						//TODO: FIX
						case 2:
							pw.println(bf.readLine());
							//System.exit(0);
						break;

						case 3:
							pw.println(bf.readLine());
							s.close();
							System.exit(0);
						break;
						case 5:
							pw.println(bf.readLine());
						break;
						default	:	
							pw.println(bf.readLine());
						break;
					}
				}
				else {
					pw.println(bf.readLine());
				}
				
			}

			

			
		}
	}
}