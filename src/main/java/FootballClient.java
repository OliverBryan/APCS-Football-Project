import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FootballClient {
	public static void main(String[] args) throws IOException {
        // hostName = args[0];
        //int portNumber = Integer.parseInt(args[1]);
    	
    	String hostName = args[0];//"10.157.1.252"; //"192.168.1.247"; //"10.156.4.133";
        int portNumber = 4000;
        
        boolean auto = true;
        while (true) {
        	if (!auto) break;
        	auto = false;
        	
	        System.out.println("Attempting to connect to " + hostName + ":" + portNumber);
	        Socket socket = new Socket(hostName, portNumber);
	        try {
	        	System.out.println("Connected to " + socket.getPort() + ":" + socket.getLocalPort());
	        	
	        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        	Scanner sc = new Scanner(System.in);
	        	
	        	int moneyAvailable = 0;
	        	
	        	String fromServer;
	        	
	        	ClientGame game = new ClientGame();
	        	while ((fromServer = in.readLine()) != null) {
	        		if (fromServer.startsWith("gi")) {
	        			System.out.println(fromServer.substring(2));
	        			out.println(getInputInBounds(1, 2, sc));
	        		}
	        		else if (fromServer.startsWith("gs")) {
	        			System.out.println(fromServer.substring(2));
	        			out.println(getNonEmptyString(sc));
	        		}
	        		else if (fromServer.startsWith("ma")) {
	        			// BUILD TEAM
	        			moneyAvailable = Integer.parseInt(fromServer.substring(2));
	        			int moneyUsed = game.makeTeam(moneyAvailable, sc); // returns the amount of money used (rerolls * 10);
	        			out.println(moneyUsed);
	        		}
	        		else if (fromServer.startsWith("ss")) {
	        			System.out.println("Running game...");
	        			out.println(game.runGame());
	        			// simulate game
	        			// out.println(score);
	        		}
	        		else if (fromServer.startsWith("gb")) {
	        			moneyAvailable = Integer.parseInt(fromServer.substring(2));
	        			fromServer = in.readLine();
	        			int min = Integer.parseInt(fromServer);
	        			fromServer = in.readLine();
	        			System.out.println(fromServer);
	        			out.println(getInputInBounds(min, moneyAvailable, sc));
	        		}
	        		else if (fromServer.startsWith("eg")) {
	        			socket.close();
	        			
	        			System.out.println("Would you like to play again? (Yes (1) or No(2)");
	        			int choice = getInputInBounds(1, 2, sc);
	        			
	        			if (choice == 1) {
	        				auto = true; continue;
	        			}
	        			else break;
	        		}
	        		else System.out.println(fromServer);
	        	}
	        	
	        	sc.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	return;
	        }
        }
    }
	
	public static int getInputInBounds(int min, int max, Scanner sc) {
		int input = -1;
		do {
			try {
				input = sc.nextInt();
				if (input < min || input > max) System.out.print("Input must be in range [" + min + ", " + max + "], please re enter: ");
			} catch (Exception e) {
				System.out.print("Input must be an integer, please re enter: ");
				sc.nextLine();
			}
		} while (input < min || input > max);
		return input;
	}
	
	public static String getNonEmptyString(Scanner sc) {
		String input = "";
		do {
			input = sc.nextLine();
			if (input.isEmpty()) System.out.println("Input must not be emtpy");
		} while (input.isEmpty());
		return input;
	}
}