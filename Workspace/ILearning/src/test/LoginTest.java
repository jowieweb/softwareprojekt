package test;

import static org.junit.Assert.*;

import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
import org.Server.Server;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginTest {

	@BeforeClass
	public static void startServer(){
		ServerStarter ss = new ServerStarter();
		ss.start();
		try{
		Thread.sleep(1500);
		}catch(Exception e){}
	}
	
	
	
	@Test
	public void login() {		
		TCPConnection tcp = new TCPConnection(new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				// TODO Auto-generated method stub
				assertEquals(p.getLoginStatus(), Packet.Login.USER);
				System.out.println(p.getLoginStatus().toString());
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				fail();

			}

		}, "127.0.0.1", 12345);
		sendPackage(new Packet("local", "local"), tcp);
		
	}
	
//	@Test
//	public void getCategories(){
//		TCPConnection tcp = new TCPConnection(new ClientListener() {
//
//			@Override
//			public void receiveClientData(Packet p) {
//				// TODO Auto-generated method stub
//				System.out.println(p.getCategories().get(0));
//				assertNotEquals(p.getCategories(), null);
//			}
//
//			@Override
//			public void exceptionInClientData(TCPClientException e) {
//				// TODO Auto-generated method stub
//				e.printStackTrace();
//				fail();
//
//			}
//
//		}, "127.0.0.1", 12345);
//		sendPackage(new Packet("local", "local"), tcp);
//	}
	
	
	public static void sendPackage(Packet p, TCPConnection tcp){
		try {
			tcp.sendPacket(p);
			Thread.sleep(5000);
		} catch (TCPClientException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail();
		} catch(Exception e){
			fail();
			e.printStackTrace();
		}
	}

}
