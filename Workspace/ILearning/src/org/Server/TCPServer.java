package org.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.Packet;

/**
 * The TCPServer class handles sending and receiving packets.
 */
public class TCPServer {
	private TCPServerListener callback;

	/**
	 * constructor registers an callback object.
	 * @param tsl object with callback method tcpReceive
	 */
	public TCPServer(TCPServerListener tsl) {
		callback = tsl;
		listen();
	}

	/**
	 * Waits for packets and invokes callback method (tcpReceive) when packets arrive.
	 */
	@SuppressWarnings("resource")
	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			System.out.println("listening");
			while(true) {

				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

				try {
					Object obj = ois.readObject();
					if(obj instanceof Packet) {
						Packet p = (Packet) obj;
						p.setSocket(socket);
						callback.tcpReceive(p);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
