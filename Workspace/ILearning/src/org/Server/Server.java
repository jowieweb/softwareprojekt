package org.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.Packet;

/**
 * The server class represents the server implementation.
 */
public class Server implements TCPServerListener {
	private PacketBuilder builder;
	
	/**
	 * The constructor instantiates the PacketBuilder and TCPServer.
	 */
	public Server() {
		builder = new PacketBuilder();
		new TCPServer(this);
	}

	public static void main(String[] args) {
		System.out.println("Server running...");
		new Server();
	}

	/**
	 * callback method that's invoked by TCPServer when a packet is received.
	 * 
	 * @param p the received packet
	 */
	@Override
	public boolean tcpReceive(Packet p) {
		Packet answer = builder.getPacket(p);
		
		ObjectOutputStream oos = getOOS(p);
		answer.setSocket(null);
		try {
			oos.writeObject(answer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Returns a new ObjectOutputStream for sending and receiving packets.
	 * @param p Old Packet which contains an existing ObjectOutputStream
	 * @return a new ObjectOutputStream
	 */
	private ObjectOutputStream getOOS(Packet p) {
		try {
			ObjectOutputStream ooStream = new ObjectOutputStream(p.getSocket().getOutputStream());
			return ooStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
