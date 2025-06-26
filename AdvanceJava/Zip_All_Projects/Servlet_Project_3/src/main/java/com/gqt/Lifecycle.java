package com.gqt;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



	@WebServlet("/Lifecycle")
	public class Lifecycle extends HttpServlet{
		
		@Override
		public void init() throws ServletException {
			System.out.println("inside init().");
		}
		
		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			System.out.println("inside the service().");
			String name = req.getParameter("name");
			PrintWriter writer=resp.getWriter();
			writer.println(name);
		}
		
		@Override
		public void destroy() {
			System.out.println("inside destroy().");
		}
	}