package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Pro2InsertPreparedStatement {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// Step1: Load the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Step2: Establish connection
		String url = "jdbc:mysql://localhost:3306/gqt_adv_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);

		// Step3: Create SQL Query
		String sql = "Insert into student values(?,?,?,?,?)"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		for(int i=1;i<3;i++) {
			System.out.print("Enter the id: ");
			ps.setInt(1, sc.nextInt());
			System.out.print("Enter the employee name: ");
			ps.setString(2, sc.next());
			System.out.print("Enter the company: ");
			ps.setString(3, sc.next());
			System.out.print("Enter the age: ");
			ps.setInt(4, sc.nextInt());
			System.out.print("Enter the salary: ");
			ps.setInt(5, sc.nextInt());
			System.out.print("Enter the designation: ");
			ps.setString(3, sc.next());
			// Step5: Execute Query and collect result

			int x = ps.executeUpdate();
			
			if(x>0)
			{
				System.out.println("Database Inserted.");
			}
			else
			{
				System.out.println("Database creation failed");
			}
		}


		// Step6: Close the connections
		ps.close();
		con.close();

	}

}
