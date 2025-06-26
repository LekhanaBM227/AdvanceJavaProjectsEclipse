package com.youtubeclone.dao;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.codec.digest.DigestUtils;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // ... rest of your code remains unchanged

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    String hashedPassword = DigestUtils.sha256Hex(password);

	    PrintWriter out = response.getWriter();
	    response.setContentType("text/html");

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/youtube_clone", "root", "Lek!@#227");

	        PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
	        stmt.setString(1, email);
	        stmt.setString(2, hashedPassword);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            HttpSession session = request.getSession();
	            session.setAttribute("userEmail", email);
	            response.sendRedirect("WelcomeServlet");
	        } else {
	            out.println("<h2>Login failed. Invalid email or password.</h2>");
	        }

	        con.close();
	    } catch (Exception e) {
	        e.printStackTrace(out);
	    }
	}

}

