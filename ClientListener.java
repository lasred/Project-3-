import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientListener implements Runnable{

	private ServerSocket serverSocket;
	private Coordinator coord;
	public static BlockingQueue<Socket> requestQueue = new LinkedBlockingQueue<Socket>();
	
	public ClientListener(ServerSocket serverSocket, Coordinator coord) {
		// TODO Auto-generated constructor stub
		this.serverSocket = serverSocket;
		this.coord = coord;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true){
				Socket clientSocket = serverSocket.accept();
				TCPSeverThread tcpServer = new TCPSeverThread(clientSocket, coord);
				Thread thread = new Thread(tcpServer);
				thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}