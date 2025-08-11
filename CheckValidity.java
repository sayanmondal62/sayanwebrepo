package java_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckValidity {
	static int validity(String id,String pass,String job) throws ClassNotFoundException, SQLException {
		int flag=0;
		if(id==null || pass==null || job==null)  return 0;
		
		Connection con=GetConnection.makeConnection();
		String quary="select * from employee where UserId=?";
		PreparedStatement pt=con.prepareStatement(quary);
		
		pt.setString(1, id);
		ResultSet rs=pt.executeQuery();
		
		if(rs.next())
		{
			String d_pass=rs.getString("Pass");
			String d_job=rs.getString("Job");
			
			if(d_pass.equals(pass) && d_job.equals(job)) {
				if(d_job.equals("admin")) flag=1;
				else flag=2;
			}
			else if(d_pass.equals(pass) && !d_job.equals(job)) {
				flag=3;
			}
			else flag=4;
		}
		con.close();
		return flag;
	}

}
