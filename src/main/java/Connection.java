import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void write(int data) {
		out.println(data);
	}
	
	public void write(String msg) {
		out.println(msg);
	}
	
	public int get() throws IOException {
		return Integer.parseInt(in.readLine());
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
	public String getString() throws IOException {
		return in.readLine();
	}
	
	public String getName() {
		return socket.getLocalAddress().toString();
	}
}
