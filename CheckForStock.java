package java_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class CheckForStock {
	public static void checkMedeicine() {
		LocalDate localDate = LocalDate.now();
		java.sql.Date date=java.sql.Date.valueOf(localDate);
		try
		{
			Connection conn=GetConnection.makeConnection();
			String sql="Select * from medicine";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				String name=rs.getString("madicine_name");
				int stock=rs.getInt("stock");
				java.sql.Date exp_date=rs.getDate("exp_date");
				if(stock==0)
				{
					addInOutOfStock(name);
					delete(name);
				}
				else if(date.equals(exp_date)||date.after(exp_date)) {
					System.out.println(exp_date);
					addExpMedicine(name, stock);
					delete(name);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void addInOutOfStock(String name) {
		try {
			Connection con=GetConnection.makeConnection();
        	String sql = "INSERT INTO  outofstock(name) VALUES (?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted>0) System.out.println("SuccessFul!!!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addExpMedicine(String name,int stock) {
		try {
			Connection con=GetConnection.makeConnection();
        	String sql = "INSERT INTO  expmedicines(name,stock) VALUES (?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setInt(2, stock);
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted>0) System.out.println("SuccessFul!!!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void delete(String name) {
		try {
			Connection con=GetConnection.makeConnection();
        	String sql = "DELETE FROM medicine WHERE madicine_name = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted>0) System.out.println("SuccessFul!!!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
