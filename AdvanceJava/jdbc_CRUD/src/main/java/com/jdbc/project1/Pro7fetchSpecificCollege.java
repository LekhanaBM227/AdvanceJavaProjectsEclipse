package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Pro7fetchSpecificCollege {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/gqt_adv_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);
		
		String sql = "select * from student where college=?";
		
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the college: ");
		String col = sc.next();
		ps.setString(1, col);
		
		ResultSet res = ps.executeQuery();
		if(res.next())
		{
			System.out.println("Student id: "+res.getInt(1));
			System.out.println("Student name: "+res.getString(2));
			System.out.println("Student name: "+res.getString(3));
			System.out.println("Student id: "+res.getLong(4));
			System.out.println("Student name: "+res.getString(5));
		}
		else
		{
			System.out.println("No student id is found: "+col);
		}
		
		res.close();
		ps.close();
		con.close();
		sc.close();
	}

}

