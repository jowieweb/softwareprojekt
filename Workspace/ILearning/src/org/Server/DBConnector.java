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


public class DBConnector {

	private java.sql.Connection connect = null;
	private int rekCount = 0;

	public DBConnector() {
		connect();
	}

	/**
	 * starts the connection to the mysql server
	 */
	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://j.z5n.de/ilearning?user=kuser&password=qwertzuiop");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

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
					updateCheckedAnswer(p);
					p.setWasRight(right);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateCheckedAnswer(Packet p)
	{
		
	}

	/**
	 * adds the categorie to the packet
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * adds all level form the DB to the packet.
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
			// TODO Auto-generated catch block
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
							"select questiontext, answer1, answer2, answer3, answer4, image from Topic join Question_Topic on Topic.id = Question_Topic.topic_id join Question on Question.id = Question_Topic.question_id where Topic.title = '"
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
	 * @param url
	 *            the URL
	 */
	private void setImage(Packet p, String url) {
		try {
			Image image = ImageIO.read(new File(url));
			p.setImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * checks the login if wrong Login.FAIL
	 * 
	 * @param p
	 * @return Login.FAIL = wrong, Login.USER = user, Login.ADMIN = admin
	 */
	public Packet.Login checkLogin(String username, String password) {
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"select name, admin from User where name = '"
							+ username + "' and password = '"
							+ password + "'");

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
			// TODO Auto-generated catch block
			connect();
			if (rekCount > 2) {
				rekCount++;
				return checkLogin(username,password);
			}
			e.printStackTrace();
			return Packet.Login.FAIL;
		}
		return Packet.Login.FAIL;
	}

}
