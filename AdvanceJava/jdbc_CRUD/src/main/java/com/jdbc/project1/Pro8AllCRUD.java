package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Pro8AllCRUD {

	private static Connection con=null;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// the connection statement goes here.
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/jdbc_db";
		String user = "root";
		String pwd = "Lek!@#227";
		con = DriverManager.getConnection(url,user,pwd);
		
		insertData();
		updateName();
		updatephoneNo();
		updateEmail();
		fetchAllData();
		fetchDataById();
		fetchDataBycollege();
		deleteDataById();
		
	}

	//	private static boolean insertData() {
	//		// TODO Auto-generated method stub
	//		return false;
	//	}

	public static boolean insertData() throws SQLException {
		// Insert data code goes here

		// Step3: Create SQL Query
		String sql = "Insert into student values(?,?,?,?,?)"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = null;
		ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		for(int i=1;i<3;i++) {
			System.out.print("Enter the id: ");
			ps.setInt(1, sc.nextInt());
			System.out.print("Enter the student name: ");
			ps.setString(2, sc.next());
			System.out.print("Enter the college: ");
			ps.setString(3, sc.next());
			System.out.print("Enter the Phone num: ");
			ps.setLong(4, sc.nextLong());
			System.out.print("Enter the email: ");
			ps.setString(5, sc.next());
			// Step5: Execute Query and collect result

			int x = ps.executeUpdate();

			if(x>0)
			{
				System.out.println("Database Inserted.");
			}
			else
			{
				System.out.println("Database insertion failed");
			}
		}
		return false;
	}

	public static boolean updateName() throws SQLException {
		// Update name code goes here

		String sql = "update student set name=? where id=?"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = null;
		ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the id: ");
		ps.setInt(2, sc.nextInt());
		System.out.print("Enter the student name: ");
		ps.setString(1, sc.next());

		// Step5: Execute Query and collect result

		int x = ps.executeUpdate();

		if(x>0)
		{
			System.out.println("Database Inserted.");
		}
		else
		{
			System.out.println("Database insertion failed");
		}
		return false;
	}

	public static boolean updatephoneNo() throws SQLException {
		// Update email code goes here
		String sql = "update student set phone=? where id=?"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the id: ");
		ps.setInt(2, sc.nextInt());
		System.out.print("Enter the student name: ");
		ps.setLong(1, sc.nextLong());

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
		return false;
	}

	public static boolean updateEmail() throws SQLException {
		// Update name code goes here
		String sql = "update student set email=? where id=?"; // Placeholder

		// Step4: Convert sql query to statement
		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the id: ");
		ps.setInt(2, sc.nextInt());
		System.out.print("Enter the student name: ");
		ps.setString(1, sc.next());

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
		return false;
	}

	public static void fetchAllData() throws SQLException {
		// Retrieve data code goes here
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

	}

	public static void fetchDataById() throws SQLException {
		// Retrieve data by id goes here
		String sql = "select * from student where id=?";

		PreparedStatement ps = con.prepareStatement(sql);
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the id: ");
		int id1 = sc.nextInt();
		ps.setInt(1, id1);

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
			System.out.println("No student id is found: "+id1);
		}
	}

	public static void fetchDataBycollege() throws SQLException {
		
		//retrieve data by college
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
	}

	public static boolean deleteDataById() throws SQLException {
		
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
		
		return false;
	}
}
