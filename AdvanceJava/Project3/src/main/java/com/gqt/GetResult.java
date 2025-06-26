package com.gqt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetResult")
public class GetResult extends HttpServlet {
	String url="jdbc:mysql://localhost:3306/college";
	String UserName="root";
	String password="Lek!@#227";
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet res=null;

	String usn=null;
	String sname=null;
	int marks1=0;
	int marks2=0;
	int marks3=0;
	Float percent=0.0f;

	public void init() {
		try {
			System.out.println("Loading driver...");
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded successfully!");
			con=DriverManager.getConnection(url, UserName, password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter pw = response.getWriter();
//	    if (con == null) {
//	        pw.println("Database connection was not initialized. Check server logs.");
//	        return;
//	    }

	    try {
	        pstmt = con.prepareStatement("SELECT * FROM student WHERE usn = ? ");
	        String temp = request.getParameter("usn");
	        pstmt.setString(1, temp);
	        res = pstmt.executeQuery();
	        while (res.next()) {
	            usn = res.getString(1);
	            sname = res.getString(2);
	            marks1 = res.getInt(3);
	            marks2 = res.getInt(4);
	            marks3 = res.getInt(5);
	            percent = res.getFloat(6);
	        }
	        pw.println(usn + " " + sname + " " + marks1 + " " + marks2 + " " + marks3 + " " + percent);
	    } catch (Exception e) {
	        e.printStackTrace(pw); // shows error in browser too
	    }
	}

	public void destroy() {
		try {
			res.close();
			pstmt.close();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}