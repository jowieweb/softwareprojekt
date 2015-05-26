package org.Server;

import org.Packet;

public interface DBConnectorListener {
	public void DBDataReceive(Packet p);
	public void loginFailed(Packet p);
}
