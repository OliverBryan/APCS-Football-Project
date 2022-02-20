
public class Player {
	private Connection connection;
	private PlayerInfo info;
	
	public Player(Connection connection) {
		this.connection = connection;
	}
	
	public void setInfo(PlayerInfo info) {
		this.info = info;
	}
	
	public PlayerInfo getInfo() {
		return info;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
}
