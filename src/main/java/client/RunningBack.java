package client;

public class RunningBack extends FantasyFootballPlayer {
	public RunningBack(String name, double ym, double tm) {
		super(name, ym, tm);
	}
	
	public void score(int yards, int touchdowns) {
		super.score(yards * 10, touchdowns * 6);
	}
}
