package org.Server;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.Packet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {

	DBConnectorListener DBCL;
	private java.sql.Connection connect = null;
	private int rekCount = 0;
	public DBConnector(DBConnectorListener l) {
		DBCL = l;
		connect();		
	}
	
	/**
	 * starts the connection to the mysql server
	 */
	private void connect(){
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

	/**
	 * places all Querrys to the DB
	 * @param p 
	 */
	public void placeQuerry(Packet p) {
		
		rekCount=0;
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

	/**
	 * adds the categorie to the packet
	 * @param p
	 */
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

	/**
	 * adds all level form the DB to the packet.
	 * @param p
	 */
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

	/**
	 * sets a question text to the packet.
	 * @param p
	 */
	private void setFrage(Packet p) {
		try {
			ResultSet resultSet = connect
					.createStatement()
					.executeQuery(
							"select questiontext, answer1, answer2, answer3, answer4, image from Topic join Question_Topic on Topic.id = Question_Topic.topic_id join Question on Question.id = Question_Topic.question_id where Topic.title = '"
									+ p.getSelectedTopic() + "' ORDER BY RAND()");
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
				if(imageurl.length()> 1){					
					setImage(p,imageurl);
				}else{
					setImage(p,"url.jpg");
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
	 * @param p
	 * @param url the URL 
	 */
	private void setImage(Packet p, String url){
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
	 * @param p
	 * @return Login.FAIL = wrong, Login.USER = user, Login.ADMIN = admin
	 */
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
			connect();
			if(rekCount > 2){
				rekCount++;
				return checkLogin(p);				
			} 
			e.printStackTrace();
			return Packet.Login.FAIL;
		}
		return Packet.Login.FAIL;
	}

}
