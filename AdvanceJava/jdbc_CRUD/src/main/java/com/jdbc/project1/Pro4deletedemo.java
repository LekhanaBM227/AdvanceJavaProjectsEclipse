package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Pro4deletedemo {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// Step1: Load the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Step2: Establish connection
		String url = "jdbc:mysql://localhost:3306/gqt_adv_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);

		// Step3: Create SQL Query
		String sql = "delete from student where id=?"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the id: ");
		ps.setInt(1, sc.nextInt());

		// Step5: Execute Query and collect result

		int x = ps.executeUpdate();

		if(x>0)
		{
			System.out.println("Database Deleted.");
		}
		else
		{
			System.out.println("Database deletion failed");
		}
		
		// Step6: close the connections
		ps.close();
		con.close();

	}

}

