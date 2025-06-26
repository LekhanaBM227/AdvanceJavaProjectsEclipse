package com.gqt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/GetResult")
public class GetResult extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String temp=request.getParameter("usn");
			model m= new model();
			m.setUsn(temp);
			m.Getresult();
			//create session
			HttpSession hs=request.getSession(true);
			hs.setAttribute("USN", m.getUsn());
			hs.setAttribute("NAME", m.getName());
			hs.setAttribute("MARKS1", m.getMarks1());
			hs.setAttribute("MARKS2", m.getMarks2());
			hs.setAttribute("MARKS3", m.getMarks3());
			hs.setAttribute("PERCENT", m.getPercent());
			//loaddind disp.jsp
			response.sendRedirect("/10-mvc-arc/disp.jsp");
			//close data base connection
			m.closeConnection();
			}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
