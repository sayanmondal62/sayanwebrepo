package java_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java_server.GetConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FetchDataServlet")
public class FetchDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dateStr = request.getParameter("date");
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	        try (Connection con = GetConnection.makeConnection();
	             PreparedStatement ps = con.prepareStatement("SELECT * FROM Bills WHERE BuyingDate = ?")) {
	        	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	              java.util.Date utilDate = sdf.parse(dateStr);
	              
	              // Convert java.util.Date to java.sql.Date
	              java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	        	
	        	ps.setDate(1, sqlDate);
	            ResultSet rs = ps.executeQuery();

	            if (!rs.isBeforeFirst()) {
	                out.println("<tr><td colspan='2'>No data found for this date</td></tr>");
	            }
	            
	            double total=0.0;
	            while (rs.next()) {
	                String[] medicineNames = rs.getString("MedicineNames").split(",");
	                String[] quantities = rs.getString("Quantities").split(",");
	                String[] amounts = rs.getString("Amount").split(",");

	                double totalAmount = 0;
	                for (String amount : amounts) {
	                    totalAmount += Double.parseDouble(amount);
	                }
	                total+=totalAmount;
	                out.println("<tr>");
	                out.println("<td>" + rs.getString("BillNo") + "</td>");
	                out.println("<td>" + rs.getString("BuyerName") + "<div class='tooltip'><h4>Medicine Details</h4><ul>");
	                for (int i = 0; i < medicineNames.length; i++) {
	                    out.println("<li>" + medicineNames[i] + " - " + quantities[i] + " units - Rs" + amounts[i] + "</li>");
	                }
	                out.println("</ul><p><strong>Total Amount: Rs" + totalAmount + "</strong></p></div></td>");
	                out.println("</tr>");
	            }
	            out.println("<h3>Total Transaction="+total+"</h3>");
	    } catch (Exception e) {
	        e.printStackTrace();
	        out.println("<tr><td colspan='2'>Error fetching data</td></tr>");
	    }
	}

}
