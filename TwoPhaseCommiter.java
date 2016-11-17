
public class TwoPhaseCommiter implements Runnable{

	private ServerConnection serverConnection;
	private Packet packet = new Packet();
	Coordinator c;
	
	public TwoPhaseCommiter(ServerConnection serverConnection, Packet packet, Coordinator c) {
		
		// TODO Auto-generated constructor stub
		this.serverConnection = serverConnection;
		this.packet = packet;
		this.c = c;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
		ServerEncoderDecoder encodeco = new ServerEncoderDecoder();
		
			byte[] datatoWrite = new byte[2000];
				datatoWrite = encodeco.encodeData(packet);
			serverConnection.getOutput().println(new String(datatoWrite));
			String ack = serverConnection.getInput().readLine();
			if(ack.equals("ACK")){
				//System.out.println("ACK for request obtained");
				Coordinator.incrementCounter();
			}
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	 
}
