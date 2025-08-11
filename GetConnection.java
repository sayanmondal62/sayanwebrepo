package java_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnection {
	public static Connection makeConnection() throws SQLException, ClassNotFoundException {
		final String URL="jdbc:mysql://localhost:3306/medicineShop";
		final String USER="root";
		final String PASSWORD="root";
		Connection con=DriverManager.getConnection(URL,USER,PASSWORD);
		return con;
	}
}
