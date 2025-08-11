package java_server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/InsertMedicine")
public class InsertMedicine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String num = request.getParameter("stock");
		String price = request.getParameter("price");
		String exp_date = request.getParameter("exp_date");
		boolean st = CheckMedicine.checkStock(name);
		if (st) {
			out.println("<h1 style='color: green; font-family: Arial, sans-serif;'>Medicine is available!</h1>");
			out.println("<button style='background-color: #007bff; color: white; border: none; padding: 10px 20px; font-size: 16px; border-radius: 8px; cursor: pointer;' onclick='window.history.back()'>Go Back</button>");

		} else {
			boolean bt = InsertDatabase.add_database(name, Integer.parseInt(num), Integer.parseInt(price), exp_date);

			if (bt) {
				out.println(
						"<h1 style='color: green; font-family: Arial, sans-serif;'>Medicine inserted successfully!</h1>");
				out.println(
						"<button style='background-color: #007bff; color: white; border: none; padding: 10px 20px; font-size: 16px; border-radius: 8px; cursor: pointer;' onclick='window.history.back()'>Go Back</button>");

			} else {
				out.println(
						"<h1 style='color: green; font-family: Arial, sans-serif;'>Medicine insertion faield!</h1>");
				out.println(
						"<button style='background-color: #007bff; color: white; border: none; padding: 10px 20px; font-size: 16px; border-radius: 8px; cursor: pointer;' onclick='window.history.back()'>Go Back</button>");

			}
		}
	}

}
