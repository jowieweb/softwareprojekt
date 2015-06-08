package org.Client;

import org.Packet;

/**
 * Defines an abstract class that sends packets.
 */
public abstract class Client {
	protected ClientListener listener;

	/**
	 * Constructor awaits a callback method object.
	 * @param listener
	 */
	public Client(ClientListener listener) {
		this.listener = listener;
	}

	/**
	 * Sends a packet.
	 * @param packet packet to send.
	 * @throws TCPClientException
	 */
	public abstract void sendPacket(Packet packet) throws TCPClientException;
}
