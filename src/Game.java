import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*

TODO:
format client output

 */

public class Game implements Runnable {
	private Thread thread;
	private int id;
	
	private Player player1, player2;
	
	private String dataFilePath;
	
	public Game(Player player1, Player player2, int id, String dataFilePath) {
		this.player1 = player1;
		this.player2 = player2;
		this.id = id;
		this.dataFilePath = dataFilePath;
	}
	
	public void error(String msg) {
		System.out.println("Error (Game " + id + "): " + msg);
		player1.getConnection().write("Error: game ended unexpectedly");
		player2.getConnection().write("Error: game ended unexpectedly");
		
		try {
			player1.getConnection().close();
			player2.getConnection().close();
		} catch (IOException e) {
			System.out.println("Error closing player sockets");
		}
	}
	
	public void run() {
		try {
			FileUtil.init(dataFilePath);
		} catch (FileNotFoundException e1) {
			error("could not load player data");
			return;
		}
		
		player2.getConnection().write("\nWaiting other player...");
		player1.getConnection().write("gsEnter your name");
		
		String player1Name, player2Name;
		try {
			player1Name = player1.getConnection().getString();
		} catch (IOException e) {
			error("player1 did not send data");
			return;
		}
		
		
		player1.getConnection().write("\nWaiting for other player...");
		player2.getConnection().write("gsEnter your name");
		try {
			player2Name = player2.getConnection().getString();
		} catch (IOException e) {
			error("player2 did not send data");
			return;
		}
		
		try {
			player1.setInfo(FileUtil.loadPlayerInfo(dataFilePath, player1Name));
			player2.setInfo(FileUtil.loadPlayerInfo(dataFilePath, player2Name));
			
			if (player1.getInfo().money == 0) {
				player1.getInfo().money = 100;
				player1.getConnection().write("You have no money, added $100 to your balance");
			}
			if (player2.getInfo().money == 0) {
				player2.getInfo().money = 100;
				player2.getConnection().write("You have no money, added $100 to your balance");
			}
			
			if (player1.getInfo().money == -1) {
				player1.getConnection().write("The name you entered is already in use, your game is being saved under the name " + player1.getInfo().name);
				player1.getInfo().money = 100;
			}
			if (player2.getInfo().money == -1) {
				player2.getConnection().write("The name you entered is already in use, your game is being saved under the name " + player2.getInfo().name);
				player2.getInfo().money = 100;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			error("could not load player data");
			return;
		}
		
		player1.getConnection().write("You are playing against " + player2.getInfo().name);
		player2.getConnection().write("You are playing against " + player1.getInfo().name);
		
		int curBet = getBet();
		if (curBet < 0)
			return;
		
		broadcast("The bet is $" + curBet);
		
		makeTeams(curBet);
		
		Pair<Integer, Integer> scores = score();
		if (scores.first < 0 && scores.second < 0) 
			return;
		
		endGame(scores, curBet);
		
		try {
			FileUtil.writePlayerInfo(dataFilePath, player1.getInfo());
			FileUtil.writePlayerInfo(dataFilePath, player2.getInfo());
		} catch (FileNotFoundException e) {
			System.out.println("Error: could not write player data to data file");
			e.printStackTrace();
		}
		
		player1.getConnection().write("You now have $" + player1.getInfo().money);
		player2.getConnection().write("You now have $" + player2.getInfo().money);
		
		broadcast("eg");
	}
	
	private int getBet() {
		int curBet = 0;
		
		player2.getConnection().write("Waiting for " + player1.getInfo().name + "...");
		player1.getConnection().write("gb" + Math.min(player1.getInfo().money, player2.getInfo().money));
		player1.getConnection().write("1");
		player1.getConnection().write("Enter your bet (Can bet a max of $" + Math.min(player1.getInfo().money, player2.getInfo().money) + ")");
		try {
			curBet = player1.getConnection().get();
		} catch (IOException e) {
			error("did not receive response from player 1");
			return -1;
		}
		
		Player pivotPlayer = player2;
		Player otherPlayer = player1;
		
		do {
			if (curBet == Math.min(player1.getInfo().money, player1.getInfo().money)) {
				break;
			}
			
			otherPlayer.getConnection().write("Waiting for " + pivotPlayer.getInfo().name + "...");
			//player2.getConnection().write("gb" + player2.getInfo().money);
			pivotPlayer.getConnection().write("gi" + otherPlayer.getInfo().name + " bets " + curBet + ". Will you match(1) or raise(2)?");
			int response = 1;
			try {
				response = pivotPlayer.getConnection().get();
			} catch (IOException e) {
				error("did not receive response from " + pivotPlayer.getInfo().name);
				return -1;
			}
			
			System.out.println(response);
			if (response == 1) {
				break;
			}
			else {
				pivotPlayer.getConnection().write("gb" + Math.min(player1.getInfo().money, player2.getInfo().money));
				pivotPlayer.getConnection().write("" + curBet);
				pivotPlayer.getConnection().write("Enter your bet (Can bet a max of $" + Math.min(player1.getInfo().money, player2.getInfo().money) + ")");
				try {
					curBet = pivotPlayer.getConnection().get();
				} catch (IOException e) {
					error("did not receive response from player 1");
					return -1;
				}
				
				Player temp = pivotPlayer;
				pivotPlayer = otherPlayer;
				otherPlayer = temp;
			}
		
		} while (true);
		
		return curBet;
	}
	
	private void makeTeams(int curBet) {
		player2.getConnection().write("Waiting for " + player1.getInfo().name + "...");
		player1.getConnection().write("ma" + (player1.getInfo().money - curBet));
		try {
			int moneyUsed = player1.getConnection().get();
			System.out.println("Player 1 used $" + moneyUsed);
			player1.getInfo().money -= moneyUsed;
		} catch (IOException e) {
			error("did not recieve response from player 1");
			return;
		}
		
		player1.getConnection().write("Waiting for " + player2.getInfo().name + "...");
		player2.getConnection().write("ma" + (player2.getInfo().money - curBet));
		try {
			int moneyUsed = player2.getConnection().get();
			System.out.println("Player 2 used $" + moneyUsed);
			player2.getInfo().money -= moneyUsed;
		} catch (IOException e) {
			error("did not recieve response from player 2");
			return;
		}
	}
	
	private void endGame(Pair<Integer, Integer> scores, int curBet) {
		System.out.println("Game " + id + ": Player 1 scored " + scores.first + " and player 2 scored " + scores.second);
		if (scores.first > scores.second) {
			System.out.println("Game " + id + ": Player 1 wins");
			
			broadcast(player1.getInfo().name + " wins");
			
			player1.getInfo().money += curBet;
			player2.getInfo().money -= curBet;
		}
		else if (scores.second > scores.first) {
			System.out.println("Game " + id + ": Player 2 wins");
			
			broadcast(player2.getInfo().name + " wins");
			
			player1.getInfo().money -= curBet;
			player2.getInfo().money += curBet;
		}
		else {
			System.out.println("Game " + id + ": Tie game");
			
			broadcast("Tie game");
		}
	}
	
	private Pair<Integer, Integer> score() {
		int player1Score = 0, player2Score = 0;
		
		player1.getConnection().write("ss");
		try {
			player1Score = player1.getConnection().get();
		} catch (IOException e) {
			error("did not receive score from player 1");
			return new Pair<>(-1, -1);
		}
		
		player2.getConnection().write("ss");
		try {
			player2Score = player2.getConnection().get();
		} catch (IOException e) {
			error("did not receive score from player 2");
			return new Pair<>(-1, -1);
		}
		
		return new Pair<>(player1Score, player2Score);
	}
	
	private void broadcast(String msg) {
		player1.getConnection().write(msg);
		player2.getConnection().write(msg);
	}
	
	public void start() {
		thread = new Thread(this, "Game " + id);
		thread.start();	
	}
	
	public boolean finished() {
		return thread.isAlive();
	}
	
	public int getID() {
		return id;
	}
}
