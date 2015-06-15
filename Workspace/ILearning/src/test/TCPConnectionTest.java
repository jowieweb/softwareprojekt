package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
import org.junit.BeforeClass;
import org.junit.Test;

public class TCPConnectionTest extends TCPConnection{

	/**
	 * construktor, calls super con.
	 */
	public TCPConnectionTest() {
		super(null, "253:255.98.1", 12345);
		// TODO Auto-generated constructor stub
	}
	
	@BeforeClass
	public static void setupTCP(){
		InnerThread input = new InnerThread();
		input.start();
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
	
	/**
	 * The Socket that was started replys and tests if the method is
	 * invoking the callback 
	 * @throws TCPClientException
	 * @throws InterruptedException
	 */
	@Test
	public void testSendPacketSuccess() throws TCPClientException, InterruptedException{
			listener = new ClientListener(){

			@Override
			public void receiveClientData(Packet p) {
				System.out.println("recieve Interface");
				assertEquals("Dummdidummdida", p.getUsername());
				System.out.println("Assertion done!");
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				fail();
			}
			
		};
		serverIP = "127.0.0.1";
		port = 32154;
		Packet p = new Packet("Dummdidummdida", "1");
		p.setPacketType(Packet.Type.CATEGORY);

		sendPacket(p);
		Thread.sleep(500);
	}
	
	private static class InnerThread extends Thread{
		
		public void run(){
			try {
				@SuppressWarnings("resource")
				
				ServerSocket serverTest = new ServerSocket(32154);
				System.out.println("Socket->listen:");
				Socket recieve = serverTest.accept();
				System.out.println("input...");
				ObjectInputStream ois = new ObjectInputStream(recieve.getInputStream());
				String input = ((Packet)ois.readObject()).getUsername();
				ObjectOutputStream st = new ObjectOutputStream(recieve.getOutputStream());
				System.out.println("reply...");
				st.writeObject(new Packet(input, "duudle"));
				recieve.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
