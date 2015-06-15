package test;

import static org.junit.Assert.*;
import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
import org.junit.Test;

public class TCPConnectionTest extends TCPConnection {

	/**
	 * construktor, calls super con.
	 */
	public TCPConnectionTest() {
		super(null, "253:255.98.1", 12345);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * checks ip ranges 
	 */
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
	
	/**
	 * checks port ranges
	 */
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

	/**
	 * tryes sending a null packet should fail
	 * @throws TCPClientException fail reason
	 */
	@Test(expected = TCPClientException.class) //wirft Exception=true
	public void testSendPacketPacketNull() throws TCPClientException {
		sendPacket(null);
	}

	/**
	 * sends a test packet should fail ip null
	 * @throws TCPClientException fail reason
	 */
	@Test(expected = TCPClientException.class)
	public void testSendPacketIPNull() throws TCPClientException{
		serverIP = null;
		sendPacket(new Packet("test", "test"));
	}

	/**
	 * sends a test packet should fail port null
	 * @throws TCPClientException
	 */
	@Test(expected = TCPClientException.class)
	public void testSendPacketPortNull() throws TCPClientException{
		port = 0;
		sendPacket(new Packet("test", "test"));
	}

	/**
	 * sends a test packet
	 * @throws TCPClientException failed.
	 */
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
	
}
