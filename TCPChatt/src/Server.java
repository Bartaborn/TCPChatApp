
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server extends Thread {
	EndPoint serverEnd;
	int serverPort;
	ChatGUI chatGUI;
	Socket socket;
	HashMap<String, Socket> connectedMembers= new HashMap<String, Socket>();
	String receivedMessage = null;
	
	public Server (int serverPort) {
		this.serverPort = serverPort;
		serverEnd = new EndPoint ();
	}
	
	@SuppressWarnings("resource")
	public void run(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		// Create a server plug
		try {
			serverSocket = new ServerSocket (serverPort);
		} catch (IOException e) {
			System.out.println("Socket Error" + e);
			e.printStackTrace();
		}
		// Create a communication channel
		
		EndPoint serverEnd = new EndPoint();
		do {
			// Build a socket and plug it to server, then listen to incoming streams
			try {
				socket = serverSocket.accept();
				
	;		} catch (IOException e) {
			System.out.println("Error opening socket" + e);
				e.printStackTrace();
			}
			
			// Receive incoming messages
			
			ClientHandler handle = new ClientHandler(socket);
			handle.start();
			
		} while (true);
		
	 }
	
	public class ClientHandler extends Thread {
		String receivedMessage;
		Socket socket;
		EndPoint handleEnd = new EndPoint();

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		public void run(){
			do {
				receivedMessage = serverEnd.readStream(socket);
				String senderName = getNamesFromMessage(receivedMessage);
				
				if (receivedMessage.startsWith("/handshake")) {
					processNewMembership(senderName, socket); //add new member after handshake
					broadcast(senderName + " has joined the chat"); //Need to Handshake before able to chat		
					continue;
				}
				
				if (receivedMessage.startsWith("/tell")) { //tell Banana hello - Oscar
					
					receivedMessage = removeCommandFromMessage(receivedMessage, "/tell");
					String recipientName = getNamesFromMessage(receivedMessage);
					handleEnd.writeStream(senderName + ": "+receivedMessage,connectedMembers.get(senderName));
					handleEnd.writeStream(senderName + ": "+receivedMessage,connectedMembers.get(recipientName));

					continue;
				}
				if (receivedMessage.startsWith("/list")) { //list all online
					list( senderName);
					continue;
				}
				if (receivedMessage.startsWith("/leave")) {
					broadcast(senderName + " left the chat");
					connectedMembers.remove(senderName);//remove from connected list
					continue;
				}
				if (receivedMessage.startsWith("/broadcast")) {
					broadcast(senderName +": " + receivedMessage);
					continue;
				}
		
			} while (true);
		}
	
		private String getNamesFromMessage(String message) { //Segments String
			String returnName = null;
			for(int i = 0; i < message.length(); i++) {
				char nameSeparator = message.charAt(i);
				if(nameSeparator == 45) { 					// If "-" cut
					returnName = message.substring(0, i);
					receivedMessage = message.substring(i+1);
					break;
				}
				else if(nameSeparator == 32) { 				//If " " cut
					returnName = message.substring(0, i);
					receivedMessage = message.substring(i+1);
					break;
				}
			}
			return returnName; //return the split part, here either sender or receivers name from Message
		}
		
		private void list(String client) {
			String name = null;
			for(String onlineUsers : connectedMembers.keySet()) {
				name = onlineUsers; //Loop through all people online and send a list of people online
				serverEnd.writeStream(name, socket);//to the sender
			}	
		}
		
		private void broadcast(String receivedMessage) {

			for(Socket onlineUsers : connectedMembers.values()) { //loop through all online and send to all
				serverEnd.writeStream(receivedMessage, onlineUsers); // except for the sender
			}
		} 
	}
		
		
		private void processNewMembership(String name, Socket socket) {
			connectedMembers.put(name, socket); //In a handshake add the members to the list
		}
		
		private String removeCommandFromMessage(String message, String prefix) {
			return message.substring(prefix.length()+1); //removes the commad ex, /tell and /list
		}
}