public class PlayerInfo {
	public String name;
	public int money;
		
	public PlayerInfo(String name, int money) {
		this.name = name;
		this.money = money;
	}
	
	public String toString() {
		return "Name: " + name + ", Money: " + money;
	}
}