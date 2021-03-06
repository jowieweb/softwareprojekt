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
					.getConnection("jdbc:mysql://j.z5n.de/ilearning?user=kuser&password=qwertzuiop&connectTimeout=0&socketTimeout=0&autoReconnect=true");

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
					String link = result.getString("link");
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
					//result.close();
					packet.setLink(link);
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
	@SuppressWarnings("resource")
	public void updateCheckedAnswer(Packet packet, boolean wasRight) {
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
			//result.close();
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
	 * Adds all available categories to the packet.
	 *
	 * @param packet
	 */
	public void addCategories(Packet packet) {
		try {
			ArrayList<String[]> categories = new ArrayList<String[]>();
			PreparedStatement stm = SQLQuerries.addCategories(connect);
			ResultSet resultSet = stm.executeQuery();
			while (resultSet.next()) {
				String[] cat = new String[2];
				cat[0] = resultSet.getString("id");
				cat[1] = resultSet.getString("title");
				System.out.println(cat[0] + ": " + cat[1]);
				categories.add(cat);
				//categorys += resultSet.getString("title") + ";";
			}
			//packet.setTopics(categorys.split(";"));
			//resultSet.close();
			packet.setCategories(categories);
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
			//resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the id of a user from the given username.
	 * @param username username of the user
	 * @return id of the user
	 */
	public String getUserID(String username){
		PreparedStatement stm = SQLQuerries.getUser(connect);
		try {
			stm.setString(1, username);
			ResultSet resultSet = stm.executeQuery();
			resultSet.next();
			String a =  resultSet.getString(1);
			//resultSet.close();
			return a;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Sets a question text to the packet.
	 * @param packet
	 */
	public void setFrage(Packet packet) {
		try {
			System.out.println();
			String userid = getUserID(packet.getUsername());
			PreparedStatement stm = null;
			if(packet.getSelectedModus().equals("")){
				stm = SQLQuerries.getQuestion(connect, false);
				System.out.println(packet.getSelectedTopic());
				System.out.println(packet.getSelectedLevel());
				System.out.println(userid);
				stm.setString(1, packet.getSelectedTopic());
				stm.setString(2, packet.getSelectedLevel());
				stm.setString(3, userid);
				stm.setString(4, userid);
			} else if(packet.getSelectedModus().equals("Mixmode")){
				stm = SQLQuerries.getQuestionAllCategories(connect, false);
			} else if(packet.getSelectedModus().equals("Errormode") ){
				stm = SQLQuerries.getWrongQuestion(connect, false);
				stm.setString(1, userid);
			}
			
			
			
			ResultSet resultSet = stm.executeQuery();
			
			resultSet.beforeFirst();
			boolean empty = false;
			//if all questions are answered within 3 minutes, a random will be selecetd
			//check, if the result set contains valid information, if not empty will set to TRUE
			try{
				resultSet.first();
				String tmp = resultSet.getString("questiontext");
				if(tmp == null | tmp.length() == 0){		
					empty = true;
				}
			}catch(Exception e){
				empty = true;
			}
			
			
			// a random question will be selected.
			// also the gotRightQuestion boolean will set to false to show the user that all questions are answered within 3 min.
			if(empty)
			{
				stm = SQLQuerries.getRandomIfEmpty(connect,false);
				stm.setString(1, packet.getSelectedTopic());
				resultSet = stm.executeQuery();
				packet.setGotRightQuestion(false);
			}
					
			
			resultSet.beforeFirst();
			
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
				if(imageurl != null){
					if (imageurl.length() > 1) {
						setImage(packet, imageurl);
					} else {
						setImage(packet, "");
					}
				}
				String videourl = resultSet.getString("video");
				String audiourl = resultSet.getString("audio");
				if(videourl != null){
					if(videourl.length()>0){
						packet.setMediaURL(videourl);
					} else { 
						if(audiourl != null){
							packet.setMediaURL(audiourl);
						}
					}
				}
				
			} else {
				System.out.println("empty again!");
			}
			//resultSet.close();
			packet.setPacketType(Packet.Type.ANSWER_QUESTION);

		} catch (Exception e) {
			e.printStackTrace();
//			System.exit(0);
		}
	}

	/**
	 * Adds an image to the packet
	 *
	 * @param packet
	 * @param url the URL
	 */
	private void setImage(Packet packet, String url) {
		if(url.length() > 0){
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
			//resultSet.close();

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
	public void changeUser(Packet p) {
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {

			try {
				PreparedStatement stm = SQLQuerries.changeUser(connect);
				stm.setString(1, p.getAnswers()[1] );
				stm.setString(2, p.getAnswers()[2] );
				stm.setString(3, p.getAnswers()[0] );
				
				stm.execute();
			
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Removes a user from the database.
	 * @param packet packet
	 */
	public void removeUser(Packet packet) {
		if (checkLogin(packet.getUsername(), packet.getPassword()) == Login.ADMIN) {
			
			try {
				PreparedStatement stm = SQLQuerries.removeUser(connect);
				stm.setString(1,  packet.getQuestion());
				stm.execute();
			
			} catch(SQLException e) {}
		}
	}
	
	/**
	 * Creates a new user 
	 * @param p
	 */
	public void addUser(Packet p) {
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			if(p.getAnswers() != null && p.getAnswers().length == 2) {

				try{
					PreparedStatement stm = SQLQuerries.addUser(connect);
					stm.setString(1,  p.getAnswers()[0]);
					stm.setString(2,  p.getAnswers()[1]);
					stm.execute();
				
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Updates a given question
	 * @param p
	 */
	public void updateQuestion(Packet p) {
		if(p.getQuestionID().length() == 0) {
			return;
		}
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			String mediatype = "";
			if(p.getMediaURL().endsWith(".jpg")) {
				mediatype = " image='" + p.getMediaURL()+ "'";
			} else if(p.getMediaURL().endsWith(".mp4")) {
				mediatype = " video='" + p.getMediaURL()+ "'";
			}else if(p.getMediaURL().endsWith(".wav")) {
				mediatype = " audio='" + p.getMediaURL() + "'";
			}
			
			String solution = "";
			for(int i = 0; i < p.getSelectedAnswers().length; i++) {
				if(p.getSelectedAnswers()[i] == 1){
					solution += (i + 1);					
				}
			}
			
		try {
			PreparedStatement stm = SQLQuerries.udpateQuestion(connect, mediatype);
			stm.setString(1, p.getQuestion());
			stm.setString(2, solution);
			stm.setString(3,  p.getAnswers()[0]);
			stm.setString(4,  p.getAnswers()[1]);
			stm.setString(5,  p.getAnswers()[2]);
			stm.setString(6,  p.getAnswers()[3]);
			stm.setString(7,  p.getQuestionID());
			
			stm.execute();
			
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds the highscore to the packet
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
			//resultSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a category to the database.
	 * @param packet
	 */
	public void addCategory (Packet packet) {
		if (packet == null || packet.getQuestion() == null
				|| packet.getEditCategoryType() != Packet.Edit_Category_Type.ADD_CATEGORY) {
			return;
		}

		try {
			PreparedStatement statement = SQLQuerries.addCategory(connect);
			statement.setString(1, packet.getQuestion());
			statement.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes a category from the database.
	 * @param packet
	 */
	public void removeCategory (Packet packet) {
		if (packet == null || packet.getQuestion() == null
				|| packet.getEditCategoryType() != Packet.Edit_Category_Type.REMOVE_CATEGORY) {
			return;
		}
		
		try {
			PreparedStatement statement = SQLQuerries.removeCategory(connect);
			statement.setString(1, packet.getQuestion());
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates a category in the database.
	 * @param packet
	 */
	public void updateCategory (Packet packet) {
		if (packet == null || packet.getQuestion() == null
				|| packet.getEditCategoryType() != Packet.Edit_Category_Type.UPDATE_CATEGORY) {
			return;
		}
		
		try {
			PreparedStatement statement = SQLQuerries.updateCategory(connect);
			statement.setString(1, packet.getQuestion());	// New category title
			statement.setString(2, packet.getCategoryID());	// Id of category to be updated
			statement.execute();

		} catch (SQLException e) {
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
	
	public void insertQuestion(Packet p){
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			String video = "";
			String image = "";
			String audio = "";
			if(p.getMediaURL().endsWith(".jpg")) {
				image =  p.getMediaURL();
			} else if(p.getMediaURL().endsWith(".mp4")) {
				video = p.getMediaURL();
			}else if(p.getMediaURL().endsWith(".wav")) {
				audio = p.getMediaURL();
			}
			
			String solution = "";
			for(int i = 0; i < p.getSelectedAnswers().length; i++) {
				if(p.getSelectedAnswers()[i] == 1){
					solution += (i + 1);					
				}
			}
			PreparedStatement statement = SQLQuerries.insertQuestion(connect);
			try {
				statement.setString(1,  p.getSelectedTopic() );
				statement.setString(2, "1");
				statement.setString(3, image);
				statement.setString(4, video);
				statement.setString(5, audio);
				statement.setString(6, p.getQuestion());
				statement.setString(7, p.getAnswers()[0]);
				statement.setString(8, p.getAnswers()[1]);
				statement.setString(9, p.getAnswers()[2]);
				statement.setString(10, p.getAnswers()[3]);
				statement.setString(11, solution);
				statement.execute();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void setAllQuestionsFromTopicForPrinting(Packet p){
		PreparedStatement ps = SQLQuerries.getAllQuestions(connect);
		try {
			ps.setString(1, p.getSelectedTopic());
			ResultSet rs = ps.executeQuery();
			ArrayList<Packet> al = new ArrayList<Packet>();			
			while(rs.next()){
				Packet tmp = new Packet("","");
				tmp.setQuestion(rs.getString("questiontext"));
				
				tmp.setQuestionID(rs.getString(1));
				
				ArrayList<String> answers = new ArrayList<String>();
				for (int i = 1; i < 5; i++) {
					answers.add(rs.getString(("answer" + i)).toString());
				}

				String[] stockArr = new String[answers.size()];
				stockArr = answers.toArray(stockArr);

				tmp.setAnswers(stockArr);
				al.add(tmp);
				
			}
			Packet[] fin =new Packet[al.size()];
			for(int i =0;i< al.size();i++){
				fin[i] = al.get(i);
			}
			//rs.close();
			p.setAllPackets(fin);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
