import java.io.*;
import java.util.*;

public class readLDIF{


	public static void main(String [] args) throws FileNotFoundException{

		Scanner ldifScanner = new Scanner(new File("addressbook.ldif")).useDelimiter("\n");


		while (ldifScanner.hasNext()){
			System.out.println(ldifScanner.next());
		}

		String userInput;

		do {
			userInput = JOptionPane.showInputDialog(null, "What would you like to do now?");
		} while (!userInput.equals("quit"));


	}


	///ldapadd [arguments] -f filename
}