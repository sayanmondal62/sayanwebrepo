package java_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Updatemedicinetable {
static void update(String name,String quantity) throws ClassNotFoundException, SQLException {
		
		int q=Integer.parseInt(quantity);
		Connection con=GetConnection.makeConnection();
        String quarry ="SELECT * FROM medicine where madicine_name=?";
        PreparedStatement stmt=con.prepareStatement(quarry);
        stmt.setString(1,name);
        ResultSet rs=stmt.executeQuery();
        if(rs.next())
        {	
        	int stock=rs.getInt("stock");
        	String update="UPDATE medicine SET stock=? where madicine_name=?";
    		PreparedStatement pstmt = con.prepareStatement(update);
    		pstmt.setLong(1,(stock-q));
    		pstmt.setString(2,name);
    		pstmt.executeUpdate();
        }
        con.close();
	}
}
