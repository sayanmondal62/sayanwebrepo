package java_server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("userid");
		String pass=request.getParameter("pass");
		String job=request.getParameter("job");
		CheckForStock.checkMedeicine();
		try {
			int status=CheckValidity.validity(id, pass, job);
			if(status==0) {
				RequestDispatcher rd=request.getRequestDispatcher("UserNotFound.html");
				rd.forward(request, response);
			}
			else if(status==1) {
				RequestDispatcher rd=request.getRequestDispatcher("Admin.html");
				rd.forward(request, response);
			}
			else if(status==2) {
				RequestDispatcher rd=request.getRequestDispatcher("UserDashBoard.jsp");
				rd.forward(request, response);
			}
			else if(status==3) {
				RequestDispatcher rd=request.getRequestDispatcher("WrongJob.html");
				rd.forward(request, response);
			}
			else {
				RequestDispatcher rd=request.getRequestDispatcher("WrongPassword.html");
				rd.forward(request, response);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
