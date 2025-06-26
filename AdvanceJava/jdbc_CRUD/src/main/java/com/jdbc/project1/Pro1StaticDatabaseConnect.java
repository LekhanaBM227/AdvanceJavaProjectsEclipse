package com.jdbc.project1;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Pro1StaticDatabaseConnect {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// Step1: Load the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		// Step2: Establish connection
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);
		
		// Step3: Create SQL Query
		String sql = "Create Schema GQT_ADV_DB";
		
		// Step4: Convert SQL Query to statement
		java.sql.Statement stmt = con.createStatement();
		
		// Step5: Execute query and collect result
		int x = stmt.executeUpdate(sql);
		if(x>=0)
		{
			
			System.out.println("Database created.");
		}
		else
		{
			System.out.println("Database creation failed");
		}
		
		// Step6: close connection
		stmt.close();
		con.close();
	}

}
