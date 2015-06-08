package org.Client;

import org.Packet;

public abstract class Client {

	protected ClientListener listener;

	public Client(ClientListener listener)
	{
		this.listener = listener;
	}

	public abstract void sendPacket(Packet p) throws TCPClientException;
}
