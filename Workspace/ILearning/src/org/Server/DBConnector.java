package org.Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

		if (checkLogin(p)) {
			String answerString = "";
			Packet answer = PacketBuilder.getPacket(p, answerString);
			addCategories(answer);
			addLevel(answer);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void addLevel(Packet p){
		try {
			ResultSet resultSet = connect.createStatement().executeQuery(
					"SELECT title FROM Level");
			String level = "";
			while (resultSet.next()) {
				level += resultSet.getString("title") + ";";
			}
			p.setLevel(level.split(";"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkLogin(Packet p) {
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
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		// resultSet gets the result of the SQL query

		// if (p.getUsername().equals("steven") && p.getPassword().equals("1"))
		// {
		// System.out.println("steven");
		// return true;
		// }
		return false;
	}

}
