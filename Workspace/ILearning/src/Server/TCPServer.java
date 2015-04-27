package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer{
	
	private TCPServerListener callback;
	
	public TCPServer(TCPServerListener tsl) {
		
		callback = tsl;
		listen();
	}
	
	public void setTCPCallback(TCPServerListener tsl) {
		callback = tsl;
	}
	
	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			
			while(true) {
				
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				
				try {
					Object obj = ois.readObject();
					if(obj instanceof Packet) {
						Packet p = (Packet) obj;	
						p.setSocket(socket);
						callback.tcpReceive(p);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
