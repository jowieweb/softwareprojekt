package test;

import static org.junit.Assert.*;


import org.junit.Test;

import static org.junit.Assert.*;

import java.net.Socket;

import org.Packet;
import org.Packet.Login;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.Server.PacketBuilder;
import org.Server.Server;

//import net.sourceforge.groboutils.junit.v1.*;
/*
 * PacketTest erstmal ausgelassen, da getter und setter testen hier keinen Sinn macht
 * 
 *  Was ist wichtig?:
 * 				- server starten, mit packeten bewerfen und antwort angucken:
 *				- login - positiv, negativ
 * 				- userverwaltungspackete
 * 				- antwortspiele
 * 
 * Änderungen für mich festgehalten: (14.06.2015)
 * 									-pTest rausgenommen, da ich ja als Objekt fungiere
 * 
 * */

public class PacketTest extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1964820369300255756L;

	public PacketTest() {
		super("Peter", "pan");
		// TODO Auto-generated constructor stub
	}

//	private PacketTest pTest;
	private Login logAdminTest;
	private Login logFailTest;
	private Login logUserTest;
	private String pwd;
	private String usr;
	private Socket client;
	private static Server server;

//	@BeforeClass
//	public static void startServer(){
//		System.out.println("Server is running");
//		server=new Server();
//	}
	
	@Before
	public void initli(){
		logAdminTest=Login.ADMIN;
		pwd="pan";
		usr="Peter";
		this.client = new Socket();
	}

	@Test
	public void setSocketTest(){
		setSocket(null);
		assertEquals(null, this.getSocket());
	}
	
	@Test
	public void getSocketTest(){
		Socket sockTest = new Socket();
		setSocket(sockTest);
		assertEquals(sockTest, this.getSocket());
	}
	
	@Test
	public void getPasswordTest(){
		this.password="munkel";
		assertEquals("munkel", this.getPassword());
	}
	
	@Test
	public void getUsernameTest(){
		assertEquals(usr, this.getUsername());
	}
	
	@Test
	public void getLoginStatusTest(){
		this.setLoginStatus(Login.ADMIN);
		assertEquals(logAdminTest,this.getLoginStatus());
	}
	
	
//	@Test
//	public void testSetLoginStatus() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFrage() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAnswers() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTopics() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetLevel() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSelectedTopic() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSelectedLevel() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPacketType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetImage() {
//		fail("Not yet implemented");
//	}


}
