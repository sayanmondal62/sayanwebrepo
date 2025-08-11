package java_server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RemoveexpServlet")
public class RemoveexpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name=request.getParameter("name");
		try {
			Connection con=GetConnection.makeConnection();
			String sql = "DELETE FROM expmedicines WHERE name = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted>0) System.out.println("SuccessFul!!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
