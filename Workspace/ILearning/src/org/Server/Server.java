package org.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Server implements TCPServerListener, DBConnectorListener {

	DBConnector dbc = new DBConnector(this);

	public Server() {
//		Packet p = new Packet("steven", "1", Packet.Type.UNUSED);
//		dbc.placeQuerry(p);
		new TCPServer(this);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server();
	}

	@Override
	public boolean tcpReceive(Packet p) {
		// TODO Auto-generated method stub
		dbc.placeQuerry(p);
		return false;
	}

	@Override 
	public void DBDataReceive(Packet p) {
		// TODO Auto-generated method stub
		ObjectOutputStream oos = getOOS(p);
		p.setSocket(null);
		try {
			oos.writeObject(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void loginFailed(Packet p) {
		// TODO Auto-generated method stub
		// p.sendAnswer();
		try {
			p.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
