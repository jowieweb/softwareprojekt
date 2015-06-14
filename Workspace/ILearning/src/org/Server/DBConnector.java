package org.Server;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.Packet;
import org.Packet.Login;
import org.SQLQuerries;

import java.sql.PreparedStatement;

/**
 * The DBConnector class retrieves data from the database.
 */
public class DBConnector {
	private java.sql.Connection connect = null;
	private int rekCount = 0;

	/**
	 * constructor invokes connect().
	 */
	public DBConnector() {
		connect();
	}

	/**
	 * starts the connection to the mysql-server
	 */
	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://j.z5n.de/ilearning?user=kuser&password=qwertzuiop");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the answer from a question from the database and checks the
	 * given answer.
	 *
	 * @param packet
	 * a Packet that contains the user's answer.
	 */
	public void checkAnswers(Packet packet) {
		if (packet.getSelectedAnswers() != null) {
			try {
				PreparedStatement stm  =  SQLQuerries.getCheckAnswer(connect);
				stm.setString(1, packet.getQuestion());
				ResultSet result = stm.executeQuery();
				while (result.next()) {
					String sol = result.getString("solution");
					boolean right = false;
					if(sol.length() > 1){
						// more than 1 answer is correct
						right = true;
						String all[] = sol.split("");
						int[] allselected = packet.getSelectedAnswers();
						
							for(int i = 0;i< all.length;i++)
							{							
								// db answers are based on 1, packets are based on 0
								int index = Integer.parseInt(all[i]) -1;
								if(allselected[index] == 0){
									right = false;
								} else{ 
									allselected[index] = 0;
								}
							}
							for(int i =0;i<allselected.length;i++) {
								if(allselected[i] == 1)
								{
									right = false;
								}
							}
						
					}else {
						// only 1 answer is correct
						int intsol = Integer.parseInt(sol) - 1;						
						if (packet.getSelectedAnswers()[intsol] == 1) {
							right = true;
						}

						for (int i = 0; i < 4; i++) {
							if (packet.getSelectedAnswers()[i] == 1 && i != intsol) {
								right = false;
							}
						}
					}
					
					updateCheckedAnswer(packet, right);
					packet.setWasRight(right);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates the user_data Table in DB
	 * @param packet including all question data
	 * @param wasRight answer was right
	 */
	public void updateCheckedAnswer(Packet packet, boolean wasRight) {
		String debug = "";
		String incorrect = "1";
		if (wasRight) {
			incorrect = "0";
		}

		try {
			PreparedStatement stm = SQLQuerries.getCountForCheck(connect);
			stm.setString(1, packet.getQuestion());
			stm.setString(2, packet.getUsername());
			ResultSet result = stm.executeQuery();
			int count = 0;
			while (result.next()) {
				count = result.getInt(1);
			}

			if (count == 1) {
				 stm = SQLQuerries.get1ForCheck(connect,false);
				 stm.setString(1, incorrect);
				 stm.setString(2, packet.getQuestion());
				 stm.setString(3,packet.getUsername());
				 
				

			} else if (count == 0) {
				 stm = SQLQuerries.get2ForCheck(connect,false);
				 stm.setString(1, packet.getUsername());
				 stm.setString(2, packet.getQuestion());
				 stm.setString(3, incorrect);
			}
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * adds the category to the packet
	 *
	 * @param packet
	 */
	public void addCategories(Packet packet) {
		try {
			PreparedStatement stm = SQLQuerries.addCategories(connect);
			ResultSet resultSet = stm.executeQuery();
			String categorys = "";
			while (resultSet.next()) {
				categorys += resultSet.getString("title") + ";";
			}
			packet.setTopics(categorys.split(";"));
			packet.setPacketType(Packet.Type.CATEGORY);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * adds all levels from the database to the packet.
	 *
	 * @param packet
	 */
	public void addLevel(Packet packet) {
		try {
			PreparedStatement stm = SQLQuerries.addLevel(connect);
			ResultSet resultSet = stm.executeQuery();
			String level = "";
			while (resultSet.next()) {
				level += resultSet.getString("title") + ";";
			}
			packet.setLevel(level.split(";"));
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUserID(String username){
		PreparedStatement stm = SQLQuerries.getUser(connect);
		try {
			stm.setString(1, username);
			ResultSet resultSet = stm.executeQuery();
			resultSet.next();
			return resultSet.getString(1);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}

	/**
	 * sets a question text to the packet.
	 *
	 * @param packet
	 */
	public void setFrage(Packet packet) {
		try {
			String userid = getUserID(packet.getUsername());
			
			PreparedStatement stm = SQLQuerries.getFrage(connect);
			stm.setString(1, packet.getSelectedTopic());
			stm.setString(2, userid);
			stm.setString(3, userid);
			String asd ="";
			ResultSet resultSet = stm.executeQuery();
			
			
			if (resultSet.next()) {
				packet.setQuestion(resultSet.getString("questiontext"));
				
				packet.setQuestionID(resultSet.getString(1));
				
				ArrayList<String> answers = new ArrayList<String>();
				for (int i = 1; i < 5; i++) {
					answers.add(resultSet.getString(("answer" + i)).toString());
				}

				String[] stockArr = new String[answers.size()];
				stockArr = answers.toArray(stockArr);

				packet.setAnswers(stockArr);
				String imageurl = resultSet.getString("image");
				if (imageurl.length() > 1) {
					setImage(packet, imageurl);
				} else {
					setImage(packet, "");
				}
				String videourl = resultSet.getString("video");
				String audiourl = resultSet.getString("audio");
				if(videourl.length()>0){
					packet.setMediaURL(videourl);
				} else { packet.setMediaURL(audiourl);}
				
			} else {
				System.out.println("EMPTY");
			}
			resultSet.close();
			packet.setPacketType(Packet.Type.ANSWER_QUESTION);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * adds an image to the packet
	 *
	 * @param packet
	 * @param url the URL
	 */
	private void setImage(Packet packet, String url) {
		if(url.length()>0){
			try {
				Image image = ImageIO.read(new File(url));
				packet.setImage(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * checks the login, if wrong Login.FAIL
	 *
	 * @param username the username
	 * @param password the password
	 * @return Login.FAIL = wrong, Login.USER = user, Login.ADMIN = admin
	 */
	public Packet.Login checkLogin(String username, String password) {
//		dump();
		try {
			PreparedStatement stm = SQLQuerries.getLogin(connect);
			stm.setString(1, username);
			stm.setString(2, password);
//			ResultSet resultSet = connect.createStatement().executeQuery(
//					"select name, admin from User where name = '" + username
//					+ "' and password = '" + password + "'");

			ResultSet resultSet = stm.executeQuery();
			while (resultSet.next()) {
				String user = resultSet.getString("name");
				String admin = resultSet.getString("admin");
				System.out.println("User: " + user);
				System.out.println("admin:" + admin);
				if (admin.equals("1")) {
					return Packet.Login.ADMIN;
				} else {
					return Packet.Login.USER;
				}
			}

		} catch (SQLException e) {
			connect();
			if (rekCount > 2) {
				rekCount++;
				return checkLogin(username, password);
			}
			e.printStackTrace();
			return Packet.Login.FAIL;
		}
		return Packet.Login.FAIL;
	}

	/**
	 * Gets all users from the database and packs them into the packet.
	 * @param packet packet
	 */
	public void addAllUsers(Packet packet) {
		if (checkLogin(packet.getUsername(), packet.getPassword()) == Login.ADMIN) {
//			String debug = "SELECT * from `User`";
			
			try {
				ResultSet resultSet = SQLQuerries.addAllUsers(connect).executeQuery();
				while (resultSet.next()) {
					String toAdd[] = new String[3];
					toAdd[0] = resultSet.getString("id");
					toAdd[1] = resultSet.getString("name");
					toAdd[2] = resultSet.getString("password");
					packet.addUsersToUserList(toAdd);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * updates the user table in the DB
	 * @param p
	 */
	public void changeUser(Packet p){
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			//String debug = "update `User` set name = '" + p.getAnswers()[1] + "', password ='" + p.getAnswers()[2] + "' where id = " + p.getAnswers()[0];
			
			try{
				PreparedStatement stm = SQLQuerries.changeUser(connect);
				stm.setString(1, p.getAnswers()[1] );
				stm.setString(2, p.getAnswers()[2] );
				stm.setString(3, p.getAnswers()[0] );
				
				stm.execute();
			
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Removes a user from the database.
	 * @param packet packet
	 */
	public void removeUser(Packet packet){
		if (checkLogin(packet.getUsername(), packet.getPassword()) == Login.ADMIN) {
//			String debug = "DELETE FROM `User` WHERE ((`name` = '" +  packet.getQuestion() + "'));";
			
			try{
				PreparedStatement stm = SQLQuerries.removeUser(connect);
				stm.setString(1,  packet.getQuestion());
				stm.execute();
			
			} catch(SQLException e){}
		}
	}
	
	/**
	 * Creates a new user 
	 * @param p
	 */
	public void addUser(Packet p)
	{
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			if(p.getAnswers() != null && p.getAnswers().length == 2)
			{
//				String debug = "Insert into `User`(name, password, surname, email) VALUES('" + p.getAnswers()[0] + "','" + p.getAnswers()[1] + "',' ',' ')";
				try{
					PreparedStatement stm = SQLQuerries.addUser(connect);
					stm.setString(1,  p.getAnswers()[0]);
					stm.setString(2,  p.getAnswers()[1]);
					stm.execute();
				
				}catch(SQLException e){
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * Updates a given question
	 * @param p
	 */
	public void updateQuestion(Packet p){
		if(p.getQuestionID().length()==0){
			return;
		}
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			String mediatype = "";
			if(p.getMediaURL().endsWith(".jpg")){
				mediatype = " image='" + p.getMediaURL()+ "'";
			} else if(p.getMediaURL().endsWith(".mp4")){
				mediatype = " video='" + p.getMediaURL()+ "'";
			}else if(p.getMediaURL().endsWith(".wav")){
				mediatype = " audio='" + p.getMediaURL() + "'";
			}
			
			String solution = "";
			for(int i = 0;i<p.getSelectedAnswers().length;i++)
			{
				if(p.getSelectedAnswers()[i]==1)
				{
					solution+=(i+1);					
				}
			}
			
//			String debug = "update `Question` set questiontext = '"
//					+ p.getQuestion() + "', solution ='" + solution + "', answer1 ='" + p.getAnswers()[0]
//					+ "', answer2 ='" + p.getAnswers()[1] + "', answer3 ='"
//					+ p.getAnswers()[2] + "', answer4 ='" + p.getAnswers()[3]
//					+ mediatype 
//					+ " where id = " + p.getQuestionID();
		try{
			PreparedStatement stm = SQLQuerries.udpateQuestion(connect, mediatype);
			stm.setString(1, p.getQuestion());
			stm.setString(2, solution);
			stm.setString(3,  p.getAnswers()[0]);
			stm.setString(4,  p.getAnswers()[1]);
			stm.setString(5,  p.getAnswers()[2]);
			stm.setString(6,  p.getAnswers()[3]);
			stm.setString(7,  p.getQuestionID());
			
			stm.execute();
			
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * addes the highscore to the packet
	 * @param p
	 */
	public void setHighScore(Packet p) {
		//String debug = "select (sum(overallCount) - sum(falseCount))* level_value, User.`name` from Question_data join Question on QuestionID = Question.id join `User` on `User`.id = Question_data.UserID GROUP BY UserID";
		try {
			ResultSet resultSet = SQLQuerries.setHighScore(connect).executeQuery();
			while (resultSet.next()) {
				String[] user = new String[2];
				user[0] = resultSet.getString(1);
				user[1] = resultSet.getString(2);
				p.addUserScore(user);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * dumps the whole DB
	 */
	public String dump(){
		// source: http://www.coderanch.com/t/480353/JDBC/databases/MySql-DB-backup-java
		try {
			ResultSet rs = connect.createStatement().executeQuery(
					"SHOW FULL TABLES WHERE Table_type != 'VIEW'");
			StringBuilder sb = new StringBuilder();
			StringBuilder buff = new StringBuilder();
			while (rs.next()) {
				String tbl = rs.getString(1);

				
				sb.append("DROP TABLE IF EXISTS `").append(tbl).append("`;\n");
				ResultSet rs2 = connect.createStatement().executeQuery(
						"SHOW CREATE TABLE `" + tbl + "`");
				rs2.next();
				String crt = rs2.getString(2) + ";";
				crt = crt.replace("AUTO_INCREMENT", "");
				crt = crt.substring(0,crt.lastIndexOf(")")+1);
				crt = crt.substring(0, crt.indexOf(")", crt.lastIndexOf("PRIMARY KEY"))) + "));";
				sb.append(crt).append(";\n");
				sb.append("\n");
				

				ResultSet rss = connect.createStatement().executeQuery(
						"SELECT * FROM " + tbl);
				while (rss.next()) {
					int colCount = rss.getMetaData().getColumnCount();
					if (colCount > 0) {
						sb.append("INSERT INTO ").append(tbl)
								.append(" VALUES(");

						for (int i = 0; i < colCount; i++) {
							if (i > 0) {
								sb.append(",");
							}
							String s = "";
							try {
								s += "'";
								s += rss.getObject(i + 1).toString();
								s += "'";
							} catch (Exception e) {
								s = "NULL";
							}
							sb.append(s);
						}
						sb.append(");\n");
						buff.append(sb.toString());
						sb = new StringBuilder();
					}
				}
			}
		
			String end = buff.toString();
			return end;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		

	}
		
	
}
