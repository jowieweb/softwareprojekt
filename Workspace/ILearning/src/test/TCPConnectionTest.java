package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
import org.junit.Before;
import org.junit.Test;

public class TCPConnectionTest extends TCPConnection {

	public TCPConnectionTest() {
		super(null, "253:255.98.1", 12345);
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void setInnerIPTest(){
		serverIP = "0.0.0.0";
		setInnerIP("tzfuzgukhg");
		assertEquals("0.0.0.0", serverIP);
		setInnerIP("192.18.56.4");
		assertEquals("192.18.56.4", serverIP);
		setInnerIP(null);
		serverIP = "0.0.0.0";
		assertEquals("0.0.0.0", serverIP);
	}

	@Test
	public void setInnerPortTest(){
		port = 2345;
		setInnerPort(789667);
		assertEquals(2345, port);
		setInnerPort(80);
		assertEquals(2345, port);
		setInnerPort(-6);
		assertEquals(2345, port);
		setInnerPort(6543);
		assertEquals(6543, port);		
	}

	
	@Test(expected = TCPClientException.class) //wirft Exception=true
	public void testSendPacketPacketNull() throws TCPClientException {
		sendPacket(null);
	}

	@Test(expected = TCPClientException.class)
	public void testSendPacketIPNull() throws TCPClientException{
		serverIP = null;
		sendPacket(new Packet("test", "test"));
	}

	@Test(expected = TCPClientException.class)
	public void testSendPacketPortNull() throws TCPClientException{
		port = 0;
		sendPacket(new Packet("test", "test"));
	}

	@Test
	public void testSendPacket() throws TCPClientException{
		listener = new ClientListener(){

			@Override
			public void receiveClientData(Packet p) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				assertEquals("Connection Failed", e.getMessage());
				
			}
			
		};
		serverIP = "127.0.0.1";
		port = 3456;
		sendPacket(new Packet("klaus", "test"));
	}
	
	public void testSocks() throws UnknownHostException, IOException, ClassNotFoundException{
		serverIP = "127.0.0.1";
		port = 3456;
		Socket s = new Socket(serverIP, port);
		ObjectOutputStream st = new ObjectOutputStream(s.getOutputStream());
		st.writeObject(new Packet("sarah", "hatKeinPassword"));
		ObjectInputStream in = new ObjectInputStream(s.getInputStream());
		Packet rp = (Packet)in.readObject();
	}
	
	

	
//	@Test
//	public void testRun() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testClient() {
//		fail("Not yet implemented");
//	}

}
