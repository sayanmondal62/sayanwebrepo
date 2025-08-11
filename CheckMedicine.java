package java_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckMedicine {
	public static boolean checkStock(String med_name) {
		boolean st=false;
		try
		{
			Connection con=GetConnection.makeConnection();
			String sql="SELECT * FROM medicine where  madicine_name=?";
			PreparedStatement stmt=con.prepareStatement(sql);
			stmt.setString(1, med_name);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
				st= true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return st;
	}
}
