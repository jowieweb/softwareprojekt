package org.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.Packet;

/**
 * The class TCPConnection handles the network stuff.
 */
public class TCPConnection extends Client implements Runnable {
	protected String serverIP;
	protected int port;
	private Packet packet;

	/**
	 * The constructor awaits a ClientListener, the ip from the server and a port.
	 * @param listener callback method object
	 * @param ip	the server ip
	 * @param port the serverport
	 */
	public TCPConnection(ClientListener listener, String ip, int port) {
		super(listener);
		
		setInnerIP(ip);
		
		setInnerPort(port);
	}

	protected void setInnerPort(int port) {
		if(port > 1024 && port < 64000) {  //!!!und, und nicht oder!!!
			this.port = port;
		}
	}

	protected void setInnerIP(String ip) {
		if(ip==null)return;
		if(ip.matches("((25[0-5]|(2[0-4]|1{0,1}"
				+ "[0-9]){0,1}[0-9])\\.){3,3}"
				+ "(25[0-5]|(2[0-4]|1{0,1}"
				+ "[0-9]){0,1}[0-9])")) {
			serverIP = ip;
		}
	}
	
	/**
	 * Sends a packet to the server.
	 * @param packet the packet to send
	 */
	public void sendPacket(Packet packet) throws TCPClientException{
		if(serverIP == null || port == 0){
			throw new TCPClientException("SERVER_IP or PORT wrong!!");
		} else if(packet == null){
			throw new TCPClientException("Packet is NULL");
		}
		this.packet = packet;
		Thread t = new Thread(this);
		t.start();
	}

	@SuppressWarnings("resource")
	/**
	 * Thread-run-method. Is invoked by Thread.start().
	 */
	public void run() {
		try {
			Socket s = new Socket(serverIP, port);
			ObjectOutputStream st = new ObjectOutputStream(s.getOutputStream());
			st.writeObject(packet);
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Packet rp = (Packet)in.readObject();
			listener.receiveClientData(rp);
			
		} catch (UnknownHostException e) {
			listener.exceptionInClientData(new TCPClientException("Unknown Host", e));
		} catch (IOException e) {
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
