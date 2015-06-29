package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.net.Socket;

import org.Packet;
import org.junit.Before;

public class PacketTest extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1964820369300255756L;

	/**
	 * construktor calling the super con.
	 */
	public PacketTest() {
		super("Peter", "pan");
		// TODO Auto-generated constructor stub
	}

	private Login logAdminTest;
	private String usr;
	
	/**
	 * cleaning up...
	 */
	@SuppressWarnings("resource")
	@Before
	public void initli(){
		logAdminTest=Login.ADMIN;
		usr="Peter";
	}

	/**
	 * checking the set socket
	 */
	@Test
	public void setSocketTest(){
		setSocket(null);
		assertEquals(null, this.getSocket());
	}
	
	/**
	 * checking the get socket
	 */
	@Test
	public void getSocketTest(){
		Socket sockTest = new Socket();
		setSocket(sockTest);
		assertEquals(sockTest, this.getSocket());
	}
	
	/**
	 * checking the get Password
	 */
	@Test
	public void getPasswordTest(){
		this.password="munkel";
		assertEquals("munkel", this.getPassword());
	}
	
	/**
	 * checking the getUsername
	 */
	@Test
	public void getUsernameTest(){
		assertEquals(usr, this.getUsername());
	}
	
	/**
	 * checking the login Status
	 */
	@Test
	public void getLoginStatusTest(){
		this.setLoginStatus(Login.ADMIN);
		assertEquals(logAdminTest,this.getLoginStatus());
	}
}
