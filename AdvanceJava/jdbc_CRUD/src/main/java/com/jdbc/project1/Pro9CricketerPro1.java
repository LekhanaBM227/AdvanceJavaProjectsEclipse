package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Pro9CricketerPro1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		// Step1: Load the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Step2: Establish connection
		String url = "jdbc:mysql://localhost:3306/cricketer";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);

		System.out.println("Enter the details of the cricketer.");

		// Step3: Create SQL Query
		String sql = "Insert into cricketer values(?,?,?,?,?,?,?,?,?,?)"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		//		for(int i=1;i<3;i++) {
		System.out.print("Enter the id: ");
		ps.setInt(1, sc.nextInt());
		System.out.print("Enter the cricketer name: ");
		ps.setString(2, sc.next());
		System.out.print("Enter the country of the cricketer: ");
		ps.setString(3, sc.next());
		System.out.print("Enter the age of cricketer: ");
		ps.setInt(4, sc.nextInt());
		System.out.print("Enter the matches of cricketer: ");
		ps.setInt(5, sc.nextInt());
		System.out.print("Enter the runs of cricketer: ");
		ps.setInt(6, sc.nextInt());
		System.out.print("Enter the wicketes of cricketer: ");
		ps.setInt(7, sc.nextInt());
		System.out.print("Enter the catches of cricketer: ");
		ps.setInt(8, sc.nextInt());
		System.out.print("Enter the Man of the match of cricketer: ");
		ps.setString(9, sc.nextLine());
		System.out.print("Enter the average of cricketer: ");
		ps.setDouble(10, sc.nextDouble());

		// Step5: Execute Query and collect result

		int x = ps.executeUpdate();

		if(x>0)
		{
			System.out.println("Database Inserted.");
		}
		else
		{
			System.out.println("Database Insertion failed");
		}
		//		}


		// Step6: Close the connections
		ps.close();
		con.close();
	}

}
