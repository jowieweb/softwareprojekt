package clientProjekt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Server.Packet;

public class TCPConnection extends Client implements Runnable{
	
	private String serverIP;
	private int port;
	private Packet packet;

	public TCPConnection(ClientListener listener, String ip, int port) {
		super(listener);
		if(ip.matches("((25[0-5]|(2[0-4]|1{0,1}"
				+ "[0-9]){0,1}[0-9])\\.){3,3}"
				+ "(25[0-5]|(2[0-4]|1{0,1}"
				+ "[0-9]){0,1}[0-9])"))
			serverIP = ip;
		if(port > 1024 || port < 64000)
			this.port = port;
			
	}

	@Override
	public void sendPacket(Packet p) throws TCPClientException{
		if(serverIP==null||port == 0){
			throw new TCPClientException("SERVER_IP or PORT wrong!!");
		}
		packet = p;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		try {
			Socket s = new Socket(serverIP, port);
			ObjectOutputStream st = new ObjectOutputStream(s.getOutputStream());
			st.writeObject(packet);
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Packet rp = (Packet)in.readObject();
			listener.recieveClientData(rp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			listener.exceptionInClientData(new TCPClientException("Unknown Host", e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			listener.exceptionInClientData(new TCPClientException("Connection Failed", e));
		} catch (ClassNotFoundException e) {
			listener.exceptionInClientData(new TCPClientException("Responding Element is wrong type.", e));
			
		} catch (ClassCastException e){
			listener.exceptionInClientData(new TCPClientException("Responding Element is wrong type.", e));

		} catch (Exception e){
			listener.exceptionInClientData(new TCPClientException("Unknown exception", e));

		}
	}

}
