import java.io.*;
import java.net.*;

public class EndPoint {
	InetAddress address;
	DatagramSocket socket; 
	int portNumber;
	String name;
	public void writeStream(String message, Socket socket) { //write
		OutputStream os;
		try {
			os = socket.getOutputStream();
		
		DataOutputStream dout = new DataOutputStream(os);
		
			dout.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readStream(Socket socket) { //read
		String message = null;
		InputStream is;
		try {
			is = socket.getInputStream();
		
		DataInputStream din = new DataInputStream(is);
		
			message = din.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (message);
	}
	
	
	public void close() {
		socket.close();
	}
}