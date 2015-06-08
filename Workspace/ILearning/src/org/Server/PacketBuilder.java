package org.Server;

import org.Packet;

/**
 * class that builds packets
 */
public class PacketBuilder {
	DBConnector dbc;

	/**
	 * The constructor instantiates a new DBConnector.
	 */
	public PacketBuilder(){
		dbc = new DBConnector();
	}

	/**
	 *
	 * @param querry
	 * @return
	 */
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
		case USER_MANAGEMENT:
			switch(querry.getManagementType()){
			case REMOVE_USER:
				if(querry.getQuestion() != null){
					dbc.removeUser(querry);
				}
				break;
			default:
				break;				
			}
			
			dbc.addAllUsers(answer);
			break;
		default:
			break;
		}

		return answer;
	}


	/**
	 * copys an existing packet.
	 * @param querry
	 * @return
	 */
	private Packet copyPacket(Packet querry ) {
		Packet p = new Packet(querry.getUsername(), querry.getPassword());
		p.setSocket(querry.getSocket());
		p.setPacketType(querry.getPacketType());
		p.setSelectedLevel(querry.getSelectedLevel());
		p.setSelectedTopic(querry.getSelectedTopic());
		p.setSelectedAnswers(querry.getSelectedAnswers());
		p.setQuestion(querry.getQuestion());
		p.setWasRight(querry.getWasRight());
		if(querry.getImage() != null) {
			p.setImage(querry.getImage());
		}
		return p;
	}

}
