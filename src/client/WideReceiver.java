package client;

public class WideReceiver extends FantasyFootballPlayer {
	public WideReceiver(String name, double ym, double tm) {
		super(name, ym, tm);
	}
	
	public void score(int yards, int touchdowns) {
		super.score(yards * 15, touchdowns * 6);
	}
}
