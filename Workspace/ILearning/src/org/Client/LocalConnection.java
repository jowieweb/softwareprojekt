package org.Client;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.Packet;
import org.SQLQuerries;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class handles the connection to the local database.
 */
public class LocalConnection extends Client {
	protected SQLiteDataSource con;
	protected Connection connect;
	protected boolean isConnected;

	/**
	 * The constructor establishes the connection to the database.
	 * @param listener
	 */
	public LocalConnection(ClientListener listener) {
		super(listener);
		this.isConnected = false;
		connect();
	}
	/**
	 * Connects to the SQLite-database.
	 */
	private void connect() {
		try {
			SQLiteJDBCLoader.initialize();
			if ((new File("local.db")).exists()) {
				con = new SQLiteDataSource();
				con.setUrl("jdbc:sqlite:local.db");

				connect = con.getConnection();
				this.isConnected = true;
			} else {
				JOptionPane.showMessageDialog(null, "Es gibt keine lokale Datenbank!",
						"Fehler", JOptionPane.WARNING_MESSAGE);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes a login with the local database.
	 */
	public void login() {
		if (this.isConnected) {
			Packet p = new Packet("local", "local");
			p.setLoginStatus(Packet.Login.USER);
			p.setPacketType(Packet.Type.CATEGORY);
			addCategries(p);
			addLevel(p);
			listener.receiveClientData(p);
		}
	}

	/**
	 * Sends a packet.
	 */
	public void sendPacket(Packet packet) throws TCPClientException {
		switch (packet.getPacketType()) {
		case CATEGORY:
			
			break;
		case ANSWER_QUESTION:
			checkAnswers(packet);
			setQuestion(packet);
			
			listener.receiveClientData(packet);
			break;
		default:

			break;
		}

	}

	/**
	 * Adds categories to a packet.
	 * @param packet
	 */
	private void addCategries(Packet packet) {

		try {
			PreparedStatement stm = (PreparedStatement) connect
					.prepareStatement("select * from Topic");
			ResultSet resultSet = stm.executeQuery();
			ArrayList<String[]> categories = new ArrayList<String[]>();

			while (resultSet.next()) {
				String[] cat = new String[2];
				cat[0] = resultSet.getString("id");
				cat[1] = resultSet.getString("title");
				categories.add(cat);
			}
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
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build the local database.
	 * @param packet
	 */
	public void createLocalDatabase(Packet packet) {
		String[] all = packet.getQuestion().split(";");
		for (String s : all) {
			if (s.length() > 2) {
				try {
					connect.createStatement().execute(s);

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			ResultSet executeQuery = connect.createStatement()
					.executeQuery("select * from Topic");
			while (executeQuery.next()) {
				System.out.println(executeQuery.getString(1));
			}
		} catch (Exception e) {
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

			if (count == 1) {
				 stm = SQLQuerries.get1ForCheck(connect,true);
				 stm.setString(1, incorrect);
				 stm.setString(2, packet.getQuestion());
				 stm.setString(3,packet.getUsername());
				 
				

			} else if (count == 0) {
				ResultSet executeQuery = connect.createStatement()
						.executeQuery("select max(id) from Question_data");
				int maxId=99;
				while (executeQuery.next()) {
					maxId = executeQuery.getInt(1);
				}
				maxId ++;
				
				
				 stm = SQLQuerries.get2ForCheck(connect,true);
				 stm.setString(1, ""+maxId);
				 stm.setString(2, packet.getUsername());
				 stm.setString(3, packet.getQuestion());
				 stm.setString(4, incorrect);
			}
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Gets the id to a user specified by an username.
	 * @param username
	 * @return id
	 */
	public String getUserID(String username){
		PreparedStatement stm = SQLQuerries.getUser(connect);
		try {
			stm.setString(1, username);
			ResultSet resultSet = stm.executeQuery();
			resultSet.next();
			return resultSet.getString(1);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Sets the question in a packet.
	 * @param packet
	 */
	public void setQuestion(Packet packet) {
		try {
			String userid = getUserID(packet.getUsername());
			PreparedStatement stm = SQLQuerries.getQuestion(connect,true);
			stm.setString(1, packet.getSelectedTopic());
			stm.setString(2, packet.getSelectedLevel());
			stm.setString(3, userid);
			stm.setString(4, userid);
			
			ResultSet resultSet = stm.executeQuery();
			//if all questions are answerd within 3 minutes, a random will be selecetd
			if(resultSet.next() == false){
				stm = SQLQuerries.getRandomIfEmpty(connect,true);
				stm.setString(1, packet.getSelectedTopic());
				resultSet = stm.executeQuery();
				packet.setGotRightQuestion(false);
			}
			
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
}
