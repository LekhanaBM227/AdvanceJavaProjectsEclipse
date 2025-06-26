//package com.gqt.server;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class Program1 extends HttpServlet {
//	private PrintWriter writer;
//
//	@Override
//	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//		String name=req.getParameter("name");
//		String age=req.getParameter("age");
//		String college=req.getParameter("college");
//		String usn=req.getParameter("usn");
//		String sem=req.getParameter("sem");
//		writer = resp.getWriter();
//		writer.println(name);
//		writer.println(age);
//		writer.println(college);
//		writer.println(usn);
//		writer.println(sem);
//	}
//
//}

package com.gqt.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Program2 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        String college = req.getParameter("college");
        String usn = req.getParameter("usn");
        String sem = req.getParameter("sem");

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        writer.println("<h2>Student Details</h2>");
        writer.println("Name: " + name + "<br>");
        writer.println("Age: " + age + "<br>");
        writer.println("College: " + college + "<br>");
        writer.println("USN: " + usn + "<br>");
        writer.println("Semester: " + sem);
    }
}
