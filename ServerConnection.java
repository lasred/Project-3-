import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {

	Socket socket;
	BufferedReader input;
	PrintWriter output;
	
	public ServerConnection(Socket socket, BufferedReader reader, PrintWriter writer) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.input = reader;
		this.output = writer;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getInput() {
		return input;
	}

	public void setInput(BufferedReader input) {
		this.input = input;
	}

	public PrintWriter getOutput() {
		return output;
	}

	public void setOutput(PrintWriter output) {
		this.output = output;
	}
}
