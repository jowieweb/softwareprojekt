package org.Client;

import org.Server.Packet;

public interface ClientListener {

	public void recieveClientData(Packet p);
	public void exceptionInClientData(TCPClientException e);
}
