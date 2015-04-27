package Server;

public interface DBConnectorListener {
	public void DBDataReceive(Packet p);
	public void loginFailed(Packet p);
}
