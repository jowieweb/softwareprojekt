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
		if (packet.getSelectedAnswer() != null) {
			try {
				ResultSet result = connect.createStatement().executeQuery(
						"select solution from Question where Question.questiontext ='"
								+ packet.getFrage() + "'");
				while (result.next()) {
					String sol = result.getString("solution");
					int intsol = Integer.parseInt(sol) - 1;
					boolean right = false;
					if (packet.getSelectedAnswer()[intsol] == 1) {
						right = true;
					}

					for (int i = 0; i < 4; i++) {
						if (packet.getSelectedAnswer()[i] == 1 && i != intsol) {
							right = false;
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
	 *
	 * @param packet
	 * @param wasRight
	 */
	public void updateCheckedAnswer(Packet packet, boolean wasRight) {
		String debug = "";
		String incorrect = "1";
		if (wasRight) {
			incorrect = "0";
		}

		try {
			debug = "select count(*) from User_data where User_data.questionid ="
					+ " (select id from Question where questiontext = '"
					+ packet.getFrage()
					+ "') and User_data.userid = (select `User`.id from"
					+ " `User` where `User`.`name` = '"
					+ packet.getUsername()
					+ "')";

			ResultSet result = connect.createStatement().executeQuery(debug);
			int count = 0;
			while (result.next()) {
				count = result.getInt(1);
			}

			if (count == 1) {
				debug = "update Question_data set falseCount= falsecount + "
						+ incorrect
						+ ", lastAnswered = now(), overallCount = overallCount +1"
						+ " where Question_data.QuestionID = (select id from Question where questiontext = '"
						+ packet.getFrage()
						+ "') and Question_data.UserID = (select `User`.id from `User` where `User`.`name` = '"
						+ packet.getUsername() + "')";

			} else if (count == 0) {
				debug = "insert into Question_data(UserID,QuestionID,falseCount,lastAnswered,overallCount) values((select `User`.id from `User` where `User`.`name` = '"
						+ packet.getUsername()
						+ "') , (select id from Question where questiontext = '"
						+ packet.getFrage() + "')," + incorrect + ",now(),1)";
			}
			connect.createStatement().execute(debug);

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
			ResultSet resultSet = connect.createStatement().executeQuery(
					"select title from Topic");
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
			ResultSet resultSet = connect.createStatement().executeQuery(
					"SELECT title FROM Level");
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

	/**
	 * sets a question text to the packet.
	 *
	 * @param packet
	 */
	public void setFrage(Packet packet) {
		try {
			ResultSet resultSet = connect
					.createStatement()
					.executeQuery(
							"select questiontext, answer1, answer2, answer3, answer4, image from Topic join Question on Question.TopicID = Topic.id where Topic.title = '"
									+ packet.getSelectedTopic()
									+ "' ORDER BY RAND()");
			if (resultSet.next()) {
				packet.setFrage(resultSet.getString("questiontext"));

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
					setImage(packet, "url.jpg");
				}
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
		try {
			Image image = ImageIO.read(new File(url));
			packet.setImage(image);
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

	public void addAllUsers(Packet p) {
		if (checkLogin(p.getUsername(), p.getPassword()) == Login.ADMIN) {
			String debug = "SELECT * from `User`";
			try {
				ResultSet resultSet = connect.createStatement().executeQuery(
						debug);
				while (resultSet.next()) {
					String toAdd[] = new String[3];
					toAdd[0] = resultSet.getString("id");
					toAdd[1] = resultSet.getString("name");
					toAdd[2] = resultSet.getString("password");
					p.addUserToUserList(toAdd);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
