package org.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.Packet;

public class Server implements TCPServerListener {

	private PacketBuilder builder;
	
	public Server() {
		builder = new PacketBuilder();
		new TCPServer(this);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Server running...");
		new Server();
	}

	@Override
	public boolean tcpReceive(Packet p) {
		// TODO Auto-generated method stub
//		dbc.placeQuerry(p);
		
		Packet answer = builder.getPacket(p);
		
		ObjectOutputStream oos = getOOS(p);
		answer.setSocket(null);
		try {
			oos.writeObject(answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	private ObjectOutputStream getOOS(Packet p)
	{
		try {
			ObjectOutputStream ooStream = new ObjectOutputStream(p.getSocket().getOutputStream());
			return ooStream;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
