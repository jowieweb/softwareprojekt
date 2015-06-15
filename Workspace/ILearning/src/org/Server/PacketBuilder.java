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
	 * receives a answer packet to a given querry
	 * @param querry packet
	 * @return the answer packet
	 */
	public Packet getPacket(Packet querry) {
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
			dbc.setHighScore(answer);
			break;
		case USER_MANAGEMENT:
			switch(querry.getManagementType()){
			case REMOVE_USER:
				if(querry.getQuestion() != null){
					dbc.removeUser(querry);
				}
				break;
			case ADD_USER:
				if(querry.getAnswers() != null){
					dbc.addUser(querry);
				}
				break;
			case CHANGE_USER:
				if(querry.getAnswers() != null){
					dbc.changeUser(querry);
				}
			default:
				break;				
			}
			dbc.addAllUsers(answer);
			break;
		case EDIT_QUESTION: 
			switch(querry.getEditQuestionType()){
			case UPDATE_QUESTION:
				dbc.updateQuestion(querry);
				break;
			case ADD_QUESTION:
				dbc.insertQuestion(querry);
			default:
				break;
			}
			dbc.addCategories(answer);
			break;
		case DUMP_DB:
			String dump = dbc.dump();
			answer.setQuestion(dump);
			answer.setPacketType(Packet.Type.DUMP_DB);
			break;
			
		case EDIT_CATEGORY:
			switch (querry.getEditCategoryType()) {
			case ADD_CATEGORY:
				dbc.addCategory(querry);
				break;
			case UPDATE_CATEGORY:
				dbc.updateCategory(querry);
				break;
			case REMOVE_CATEGORY:
				dbc.removeCategory(querry);
				break;
			default:
					break;
			}
			break;
			
		default:
			break;
		}

		return answer;
	}

	/**
	 * Copys an existing packet.
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
		p.setQuestionID(querry.getQuestionID());
		if(querry.getImage() != null) {
			p.setImage(querry.getImage());
		}
		return p;
	}
}
