package com.gqt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
		        
		        Integer a = new Integer(marks1);
		        Integer b = new Integer(marks2);
		        Integer c = new Integer(marks3);
		        Float d = new Float(percent);
		        
		        String a1 = a.toString();
		        String b1 = b.toString();
		        String c1 = c.toString();
		        String d1 = d.toString();
		        
		        Cookie ck1 = new Cookie("REG_NO",usn);
		        Cookie ck2 = new Cookie("NAME",sname);
		        Cookie ck3 = new Cookie("MARKS1",a1);
		        Cookie ck4 = new Cookie("MARKS2",b1);
		        Cookie ck5 = new Cookie("MARKS3",c1);
		        Cookie ck6 = new Cookie("PERCENTAGE",d1);

		        ck1.setMaxAge(60*60);
		        ck2.setMaxAge(60*60);
		        ck3.setMaxAge(60*60);
		        ck4.setMaxAge(60*60);
		        ck5.setMaxAge(60*60);
		        ck6.setMaxAge(60*60);

		        response.addCookie(ck1);
		        response.addCookie(ck2);
		        response.addCookie(ck3);
		        response.addCookie(ck4);
		        response.addCookie(ck5);
		        response.addCookie(ck6);  
		}catch (Exception e) {
		        e.printStackTrace(); // shows error in browser too
		    }
	        
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
