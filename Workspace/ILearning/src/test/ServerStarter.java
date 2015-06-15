package test;

import org.Server.Server;

public class ServerStarter extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("run");
		new Server();
	}

}
