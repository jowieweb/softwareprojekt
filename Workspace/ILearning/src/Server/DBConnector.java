package Server;

public class DBConnector {

	DBConnectorListener DBCL;

	public DBConnector(DBConnectorListener l) {
		DBCL = l;
	}

	public void placeQuerry(Packet p) {

		if(checkLogin(p))
		{
			Packet answer = PacketBuilder.getPacket(p, "GANZ VIEL KRAM AUS DER DB");
			DBCL.DBDataReceive(answer);			
		}
		else
		{
			DBCL.loginFailed(p);
		}
	}

	private boolean checkLogin(Packet p) {
		
		if(p.getUsername().equals("steven") && p.getPassword().equals("1")){
			System.out.println("steven");
			return true;					
		}
		return false;		
	}

}
