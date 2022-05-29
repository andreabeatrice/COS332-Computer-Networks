import java.io.*;
import java.net.Socket;

public class telnetclient {

    public telnetclient (String address, int port){
        try {
            Socket clientsocket = new Socket(address, port);

            BufferedReader serverInput = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

            //String msg = serverInput.readLine();

            InputStream raw = clientsocket.getInputStream();

            InputStream buffer = new BufferedInputStream(raw);
            
            // chain the InputStream to a Reader
            Reader r = new InputStreamReader(buffer);
            int c;
            while ((c = r.read(  )) != -1) {
                System.out.print((char) c);
            }


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