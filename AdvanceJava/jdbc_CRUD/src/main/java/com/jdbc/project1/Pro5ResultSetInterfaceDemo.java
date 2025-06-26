package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pro5ResultSetInterfaceDemo {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/gqt_adv_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);
		
		String sql = "select * from student";
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		while(res.next()==true)
		{
			System.out.println("Student id: "+res.getInt(1));
			System.out.println("Student name: "+res.getString(2));
			System.out.println("Student name: "+res.getString(3));
			System.out.println("Student id: "+res.getLong(4));
			System.out.println("Student name: "+res.getString(5));
		}
		
		res.close();
		stmt.close();
		con.close();
	}

}
