package java_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FetchMedicineDetails")
public class FetchMedicineDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String medicineName = request.getParameter("medicineName");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			Connection con = GetConnection.makeConnection();
			String sql = "SELECT price, stock, exp_date FROM medicine WHERE madicine_name = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, medicineName);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				java.sql.Date expDate = rs.getDate("exp_date");
				String jsonResponse = String.format("{\"price\": %d, \"stock\": %d, \"exp_date\": \"%s\"}", price,
						stock, expDate);
				out.print(jsonResponse);
			} else {
				String jsonResponse = "{\"price\": 0, \"stock\": 0, \"exp_date\": \"\"}";
				out.print(jsonResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\": \"An error occurred while fetching medicine details.\"}");
		}
		
		out.flush();
	}

}
