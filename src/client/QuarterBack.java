package client;

public class QuarterBack extends FantasyFootballPlayer {
	public QuarterBack(String name, double ym, double tm) {
		super(name, ym, tm);
	}
	
	public void score(int yards, int touchdowns) {
		super.score(yards * 25, touchdowns * 4);
	}
}
