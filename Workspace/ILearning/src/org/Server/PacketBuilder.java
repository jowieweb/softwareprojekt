package org.Server;

import org.Packet;

public class PacketBuilder {

	public static  Packet getPacket(Packet querry, String dbAnswer, Packet.Login login, Packet.Type type) {
		Packet answer = copyPacket(querry);
		answer.setPacketType(type);
		answer.setLoginStatus(login);
//		answer.setFrage(dbAnswer);
		return answer;
	}
	

	private static Packet copyPacket(Packet querry ) {
		Packet p = new Packet(querry.getUsername(), querry.getPassword());
		p.setSocket(querry.getSocket());
		p.setSelectedLevel(querry.getSelectedLevel());
		p.setSelectedTopic(querry.getSelectedTopic());
		p.setSelectedAnswer(querry.getSelectedAnswer());
		p.setFrage(querry.getFrage());
		p.setWasRight(querry.getWasRight());
		if(querry.getImage() != null)
			p.setImage(querry.getImage());
		return p;
	}

}
