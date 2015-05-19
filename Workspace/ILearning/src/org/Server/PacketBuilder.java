package org.Server;

public class PacketBuilder {

	public static  Packet getPacket(Packet querry, String dbAnswer) {
		Packet answer = copyPacket(querry);
		answer.setFrage(dbAnswer);
		return answer;
	}

	private static Packet copyPacket(Packet querry) {
		Packet p = new Packet(querry.getUsername(), querry.getPassword(),
				Packet.Type.SERVER, Packet.Login.ADMIN);
		p.setSocket(querry.getSocket());
		return p;
	}

}
