package org.Server;

import org.Packet;

public class PacketBuilder {

	DBConnector dbc;
	
	public PacketBuilder(){
		dbc = new DBConnector();
	}
	
	
	
	public  Packet getPacket(Packet querry) {
		Packet answer = copyPacket(querry);

		answer.setLoginStatus(dbc.checkLogin(answer.getUsername(), answer.getPassword()));
		if(answer.getLoginStatus() == Packet.Login.FAIL){
			return answer;
		}
		
		switch (answer.getPacketType()) {
		case CATEGORY:
			dbc.addCategories(answer);
			dbc.addLevel(answer);
			break;
		case ANSWER_QUESTION:
			dbc.checkAnswers(answer);
			dbc.setFrage(answer);
			break;
		default:				
			break;
		}
		
		return answer;
	}
	

	private Packet copyPacket(Packet querry ) {
		Packet p = new Packet(querry.getUsername(), querry.getPassword());
		p.setSocket(querry.getSocket());
		p.setPacketType(querry.getPacketType());
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
