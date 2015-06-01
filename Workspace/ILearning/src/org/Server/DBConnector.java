package org.Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.Packet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {

	DBConnectorListener DBCL;
	private java.sql.Connection connect = null;

	public DBConnector(DBConnectorListener l) {
		DBCL = l;

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

	public void placeQuerry(Packet p) {

		Packet.Login login = checkLogin(p);
		if (login != Packet.Login.FAIL) {
			String answerString = "";
			Packet answer = PacketBuilder.getPacket(p, answerString, login, Packet.Type.UNUSED);
			// das ist nicht wirklich schön, und um geht total das Factory
			// pattern, aber wir haben im Packet Builder keinen zugriff auf die
			// DB mehr,
			// also müssen wir an dieser Stelle auswerten, was alles in das
			// Packet rein muss.
			if (p.getSelectedTopic().equals("")
					&& p.getSelectedLevel().equals("")) {
				addCategories(answer);
				addLevel(answer);
			} else {
				setFrage(answer);
			}

			DBCL.DBDataReceive(answer);

		} else {
			DBCL.loginFailed(p);
		}
	}

	private void addCategories(Packet p) {
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

	private void addLevel(Packet p) {
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

	private void setFrage(Packet p) {
		try {
			ResultSet resultSet = connect
					.createStatement()
					.executeQuery(
							"select questiontext, answer1, answer2, answer3, answer4 from Topic join Question_Topic on Topic.id = Question_Topic.topic_id join Question on Question.id = Question_Topic.question_id where Topic.title = '"
									+ p.getSelectedTopic() + "'");
			if (resultSet.next()) {
				p.setFrage(resultSet.getString("questiontext"));

				ArrayList<String> answers = new ArrayList<String>();
				for (int i = 1; i < 5; i++) {
					answers.add(resultSet.getString(("answer" + i)).toString());
				}
				
				String[] stockArr = new String[answers.size()];
				stockArr = answers.toArray(stockArr);
				
				p.setAnswers(stockArr);
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

	private Packet.Login checkLogin(Packet p) {
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"select name, admin from User where name = '"
							+ p.getUsername() + "' and password = '"
							+ p.getPassword() + "'");

			while (resultSet.next()) {
				String user = resultSet.getString("name");
				String admin = resultSet.getString("admin");
				System.out.println("User: " + user);
				System.out.println("admin:" + admin);
				if(admin.equals("admin")){
					return Packet.Login.ADMIN;					
				}
				else{
					return Packet.Login.USER;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Packet.Login.FAIL;
		}
		return Packet.Login.FAIL;
	}

}
