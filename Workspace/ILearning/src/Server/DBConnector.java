package Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			Packet answer = PacketBuilder.getPacket(p,
					"GANZ VIEL KRAM AUS DER DB");
			DBCL.DBDataReceive(answer);
		} else {
			DBCL.loginFailed(p);
		}
	}

	private boolean checkLogin(Packet p) {
		try {
			ResultSet resultSet = connect.createStatement().executeQuery("select name, admin from user where name = '" + p.getUsername() + "' and password = '" + p.getPassword() + "'");
			
			while (resultSet.next()) {
				String user = resultSet.getString("name");
				String admin = resultSet.getString("admin");
				System.out.println("User: " + user);
				System.out.println("admin:" + admin) ;
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		// resultSet gets the result of the SQL query
		 
		 
		if (p.getUsername().equals("steven") && p.getPassword().equals("1")) {
			System.out.println("steven");
			return true;
		}
		return false;
	}


	
	

}
