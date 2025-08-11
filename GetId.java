package java_server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetId {
	static int id() throws ClassNotFoundException, SQLException {
		int lastId = 0; // Default ID if no records exist

		// Establish a connection
		try (Connection con = GetConnection.makeConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT MAX(IdNo) AS LastId FROM Employee")) {

			// Retrieve the last ID
			if (rs.next())
				lastId = rs.getInt("LastId");
		}
		return lastId+1;
	}
	static int billNo() throws ClassNotFoundException, SQLException {
		int lastId = 0; // Default ID if no records exist

		// Establish a connection
		try (Connection con = GetConnection.makeConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT MAX(BillNo) AS LastId FROM Bills")) {

			// Retrieve the last ID
			if (rs.next())
				lastId = rs.getInt("LastId");
		}
		return lastId;
	}
}
