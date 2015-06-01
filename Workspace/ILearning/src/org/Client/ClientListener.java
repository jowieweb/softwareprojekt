package org.Client;

import org.Packet;

public interface ClientListener {

	public void receiveClientData(Packet p);
	public void exceptionInClientData(TCPClientException e);
}
