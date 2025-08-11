package java_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InsertDatabase {
	public static boolean add_database(String name,int stock,int price,String exp_date) {
		boolean st=false;
		try {
            // Convert String to java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(exp_date);
            
            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            
            try {
            	Connection con=GetConnection.makeConnection();
            	String sql = "INSERT INTO  medicine(madicine_name,stock,price,exp_date) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1,name);
                stmt.setInt(2,stock);
                stmt.setInt(3,price);
                stmt.setDate(4, sqlDate);
                int rowsInserted = stmt.executeUpdate();
                
                if(rowsInserted>0) st=true;
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return st;
	}
}
