import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtil {
	private static ArrayList<String> usedNames = new ArrayList<>();
	private static int nameCount;
	
	public static void init(String dataFilePath) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(dataFilePath));
		nameCount = sc.nextInt();
		sc.close();
	}
	
	public static PlayerInfo loadPlayerInfo(String dataFilePath, String targetName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(dataFilePath));
		
		sc.nextLine();
		while (sc.hasNext()) {
			String name = sc.nextLine();
			int money = Integer.parseInt(sc.nextLine());
			
			if (name.equals(targetName)) {
				if (!usedNames.contains(name)) {
					usedNames.add(name);
					nameCount++;
					sc.close();
					return new PlayerInfo(name, money);
				}
				else { // name is already in use
					sc.close();
					return new PlayerInfo("Player" + nameCount, -1);
				}
			}
		}
		
		sc.close();
		return new PlayerInfo(targetName, 100);
	}
	
	public static void writePlayerInfo(String dataFilePath, PlayerInfo player) throws FileNotFoundException {
		usedNames.remove(player.name);
		
		Scanner sc = new Scanner(new File(dataFilePath));
		ArrayList<String> lines = new ArrayList<>();
		
		sc.nextLine();
		lines.add("" + nameCount);
		
		boolean found = false, nextIter = false;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.equals(player.name)) {
				found = true;
				nextIter = true;
			}
			else if (nextIter) {
				line = "" + player.money;
				nextIter = false;
			}
			
			lines.add(line);
		}
		
		if (!found) {
			lines.add(player.name);
			lines.add("" + player.money);
		}
		
		PrintWriter pw = new PrintWriter(new File(dataFilePath));
		for (String line : lines) {
			pw.println(line);
		}
		
		pw.close();
	}
}
