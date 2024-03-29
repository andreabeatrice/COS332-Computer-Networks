import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class telnetclient {

    private Socket socket;
    private Socket clientActor;
    private Writer pw;
    private BufferedReader br, bf;

    private ServerSocket serverActor;

    private DataInputStream in;
    private DataOutputStream out;
    private PrintWriter output;

    public telnetclient (String address, int port){
        try {
            //Server Actor
            serverActor = new ServerSocket(55555);
            //System.out.println("Proxy started. Waiting for client...");

            socket = serverActor.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            output = new PrintWriter(socket.getOutputStream(), true); //, StandardCharsets.ISO_8859_1

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));

            //Client Actor
            clientActor = new Socket("10.0.0.2", 23);

            pw = new PrintWriter(clientActor.getOutputStream(), true);

            br = new BufferedReader(new InputStreamReader(clientActor.getInputStream(), StandardCharsets.ISO_8859_1)); //

            //bf = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    output.write(br.read());

                    System.out.println(br.read());

                    /*if (out.read() != 0){
                        System.out.println(out.read());
                    }*/

/*                    byte[] buffer = new byte[1024];
                    int read;
                    while((read = in.read(buffer)) != -1) {
                        String output = new String(buffer, 0, read);
                        System.out.print(output);
                        System.out.flush();
                    };*/

                }
                catch(Exception e){

                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String [] args) throws IOException {
        DataInputStream input = new DataInputStream(System.in);

        String line = input.readLine();


        if (line.contains("telnet")){
            telnetclient client = new telnetclient("10.0.0.2", 23);

        }

    }
}