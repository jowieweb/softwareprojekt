package org.Server;

import org.Packet;

public class PacketBuilder {

	public static  Packet getPacket(Packet querry, String dbAnswer) {
		Packet answer = copyPacket(querry);
		answer.setFrage(dbAnswer);
		return answer;
	}

	private static Packet copyPacket(Packet querry) {
		Packet p = new Packet(querry.getUsername(), querry.getPassword(),
				Packet.Type.LOGIN, Packet.Login.ADMIN);
		p.setSocket(querry.getSocket());
		p.setSelectedLevel(querry.getSelectedLevel());
		p.setSelectedTopic(querry.getSelectedTopic());
		return p;
	}

}
