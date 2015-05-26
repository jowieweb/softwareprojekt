package org.Server;

import org.Packet;

public interface TCPServerListener {
	public boolean tcpReceive(Packet p);
}
