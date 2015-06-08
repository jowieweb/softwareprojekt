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
	 * Retrieves the answer from a question from the database and checks the given answer. 
	 * @param p a Packet that contains the user's answer.
	 */
	public void checkAnswers(Packet p) {
		if (p.getSelectedAnswer() != null) {
			try {
				ResultSet result = connect.createStatement().executeQuery(
						"select solution from Question where Question.questiontext ='"
								+ p.getFrage() + "'");
				while (result.next()) {
					String sol = result.getString("solution");
					int intsol = Integer.parseInt(sol) - 1;
					boolean right = false;
					if (p.getSelectedAnswer()[intsol] == 1) {
						right = true;
					}
					
					for (int i = 0; i < 4; i++) {
						if (p.getSelectedAnswer()[i] == 1 && i != intsol) {
							right = false;
						}
					}
					updateCheckedAnswer(p, right);
					p.setWasRight(right);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param p
	 * @param wasRight
	 */
	public void updateCheckedAnswer(Packet p, boolean wasRight) {
		String debug = "";
		String correct = "0";
		String incorrect = "1";
		if (wasRight) {
			incorrect = "0";
			correct = "1";
		}

		try {
			debug = "select count(*) from User_data where User_data.questionid ="
					+ " (select id from Question where questiontext = '"
					+ p.getFrage()
					+ "') and User_data.userid = (select `User`.id from"
					+ " `User` where `User`.`name` = '"
					+ p.getUsername() + "')";

			ResultSet result = connect.createStatement().executeQuery(debug);
			int count = 0;
			while (result.next()) {
				count = result.getInt(1);
			}

			if (count == 1) {
				debug = "update User_data set falseCount= falsecount + "
						+ incorrect
						+ ", lastAnswered = now(), overallCount = overallCount +1,"
						+ " correctAnswered = correctAnswered + "
						+ correct
						+ " where User_data.questionid = (select id from Question"
						+ " where questiontext = '"
						+ p.getFrage()
						+ "') and User_data.userid = (select `User`.id from `User`"
						+ " where `User`.`name` = '"
						+ p.getUsername() + "')";

			} else if (count == 0) {
				debug = "insert into User_data(userid,questionid,falseCount,lastAnswered,"
						+ "overallCount,correctAnswered) values((select `User`.id from"
						+ " `User` where `User`.`name` = '"
						+ p.getUsername()
						+ "') , (select id from Question where questiontext = '"
						+ p.getFrage()
						+ "'),"
						+ incorrect
						+ ",now(),"
						+ correct + ",1)";
			}
			connect.createStatement().execute(debug);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * adds the category to the packet
	 * 
	 * @param p
	 */
	public void addCategories(Packet p) {
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"select title from Topic");
			String categorys = "";
			while (resultSet.next()) {
				categorys += resultSet.getString("title") + ";";
			}
			p.setTopics(categorys.split(";"));
			p.setPacketType(Packet.Type.CATEGORY);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * adds all levels from the database to the packet.
	 * 
	 * @param p
	 */
	public void addLevel(Packet p) {
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"SELECT title FROM Level");
			String level = "";
			while (resultSet.next()) {
				level += resultSet.getString("title") + ";";
			}
			p.setLevel(level.split(";"));
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sets a question text to the packet.
	 * 
	 * @param p
	 */
	public void setFrage(Packet p) {
		try {
			ResultSet resultSet = connect
					.createStatement()
					.executeQuery(
							"select questiontext, answer1, answer2, answer3, answer4, image"
									+ " from Topic join Question_Topic on Topic.id ="
									+ " Question_Topic.topic_id join Question on Question.id"
									+ " = Question_Topic.question_id where Topic.title = '"
									+ p.getSelectedTopic()
									+ "' ORDER BY RAND()");
			if (resultSet.next()) {
				p.setFrage(resultSet.getString("questiontext"));

				ArrayList<String> answers = new ArrayList<String>();
				for (int i = 1; i < 5; i++) {
					answers.add(resultSet.getString(("answer" + i)).toString());
				}

				String[] stockArr = new String[answers.size()];
				stockArr = answers.toArray(stockArr);

				p.setAnswers(stockArr);
				String imageurl = resultSet.getString("image");
				if (imageurl.length() > 1) {
					setImage(p, imageurl);
				} else {
					setImage(p, "url.jpg");
				}
			} else {
				System.out.println("EMPTY");
			}
			resultSet.close();
			p.setPacketType(Packet.Type.ANSWER_QUESTION);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * adds an image to the packet
	 * 
	 * @param p
	 * @param url the URL
	 */
	private void setImage(Packet p, String url) {
		try {
			Image image = ImageIO.read(new File(url));
			p.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
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
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"select name, admin from User where name = '" + username
							+ "' and password = '" + password + "'");

			while (resultSet.next()) {
				String user = resultSet.getString("name");
				String admin = resultSet.getString("admin");
				System.out.println("User: " + user);
				System.out.println("admin:" + admin);
				if (admin.equals("admin")) {
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

}
