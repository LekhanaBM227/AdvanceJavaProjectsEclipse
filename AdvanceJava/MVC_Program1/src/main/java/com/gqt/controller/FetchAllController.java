//package com.gqt.controller;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.gqt.model.Model;
//
//public class FetchAllController extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("➡️ FetchAllController servlet called");
//
//        resp.setContentType("text/html");
//        PrintWriter out = resp.getWriter();
//
//        // Create Model object to get connection
//        Model model = new Model();
//        Connection con = model.getConnection();  // You need to add this getter method in your Model class
//
//        if (con == null) {
//            out.println("<h3 style='color:red;'>Database connection failed.</h3>");
//            return;
//        }
//
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
//
//            out.println("<h2>Employee Data:</h2>");
//            out.println("<table border='1'>");
//            out.println("<tr><th>ID</th><th>Name</th><th>Company</th><th>Age</th><th>Salary</th><th>Designation</th></tr>");
//
//            while (rs.next()) {
//                out.println("<tr>");
//                out.println("<td>" + rs.getInt("id") + "</td>");
//                out.println("<td>" + rs.getString("name") + "</td>");
//                out.println("<td>" + rs.getString("company") + "</td>");
//                out.println("<td>" + rs.getInt("age") + "</td>");
//                out.println("<td>" + rs.getInt("salary") + "</td>");
//                out.println("<td>" + rs.getString("designation") + "</td>");
//                out.println("</tr>");
//            }
//
//            out.println("</table>");
//
//            rs.close();
//            stmt.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            out.println("<h3 style='color:red;'>Error fetching data.</h3>");
//        }
//    }
//}

package com.gqt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gqt.model.Model;

public class FetchAllController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Debug message to confirm servlet is being hit
		System.out.println("➡️ FetchAllController servlet called");

		// Set response type
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		// Temporary response
		out.println("<h1>Fetching all employee records...</h1>");

		// Create Model object to get connection
		Model model = new Model();
		Connection con = model.getConnection();  // You need to add this getter method in your Model class

		if (con == null) {
			out.println("<h3 style='color:red;'>Database connection failed.</h3>");
			return;
		}

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

			out.println("<h2>Employee Data:</h2>");
			out.println("<table border='1'>");
			out.println("<tr><th>ID</th><th>Name</th><th>Company</th><th>Age</th><th>Salary</th><th>Designation</th></tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("id") + "</td>");
				out.println("<td>" + rs.getString("name") + "</td>");
				out.println("<td>" + rs.getString("company") + "</td>");
				out.println("<td>" + rs.getInt("age") + "</td>");
				out.println("<td>" + rs.getInt("salary") + "</td>");
				out.println("<td>" + rs.getString("designation") + "</td>");
				out.println("</tr>");
			}

			out.println("</table>");
			
			out.println("<a href='index.html'>Back</a>");

			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<h3 style='color:red;'>Error fetching data.</h3>");
		}

	}
}

