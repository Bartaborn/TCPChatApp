import java.net.InetAddress;
import java.net.Socket;

public class Driver {
	public static void main(String[] args){
	Server serverInstance;
	Client clientInstance;
	Client clientInstance2;
	Client clientInstance3;
	Client clientInstance4;
	
	
	
	int serverPort = 1234;
	String serverString = "localhost";
	serverInstance = new Server(serverPort);
	serverInstance.start();
	clientInstance = new Client("Andrei");
	clientInstance2 = new Client("Banana");
	clientInstance3 = new Client("Oscar");
	clientInstance4 = new Client("Anden");
	

	clientInstance.setServerParameters(serverString, serverPort);
	clientInstance2.setServerParameters(serverString, serverPort);
	clientInstance3.setServerParameters(serverString, serverPort);
	clientInstance4.setServerParameters(serverString, serverPort);
	
	
	clientInstance.start();
	clientInstance2.start();
	clientInstance3.start();
	clientInstance4.start();
	

	}
}
