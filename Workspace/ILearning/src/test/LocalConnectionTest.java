package test;
/*
 * userverwaltungspackete
 * antwortspiele
 *
 */
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.Packet;
import org.Client.ClientListener;
import org.Client.LocalConnection;
import org.Client.TCPClientException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Files;

/**
 * Tests the local connection class, only functionallity
 * @author Steven Yeates
 */
public class LocalConnectionTest extends LocalConnection {

	/**
	 * Constructor simply calls the superConstructor null, because
	 * testcases aren't able to supply with params
	 * @throw SQLException
	 */
	public LocalConnectionTest() throws SQLException {
		super(null);
	}

	/**
	 * The before copies a givven version of the local
	 * database to compare and opens the standard connection
	 * @throws IOException
	 * @throws SQLException
	 */
	@Before
	public void davor() throws IOException, SQLException {
		File file = new File("localTestStart.db");
		if (file.exists()) {
			File file2 = new File("localdummy.db");
			if (file2.exists()){
				if(file2.delete())
					Files.copy(file.toPath(), file2.toPath());
			}
		}
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localTest.db");
		connect = con.getConnection();
	}
	
	/**
	 * closes the connection !! MUST BE DONE !!
	 * @throws SQLException
	 */
	@After
	public void tearDown() throws SQLException{
		connect.close();
	}

	/**
	 * Tests if the difficulty-levels will be returned
	 */
	@Test
	public void testLoginLevels() {
		listener = new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				assertEquals("leicht", p.getLevel()[0]);
				assertEquals("mittel", p.getLevel()[1]);
				assertEquals("schwer", p.getLevel()[2]);
			}

			@Override
			public void exceptionInClientData(TCPClientException e) { // Darf
																		// nicht
																		// ausgeführt
																		// werden
				fail("This method should not be called!");
			}

		};
		login();
	}

	/**
	 * Test the return of the topics, locally!
	 */
	@Test
	public void testAddCategories() {
		listener = new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				assertEquals("Topic1", p.getCategories().get(0)[1]);
				assertEquals("Topic2", p.getCategories().get(1)[1]);
				assertEquals("Topic3", p.getCategories().get(2)[1]);
				assertEquals("Topic4", p.getCategories().get(3)[1]);
				assertEquals("1", p.getCategories().get(0)[0]);
				assertEquals("2", p.getCategories().get(1)[0]);
				assertEquals("3", p.getCategories().get(2)[0]);
				assertEquals("4", p.getCategories().get(3)[0]);

			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// Darf nicht ausgeführt werden
				fail();
			}

		};
		login();
	}

	/**
	 * Test is to check if a wrong package is not sended!
	 * @throws TCPClientException 
	 * @throws SQLException 
	 * 
	 */
	@Test
	public void sendPacketTest() throws TCPClientException, SQLException {
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localdummy.db");
		connect = con.getConnection();
		listener = new ClientListener() {

			@Override
			public void receiveClientData(Packet p) {
				fail("shoud not be sended!");
			}

			@Override
			public void exceptionInClientData(TCPClientException e) {
				// Darf nicht ausgeführt werden
				fail();
			}

		};
		Packet p = new Packet("klaus", "local");
		p.setQuestion("Frage");
		p.setSelectedAnswers(new int[] { 1, 0, 0, 0 });
		p.setPacketType(Packet.Type.CATEGORY);
		sendPacket(p);
	}

	/**
	 * Checks the update in the database when answer is right
	 * @throws SQLException
	 */
	@Test
	public void updateCheckedAnswerRight() throws SQLException {
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localdummy.db");
		connect = con.getConnection();

		Packet p = new Packet("klaus", "local");
		p.setQuestion("Frage");
		p.setSelectedAnswers(new int[] { 1, 0, 0, 0 });
		updateCheckedAnswer(p, true);
		ResultSet executeQuery = connect.createStatement().executeQuery(
				"select overallCount from question_data");
		assertEquals("22", executeQuery.getString(1));
		executeQuery = connect.createStatement().executeQuery(
				"select falseCount from question_data");
		assertEquals("3", executeQuery.getString(1));
	}

	/**
	 * Checks the update in the database when answer is wrong
	 * @throws SQLException
	 */
	@Test
	public void updateCheckedAnswerWrong() throws SQLException {
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localdummy.db");
		connect = con.getConnection();

		Packet p = new Packet("klaus", "local");
		p.setQuestion("Frage");
		p.setSelectedAnswers(new int[] { 0, 1, 0, 0 });
		updateCheckedAnswer(p, false);
		ResultSet executeQuery = connect.createStatement().executeQuery(
				"select overallCount from question_data");
		assertEquals("22", executeQuery.getString(1));
		executeQuery = connect.createStatement().executeQuery(
				"select falseCount from question_data");
		assertEquals("4", executeQuery.getString(1));
	}

	/**
	 * Check if the answer is tested by the connector the right way!
	 * @throws SQLException
	 */
	@Test
	public void checkAnswersTest() throws SQLException {
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localdummy.db");
		connect = con.getConnection();

		Packet p = new Packet("klaus", "local");
		p.setQuestion("Frage");
		p.setSelectedAnswers(new int[] { 1, 0, 0, 0 });
		checkAnswers(p);
		assertTrue(p.getWasRight());
	}

	/**
	 * simply calls the userid
	 */
	@Test
	public void getUserIdTest() {
		Packet p = new Packet("klaus", "local");
		String userid = getUserID(p.getUsername());
		assertEquals("1", userid);
	}

	/**
	 * tests the setFrage if a right one is returned from the db
	 * @throws SQLException
	 */
	@Test
	public void setFrageTest() throws SQLException {
		con = new SQLiteDataSource();
		con.setUrl("jdbc:sqlite:localdummy.db");
		connect = con.getConnection();

		Packet p = new Packet("klaus", "local");
		p.setSelectedTopic("Topic1");
		setFrage(p);

		assertEquals("Frage", p.getQuestion());
	}
}
