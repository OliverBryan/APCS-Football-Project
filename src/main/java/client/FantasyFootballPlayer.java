package client;

public class FantasyFootballPlayer {
	private String name;
	private int points = 0;
	
	private double yardsMultiplier;
	private double touchdownsMultiplier;
	
	public FantasyFootballPlayer(String name, double ym, double tm) {
		this.name = name;
		this.yardsMultiplier = ym;
		this.touchdownsMultiplier = tm;
	}
	
	public void score(int yards, int touchdowns) {
		this.points += (yards * yardsMultiplier) + (touchdowns * touchdownsMultiplier);
	}	
	
	public String getName() {
		return this.name;
	}
	
	public int getPoints() {
		return points;
	}
	
	public String toString() {
		return getClass().getName() + ": \nYard Multiplier: " + yardsMultiplier + "\nTouchdown Multiplier: " + touchdownsMultiplier + "\n";
	}
}


