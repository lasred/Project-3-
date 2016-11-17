import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
	
	public static Map<String,String> keyValueMap = new HashMap<String, String>();
	public static BlockingQueue<Socket> requestQueue = new LinkedBlockingQueue<Socket>();	
	public static int coordinationPort = 9876;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 2){
			System.out.println("Incorrect no of arguments");
			System.exit(1);
		}
		String mode = "TCP";
		int portNo = Integer.parseInt(args[0]);
		String filePath = args[1];
		if("TCP".equalsIgnoreCase(mode)){
			try{
				BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
				ServerSocket coordination = new ServerSocket(coordinationPort);
				ServerSocket serverSocket = new ServerSocket(portNo);
				System.out.println("Press any key and enter after starting all the servers");
				String choice = consoleInput.readLine();
				Coordinator coord = new Coordinator(filePath);
				CoordinationListener coordListener = new CoordinationListener(coordination, coord);
				Thread thread = new Thread(coordListener);
				thread.start();
				ClientListener clientListener = new ClientListener(serverSocket, coord);
				Thread clientThread = new Thread(clientListener);
				clientThread.start();
				while(true){
					
				}
				/*while(true){
					Socket clientSocket = serverSocket.accept();
					requestQueue.add(clientSocket);
					//TCPSeverThread tcpServerThread = new TCPSeverThread(clientSocket);
					//Thread thread = new Thread(tcpServerThread);
					//thread.start();
				}*/
			}catch(Exception e){
				
			}
		}else if("UDP".equalsIgnoreCase(mode)){
			
			try {
				UDPServerThread udpServer = new UDPServerThread(portNo);
				Thread thread = new Thread(udpServer);
				thread.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if("RPC".equalsIgnoreCase(mode)){
			
		}else{
			System.out.println("Incorrect mode entered");
			System.exit(1);
		}
		
	}

}
 