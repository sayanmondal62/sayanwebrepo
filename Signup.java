package java_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid=request.getParameter("userid");
		String pass=request.getParameter("password");
		String post=request.getParameter("post");
		
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		try {
			
			Connection con=GetConnection.makeConnection();
			String quary="INSERT INTO employee (IdNo,UserId,Job,Pass) VALUES (?, ?, ?, ?)";
			PreparedStatement stmt=con.prepareStatement(quary);
			
			String idno=Integer.toString(GetId.id());
			stmt.setString(1,idno);
			stmt.setString(2,uid);
			stmt.setString(3, post);
			stmt.setString(4, pass);
			
			int res=stmt.executeUpdate();
			if(res>0){
                out.println("<h3>Sign-Up Sucessfull!</h3>");
            } else {
                out.println("<h3>Error Inserting Record</h3>");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
