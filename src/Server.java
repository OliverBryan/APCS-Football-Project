import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
	private Thread clientListener;
	private int port;
	private ServerSocket serverSocket;
	private ArrayList<Game> games;
	
	private int gameCount = 1;
	private String dataFilePath;
	
	public Server(int port, String dataFilePath) {
		this.port = port;
		this.dataFilePath = dataFilePath;
		games = new ArrayList<>();
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error: port is already in use");
			e.printStackTrace();
		}
		
		/*
		try (
			this.serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out =
				new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
			) {
				String inputLine, outputLine;

				outputLine = "dowd";
				out.println(outputLine);

				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
				    outputLine = "dowd";
				    out.println(outputLine);
				    if (outputLine.equals("Bye."))
				        break;
				} catch (Exception e) {
				throw e;
		*/
	}
	
	public void run() {
		System.out.println("Starting server on port " + port);
		
		while (true) {
			System.out.println("\nWaiting for players...");
			try {
				Socket player1Socket, player2Socket;
				
				player1Socket = serverSocket.accept();
				Connection connection1 = new Connection(player1Socket);
				Player player1 = new Player(connection1);
				
				System.out.println("Accepted player 1 at " + player1Socket.getRemoteSocketAddress().toString().substring(1));
				System.out.println("Waiting for another player...");
				
				player2Socket = serverSocket.accept();
				Connection connection2 = new Connection(player2Socket);
				Player player2 = new Player(connection2);
				
				System.out.println("Accepted player 2 at " + player2Socket.getRemoteSocketAddress().toString().substring(1));
				
				Game game = new Game(player1, player2, gameCount, dataFilePath);
				System.out.println("Starting game " + gameCount);
				games.add(game);
				game.start();
				gameCount++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		if (clientListener == null) {
			clientListener = new Thread(this, "Client Listener");
			clientListener.start();
		}
		
		while (true) {
			for (int i = 0; i < games.size(); i++) {
				if (!games.get(i).finished()) {
					System.out.println("Finished game " + games.get(i).getID());
					games.remove(i);
					i--;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		int portNumber = Integer.parseInt(args[0]);
		
		Server server = new Server(portNumber, args[1]);
		server.start();
		
	}
}
