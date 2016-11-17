import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Coordinator {

	private int communicationPort = 9876;
	private List<ServerConnection> serverConnections = new ArrayList<ServerConnection>();
	private ServerState state = ServerState.IDLE;
	private static int counter = 0;
			
	
	public Coordinator(String serverInfoFile) {
		// TODO Auto-generated constructor stub
		try{
		File serverInfo = new File(serverInfoFile);
		BufferedReader br = new BufferedReader(new FileReader(serverInfo));
		String line;
		while ((line = br.readLine()) != null) {
			Socket socket = new Socket(line.trim(), communicationPort);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			ServerConnection serverConnection = new ServerConnection(socket, input, output);
			serverConnections.add(serverConnection);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public synchronized boolean doTwoPhaseCommit(Packet packet){
		try{
		
		//System.out.println("Coord:"+packet.getOperation());
		ExecutorService es = Executors.newCachedThreadPool();
		counter = 0;
		for(ServerConnection socket : serverConnections){
			TwoPhaseCommiter tpc = new TwoPhaseCommiter(socket, packet, this);
			es.execute(tpc);
		}
		es.shutdown();
		boolean finshed = es.awaitTermination(30, TimeUnit.SECONDS);
		if(counter == serverConnections.size()){
			counter = 0;
			Packet p = new Packet();
			p.setOperation((short)11);
			ExecutorService es1 = Executors.newCachedThreadPool();
			for(ServerConnection socket : serverConnections){
				TwoPhaseCommiter tpc = new TwoPhaseCommiter(socket, p, this);
				es1.execute(tpc);
			}
			es1.shutdown();
			boolean done = es1.awaitTermination(30, TimeUnit.SECONDS);
			if(counter == serverConnections.size()){
				return true;
			}
		}else{
		}
		
		}catch(Exception e){
			return false;
		}
		
		return false;
	}

	public int getCommunicationPort() {
		return communicationPort;
	}

	public void setCommunicationPort(int communicationPort) {
		this.communicationPort = communicationPort;
	}

	public List<ServerConnection> getServerConnections() {
		return serverConnections;
	}

	public void setServerConnections(List<ServerConnection> serverConnections) {
		this.serverConnections = serverConnections;
	}

	public int getCounter() {
		return counter;
	}

	public synchronized static void incrementCounter() {
		 counter++;
	}

	public ServerState getState() {
		return state;
	}


	public synchronized void setState(ServerState state) {
		this.state = state;
	}
	
	
}
