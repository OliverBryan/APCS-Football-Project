import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import client.FantasyFootballPlayer;
import client.QuarterBack;
import client.RunningBack;
import client.WideReceiver;

public class ClientGame {
	private String qb = "║        QUARTERBACK        ║\n";
	private String rb = "║       RUNNING BACK        ║\n";
	private String wr = "║       WIDE RECEIVER       ║\n";
	private ArrayList<FantasyFootballPlayer> team;
	
	public ClientGame() {
		team = new ArrayList<>();
	}

	private int skill() {
		return (int)((Math.pow(10, Math.random()))+0.6);
	}

	private FantasyFootballPlayer[] generatePlayer(String type, int amount) {
		FantasyFootballPlayer[] players = new FantasyFootballPlayer[amount];
		for(int i = 0; i < amount; i++) {
			int skill1 = skill();
			int skill2 = skill();
			String bar1 = (skill1 != 10)?("║       Touchdown: " + skill1 + "        ║\n"):("║       Touchdown: " + skill1 + "       ║\n");
			String bar2 = (skill2 != 10)?("║         Running: " + skill2 + "        ║\n"):("║         Running: " + skill2 + "       ║\n");
			String box = "╔═══════════════════════════╗\n"+type+bar1+bar2+
					"╚═══════════════════════════╝";
			System.out.println(box);
			if(type.equals(qb))
				players[i] = new QuarterBack("Player " + i, skill1, skill2);
			else if(type.equals(rb))
				players[i] = new RunningBack("Player " + i, skill1, skill2);
			else if(type.equals(wr))
				players[i] = new WideReceiver("Player " + i, skill1, skill2);
		}
		return players;
	}

	// returns the amount of money used in rerolls (NOT the amount of money remaining) -- make sure money used does not exceed moneyAvailable
	public int makeTeam(int moneyAvailable, Scanner sc) {
		System.out.println("TEAM CREATION");
		int rerolls = 0;
		while(true) {
			int response;
			QuarterBack quarterBack = (QuarterBack)generatePlayer(qb, 1)[0];
			if(moneyAvailable >= 10) {
				System.out.println("Re-roll for $10? Yes(1) or No(2) (You have $" + moneyAvailable + ")");
				response = sc.nextInt();
				while(response != 1 && response != 2) {
					System.out.println("Invalid input. Enter (1) or (2)");
					response = sc.nextInt();
				}
				if(response == 2) {
					team.add(quarterBack);
					break;
				}
				moneyAvailable -= 10;
				rerolls++;
			} else {
				team.add(quarterBack);
				break;
			}
			System.out.println();
		}

		while(true) {
			int response;
			FantasyFootballPlayer[] rbs = generatePlayer(rb, 4);
			if(moneyAvailable >= 10) {
				System.out.println("Re-roll for 10? Yes(1) or No(2)");
				response = sc.nextInt();
				while(response != 1 && response != 2) {
					System.out.println("Invalid input. Enter (1) or (2)");
					response = sc.nextInt();
				}
				if(response == 2) {
					team.addAll(Arrays.asList(rbs));
					break;
				}
				moneyAvailable -= 10;
				rerolls++;
			} else {
				team.addAll(Arrays.asList(rbs));
				break;
			}
			System.out.println();
		}

		while(true) {
			int response;
			FantasyFootballPlayer[] wrs = generatePlayer(wr, 4);
			if(moneyAvailable >= 10) {
				System.out.println("Re-roll for 10? Yes(1) or No(2)");
				response = sc.nextInt();
				while(response != 1 && response != 2) {
					System.out.println("Invalid input. Enter (1) or (2)");
					response = sc.nextInt();
				}
				if(response == 2) {
					team.addAll(Arrays.asList(wrs));
					break;
				}
				moneyAvailable -= 10;
				rerolls++;
			} else {
				team.addAll(Arrays.asList(wrs));
				break;
			}
			System.out.println();
		}

		return rerolls*10;
	}
	
	// returns the amount of points scored by the team, effectively simulates a game
	public int runGame() {
		// I will leave the main details of this up to you, but this is the way I would recommend doing it:
		//
		// for each player in the team, call score() with a random amount of touchdowns and a random amount of yards,
		// then add that the score returned by score() to a total score for the team, then return that
		//
		// you can feel free to modify this part however you wish, I just need the function to return an integer score that 
		// can be compared between teams -- keep in mind teams with better stats should win most of the time, I am trusting you to balance this
		//
		// right now this returns a random number for testing with the server
		double totalScore = 0;
		for(FantasyFootballPlayer player : team) {
			player.score((int)(Math.random()*10)+1, (int)(Math.random()*10)+1);
			totalScore += player.getPoints();
		}
		return (int) (totalScore);
	}
}
