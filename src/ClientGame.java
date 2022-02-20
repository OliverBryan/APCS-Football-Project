import java.util.ArrayList;
import java.util.Scanner;

import client.FantasyFootballPlayer;

public class ClientGame {
	
	private ArrayList<FantasyFootballPlayer> team;
	
	public ClientGame() {
		team = new ArrayList<>();
	}

	// returns the amount of money used in rerolls (NOT the amount of money remaining) -- make sure money used does not exceed moneyAvailable
	public int makeTeam(int moneyAvailable, Scanner sc) {
		// what you need to do: generate a random team with 1 quarterback, 4 wide receivers, and 4 running backs
		//
		// for each player, generate a list of 3 players with randomized stats, show them to the player, and and allow the player
		// to either pick a player, or re roll the list (costs $10, make sure they have enough money using moneyAvailable)
		//
		// in terms of generating random stats, you need to generate two stats, a yardMultiplier and a touchdownMultiplier, just pass
		// these to the FantasyFootballPlayer constructor and it will take care of scoring with them. (the footballplayer class already 
		// has an adequete toString for printing purposes)
		//
		// after you have finished selecting players, make sure to add all of them to the team array list, and then return the
		// amount of money spend on re rolls (this is important)
		//
		// right now it just returns a random number as a dummy for the server
		//
		// IMPORTANT: MAKE SURE YOU USE THE SCANNER PASSED TO THE FUNCTION, OPENING A NEW SCANNER WILL NOT WORK (due to the way the server works -- quircky java)
		
		return (int) (Math.random() * moneyAvailable);
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
		
		return (int) (Math.random() * 10);
	}
}
