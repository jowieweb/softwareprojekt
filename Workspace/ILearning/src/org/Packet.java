package org;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * The class Packet represents a packet. It includes a type, login information, etc.
 */
public class Packet implements Serializable {
	private Socket client;
	private String username;
	private String password;
	private String frage;
	private String answer[];
	private String selectedTopic="";
	private String selectedLevel="";
	private String[] topics;
	private String[] level;

	public enum Type {
		UNUSED, CATEGORY, EDIT_QUESTION, ANSWER_QUESTION, LOGIN, USER_MANAGEMENT
	};

	public enum Login {
		FAIL, USER, ADMIN
	}

	private Type type = Type.UNUSED;
	private Login login = Login.FAIL;

	/**
	 * Constructor builds new packet. 
	 * @param username the username
	 * @param password the password (MD5)
	 */
	public Packet(String username, String password) {
		if (username == null || password == null) {
			password = "";
			username = "";
		}
		this.username = username;
		this.password = password;
	}

	public void setSocket(Socket s) {
		client = s;
	}

	public Socket getSocket() {
		return client;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public Login getLoginStatus() {
		return login;
	}

	public String getFrage() {
		return frage;
	}

	public void setFrage(String p) {
		if (p != null) {
			frage = p;
		}
	}
	public void setAnswers(String[] p) {
		if(p!=null) {
			answer = p;
		}
	}
	
	public String[] getAnswers() {
		return answer;
	}

	public void setTopics(String[] para) {
		if (para != null) {
			this.topics = para;
		}
	}

	public String[] getTopics() {
		return this.topics;
	}

	public void setLevel(String[] lvl) {
		if (lvl != null) {
			level = lvl;
		}
	}

	public String[] getLevel() {
		return level;
	}

	public String getSelectedTopic() {
		return selectedTopic;
	}

	public void setSelectedTopic(String topic) {
		if (topic != null) {
			selectedTopic = topic;
		}
	}

	public void setSelectedLevel(String lvl) {
		if(lvl != null) {
			selectedLevel = lvl;
		}
	}
	
	public String getSelectedLevel() {
		return selectedLevel;
	}
	
	public Type getPacketType() {
		return type;
	}
	
	public void setPacketType(Type t) {
		type = t;
	}
}
