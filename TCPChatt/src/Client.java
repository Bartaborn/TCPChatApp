import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
 
public class Client extends Thread implements ActionListener {
	Socket socket;
	int port;
	String name;
	EndPoint clientEnd;
	ChatGUI chatGUI;
	String serverAddress;
	long sentTime;
	long receivedTime;

	public Client(String name) {
		clientEnd = new EndPoint();
		this.name = name;
		chatGUI = new ChatGUI(this, name);
	}
	public void setServerParameters (String serverAddressString, int serverPortNumber) {
        InetAddress address;
        // Build a socket to a server destination address and program
        try {
            address = InetAddress.getByName(serverAddressString);
            socket = new Socket(address, serverPortNumber);
        }catch(UnknownHostException e){
            System.out.println("Error getting host");
            System.out.println("Error: " + e);
        }catch(IOException e){
            System.out.println("Error creating socket");
            System.out.println("Error: " + e);
        }
    }
	public void run() {
		EndPoint clientEnd = new EndPoint();
		String message = chatGUI.getInput();
		
		do {		
		chatGUI.setInput(clientEnd.readStream(socket)); //Read incoming messages while online
		receivedTime = System.nanoTime();
        calculateRTT();
		}while(!message.equals("/leave"));//Exit "online" loop if message equals /leave
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error closing socket");
            System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
@Override	
public void actionPerformed(ActionEvent e) {
	
	EndPoint clientEnd = new EndPoint();
	String message = chatGUI.getInput();

	// add sender name to message
	sentTime = System.nanoTime();
	message =  name + "-" + message;
	clientEnd.writeStream( message, socket); 
// send the message

	// clear the GUI input field, using a utility function of ChatGUI
	 chatGUI.clearInput();
	}
	private void calculateRTT() {
		long RTT = receivedTime - sentTime;
		System.out.println("RTT in NanoSeconds: " + RTT);
		receivedTime = 0;
		sentTime = 0;
	}

}