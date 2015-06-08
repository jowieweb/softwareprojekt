package org;

import java.awt.Image;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * The class Packet represents a packet. It includes a type, login information, etc.
 */
public class Packet implements Serializable {
	private static final long serialVersionUID = -5954611261930673901L;
	private Socket client;
	private String username;
	private String password;
	private String frage;
	private String answer[];
	private String selectedTopic="";
	private String selectedLevel="";
	private String[] topics;
	private String[] level;
	private ImageIcon image;
	private int[] selectedAnswer;
	private boolean wasRight;
	private ArrayList<String[]> users;

	public enum Type {
		UNUSED, CATEGORY, EDIT_QUESTION, ANSWER_QUESTION, USER_MANAGEMENT
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
		this.image = null;
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
	
	public void setLoginStatus(Login status) {
		login = status;
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
	
	public void setImage(Image i){
		
		image =new ImageIcon(i);
	}
	public Image getImage(){
		if(image != null){
			return image.getImage();
		}
		return null;
	}
	public void setSelectedAnswer(int[] para)
	{
		selectedAnswer = para;
	}
	
	public int[] getSelectedAnswer(){
		return selectedAnswer;
	}
	
	public boolean getWasRight(){
		return wasRight;
	}
	
	public void setWasRight(boolean w)
	{
		wasRight = w;
	}
	
	public void addUserToUserList(String[] s){
		if(users== null){
			users = new ArrayList<String[]>();
		}
		if(s != null && s.length ==3){
			users.add(s);
		}
	}
	
	public ArrayList<String[]> getUsers(){
		return users;
	}
}
