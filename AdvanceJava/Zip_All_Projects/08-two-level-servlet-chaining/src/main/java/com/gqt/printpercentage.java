package com.gqt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/printpercentage")
public class printpercentage extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		try {
			 HttpSession hs=request.getSession();
			 int a=(int) hs.getAttribute("marks1");
			 int b=(int) hs.getAttribute("marks2");
			 int c=(int) hs.getAttribute("marks3");
			 float percent=(a+b+c)/3.0f;
			 pw.print( "percentage="+percent);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
