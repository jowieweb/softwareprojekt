package test;

import static org.junit.Assert.*;

import org.Packet;
import org.Client.ClientListener;
import org.Client.TCPClientException;
import org.Client.TCPConnection;
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
	
	@Test
	public void getCategories(){
		TCPConnection tcp = new TCPConnection(new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				// TODO Auto-generated method stub
				assertNotNull(p.getCategories());
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				fail();

			}

		}, "127.0.0.1", 12345);
		Packet p = new Packet("local", "local");
		p.setPacketType(Packet.Type.CATEGORY);
		sendPackage(p, tcp);
	}
	
	@Test
	public void getFrage(){
		TCPConnection tcp = new TCPConnection(new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				// TODO Auto-generated method stub
				assertNotNull(p.getQuestion());
				if(p.getQuestion().length()<=1)
				{
					fail("to small");
				}
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				fail();

			}

		}, "127.0.0.1", 12345);
		Packet p = new Packet("local", "local");
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		p.setSelectedTopic("Einstiegsveranstaltung");
		sendPackage(p, tcp);
	}
	
	@Test
	public void getFrageAntworten(){
		TCPConnection tcp = new TCPConnection(new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				// TODO Auto-generated method stub
				assertNotNull(p.getAnswers());
				if(p.getAnswers()[0].length()<=1)
				{
					fail("to small");
				}
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				fail();

			}

		}, "127.0.0.1", 12345);
		Packet p = new Packet("local", "local");
		p.setPacketType(Packet.Type.ANSWER_QUESTION);
		p.setSelectedTopic("Einstiegsveranstaltung");
		sendPackage(p, tcp);
	}
	
	
	public static void sendPackage(Packet p, TCPConnection tcp){
		try {
			tcp.sendPacket(p);
			Thread.sleep(500);
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
