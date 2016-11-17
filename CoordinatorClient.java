import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CoordinatorClient implements Runnable{

	private Socket socket = null;
	private Coordinator coord;
	
	public CoordinatorClient(Socket socket, Coordinator coord) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.coord = coord;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			ServerEncoderDecoder encodeco = new ServerEncoderDecoder();
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String inputLine;
	        while((inputLine = input.readLine()) != null){
	        	Packet pac = encodeco.decodeData(inputLine.getBytes());
	        	
	        	if(pac.getOperation() == 2){
	        		output.println("ACK");
	        		byte[] data = input.readLine().getBytes();
	        		Packet p = encodeco.decodeData(data);
	        		if(p.getOperation() == 11){
	        			//System.out.println("Commit command received");
	        			synchronized(Server.keyValueMap){
                    		Server.keyValueMap.put(pac.getKey(), pac.getvalue());
                    	}
	        			output.println("ACK");
	        		}
	        	}else if(pac.getOperation() == 3){
	        		//System.out.println("Request for delete received");
	        		output.println("ACK");
	        		byte[] data = input.readLine().getBytes();
	        		Packet p = encodeco.decodeData(data);
	        		if(p.getOperation() == 11){
	        			synchronized (Server.keyValueMap) {
	                		Server.keyValueMap.remove(pac.getKey());
						}
	        			output.println("ACK");
	        		}
	        	}
	        	
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
}
