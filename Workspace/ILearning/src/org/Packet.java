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
	private Socket socket;
	private String username;
	private String password;
	private String question;
	private String answer[];
	private String selectedTopic="";
	private String selectedLevel="";
	private String[] topics;
	private String[] level;
	private String questionID = "";
	private ImageIcon image;
	private int[] selectedAnswer;
	private boolean wasRight;
	
	private ArrayList<String[]> users;

	/**
	 * Type for specifying the packet type.
	 */
	public enum Type {
		UNUSED, CATEGORY, EDIT_QUESTION, ANSWER_QUESTION, USER_MANAGEMENT
	};
	
	public enum Management_Type {
		UNUSED, ADD_USER, REMOVE_USER, CHANGE_USER
	}
	
	public enum Edit_Question_Type {
		UNUSED, UPDATE_QUESTION, ADD_QUESTION, REMOVE_QUESTION
	}
	

	/**
	 * Login status with permissions.
	 */
	public enum Login {
		FAIL, USER, ADMIN
	}

	private Type type = Type.UNUSED;
	private Login login = Login.FAIL;
	private Management_Type m_type = Management_Type.UNUSED;
	private Edit_Question_Type eq_type = Edit_Question_Type.UNUSED;

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

	/**
	 * Sets the socket for the packet.
	 * @param socket socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Returns the socket.
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Returns the password of the user.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the username of the user.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the login status (e.g. FAIL, USER or ADMIN) from the packet.
	 * @return login status
	 */
	public Login getLoginStatus() {
		return login;
	}

	/**
	 * Sets the login Status for a packet.
	 * @param status login status (FAIL, USER or ADMIN)
	 */
	public void setLoginStatus(Login status) {
		login = status;
	}

	/**
	 * Returns the question.
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Sets the question for a packet.
	 * @param question the question text
	 */
	public void setQuestion(String question) {
		if (question != null) {
			this.question = question;
		}
	}
	
	/***
	 * Sets the QuestionID
	 * @param id the id of the question
	 */
	public void setQuestionID(String id){
		if(id.length()>=1){
			questionID = id;
		}
	}
	
	/***
	 * returns the QuestionID
	 * @return the QuestionID
	 */
	public String getQuestionID()
	{
		return questionID;
	}
	
	/**
	 * Sets the answers for a packet.
	 * @param answers available answers
	 */
	public void setAnswers(String[] answers) {
		if(answers != null) {
			answer = answers;
		}
	}

	/**
	 * Returns the answers.
	 * @return answers
	 */
	public String[] getAnswers() {
		return answer;
	}

	/**
	 * Sets the topics for a packet. Used by CategoryPanel.
	 * @param topics topics available
	 */
	public void setTopics(String[] topics) {
		if (topics != null) {
			this.topics = topics;
		}
	}

	/**
	 * Returns all available topics.
	 * @return topics
	 */
	public String[] getTopics() {
		return this.topics;
	}

	/**
	 * Sets all available levels for a packet. Used by CategoryPanel.
	 * @param level levels to be added
	 */
	public void setLevel(String[] level) {
		if (level != null) {
			this.level = level;
		}
	}

	/**
	 * Returns all available levels.
	 * @return levels
	 */
	public String[] getLevel() {
		return level;
	}

	/**
	 * Returns the selected topic.
	 * @return selected topic
	 */
	public String getSelectedTopic() {
		return selectedTopic;
	}

	/**
	 * Sets the selected topic for a packet.
	 * @param topic the selected topic
	 */
	public void setSelectedTopic(String topic) {
		if (topic != null) {
			selectedTopic = topic;
		}
	}

	/**
	 * Sets the selected level for a packet.
	 * @param level the selected level
	 */
	public void setSelectedLevel(String level) {
		if(level != null) {
			selectedLevel = level;
		}
	}

	/**
	 * Returns the selected level.
	 * @return selected level
	 */
	public String getSelectedLevel() {
		return selectedLevel;
	}

	/**
	 * Returns the packet type.
	 * @return packet type
	 */
	public Type getPacketType() {
		return type;
	}

	/**
	 * Sets the packet type for a packet.
	 * @param type packet type
	 */
	public void setPacketType(Type type) {
		this.type = type;
	}

	/**
	 * Sets the image for a packet.
	 * @param image image to be setted
	 */
	public void setImage(Image image){
		this.image = new ImageIcon(image);
	}

	/**
	 * Returns the image.
	 * @return image
	 */
	public Image getImage(){
		if(image != null){
			return image.getImage();
		}
		return null;
	}
	
	/**
	 * Sets the selected answer for a packet.
	 * @param selectedAnswers the selected answers
	 */
	public void setSelectedAnswers(int[] selectedAnswers) {
		this.selectedAnswer = selectedAnswers;
	}

	/**
	 * Returns the selected answers.
	 * @return selected answers
	 */
	public int[] getSelectedAnswers() {
		return selectedAnswer;
	}

	/**
	 * Returns whether the user answered the question correctly.
	 * @return true (correct) or false (incorrect)
	 */
	public boolean getWasRight() {
		return wasRight;
	}

	/**
	 * Sets whether the user answered the question correctly.
	 * @param correct true (correct) or false (incorrect)
	 */
	public void setWasRight(boolean correct) {
		wasRight = correct;
	}

	/**
	 * Adds a user to the user list, which is used by the AdministartionPanel.
	 * @param users user to add
	 */
	public void addUsersToUserList(String[] users){
		if(this.users == null){
			this.users = new ArrayList<String[]>();
		}
		if(users != null && users.length == 3){
			this.users.add(users);
		}
	}

	/**
	 * Returns a list of all available users.
	 * @return users
	 */
	public ArrayList<String[]> getUsers(){
		return users;
	}
	
	/**
	 * sets what type of User Management will be done
	 * @param mtype
	 */
	public void setManagemtType(Management_Type mtype){
		m_type = mtype;
	}
	
	/**
	 * gets what type of user management will be done
	 * @return
	 */
	public Management_Type getManagementType(){
		return m_type;
	}
	
	/**
	 * sets what tye of Edit will be done to a question
	 * @param eqt
	 */
	public void setEditQuestionType(Edit_Question_Type eqt){
		eq_type = eqt;
	}
	
	/**
	 * gets what type of editing will be dine to a question
	 * @return
	 */
	public Edit_Question_Type getEditQuestionType()
	{
		return eq_type;
	}
	
}
