package test;

import static org.junit.Assert.*;

import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
import org.junit.Before;
import org.junit.Test;

public class TCPConnectionTest extends TCPConnection{

	private ClientListener cli;
	public Packet pck=new Packet("Peter","Pan");
	private Packet dummyPacket;

	public TCPConnectionTest() {
		super(null, "253:255.98.1", 12345);
		// TODO Auto-generated constructor stub
	}

	@Before
	public void init(){
		dummyPacket = new Packet("user","pwd");
	}
	
	@Test(expected = TCPClientException.class) //wirft Exception=true
	public void testSendPacket() throws TCPClientException {
		sendPacket(dummyPacket);
	}

	@Test //Matched die IP-Adresse? (Negativ)
	public void testTCPConnectionIPFalse() {
		assertNotEquals("253.27:98.1", this.serverIP);
	}
	
	@Test //Gueltiger Port (Negativ)
	public void testTCPConnectionPortFalse() {
		assertNotEquals(0, this.port);
	}
	
	@Test //Matched die IP-Adresse? (Positiv)
	public void testTCPConnectionIPTrue() {
		assertEquals(null, this.serverIP);
	}
	
	@Test //Gueltiger Port (Positiv)
	public void testTCPConnectionPortTrue() {
		assertEquals(12345, this.port);
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
