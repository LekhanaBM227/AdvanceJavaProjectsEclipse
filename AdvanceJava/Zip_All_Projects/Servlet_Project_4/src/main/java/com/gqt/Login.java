package com.gqt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private RequestDispatcher  reqd1;
	private PrintWriter writer;
static int count=3;
private RequestDispatcher  reqd2;
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
String static_password="Test123";
String name=req.getParameter("uname");
String password=req.getParameter("password");
resp.setContentType("text/html");
if(static_password.equals(password)) {
	 reqd1 = req.getRequestDispatcher("/Home");
	 reqd1.forward(req, resp);
}
else {
	writer = resp.getWriter();
	writer.println("Invalid password");
	count--;
	writer.println("Available attempts:"+count);
	if(count>0) {
		 reqd2 = req.getRequestDispatcher("/Login.html");
		 reqd2.include(req, resp);
	}
	else {
		writer.println("No attempts left");
	}
	
}

}
}