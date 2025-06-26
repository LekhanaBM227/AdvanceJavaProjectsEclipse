//package com.jdbc.project1;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Scanner;
//
//public class EmployeeCRUD {
//
//	private static Connection con=null;
//
//	public static void CRUDEmployee() throws ClassNotFoundException, SQLException
//	{
//		try {
//			System.out.println("Java's database Connectivity check.");
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("Driver is loaded");
//
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBC_DB","root","Lek!@#227");
//			if(connection==null)
//			{
//				System.out.println("Connectivity failed.");
//			}
//			else
//			{
//				System.out.println("Connection successful.");
//			}
//		} 
//		catch (ClassNotFoundException e) {
//			System.out.println("Driver could not be loaded.");
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//
//		insertData();
//		updateName();
//		updatesalary();
//		updateCompany();
//		fetchAllData();
//		fetchDataById();
//		fetchDataByCompany();
//		deleteDataById();
//		
//
//	}
//
//
//	public static boolean insertData(Student s) {
//		// Insert data code goes here
//
//		try {
//			String sql = "Insert into employee values(?,?,?,?,?,?)"; // Placeholder
//
//			// Step4: Convert sql query to statement
//			PreparedStatement ps = null;
//			ps = con.prepareStatement(sql);
//			Scanner sc = new Scanner(System.in);
//			for(int i=1;i<3;i++) {
//				System.out.print("Enter the id: ");
//				ps.setInt(1, sc.nextInt());
//				System.out.print("Enter the employee name: ");
//				ps.setString(2, sc.next());
//				System.out.print("Enter the colege: ");
//				ps.setString(3, sc.next());
//				System.out.print("Enter the salary: ");
//				ps.setInt(4, sc.nextInt());
//				System.out.print("Enter the designation: ");
//				ps.setString(5, sc.next());
//				System.out.print("Enter the experience: ");
//				ps.setInt(6, sc.nextInt());
//				// Step5: Execute Query and collect result
//
//				int x = ps.executeUpdate();
//
//				if(x>0)
//				{
//					return true;
//				}
//				else
//				{
//					return false;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//		return false;
//	}
//
//	public static boolean updateName(String name, int id) {
//		// Update name code goes here
//
//		try {
//			String sql = "update employee set name=? where id=?"; // Placeholder
//
//			// Step4: Convert sql query to statement
//			PreparedStatement ps = null;
//			ps = con.prepareStatement(sql);
//			ps.setInt(1, id);
//			ps.setString(2, name);
//
//			// Step5: Execute Query and collect result
//
//			int x = ps.executeUpdate();
//
//			if(x>0)
//			{
//				return true;
//			}
//			else
//			{
//				return false;
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	public static boolean updatesalary(int salary, int id) {
//		// Update name code goes here
//
//		try {
//			String sql = "update employee set salary=? where id=?"; // Placeholder
//
//			// Step4: Convert sql query to statement
//			PreparedStatement ps = null;
//			ps = con.prepareStatement(sql);
//			ps.setInt(1, id);
//			ps.setInt(2, salary);
//
//			// Step5: Execute Query and collect result
//
//			int x = ps.executeUpdate();
//
//			if(x>0)
//			{
//				return true;
//			}
//			else
//			{
//				return false;
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	public static boolean updateCompany(String company, int id) {
//		// Update name code goes here
//
//		try {
//			String sql = "update employee set name=? where id=?"; // Placeholder
//
//			// Step4: Convert sql query to statement
//			PreparedStatement ps = null;
//			ps = con.prepareStatement(sql);
//			ps.setInt(1, id);
//			ps.setString(2, company);
//
//			// Step5: Execute Query and collect result
//
//			int x = ps.executeUpdate();
//
//			if(x>0)
//			{
//				return true;
//			}
//			else
//			{
//				return false;
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	public static void fetchAllData() throws SQLException {
//		// Retrieve data code goes here
//		String sql = "select * from employee";
//		Statement stmt = con.createStatement();
//		ResultSet res = stmt.executeQuery(sql);
//		while(res.next()==true)
//		{
//			System.out.println("employee id: "+res.getInt(1));
//			System.out.println("employee name: "+res.getString(2));
//			System.out.println("employee company: "+res.getString(3));
//			System.out.println("employee salary: "+res.getInt(4));
//			System.out.println("employee designation: "+res.getString(5));
//			System.out.println("employee experiance: "+res.getInt(4));
//		}
//
//	}
//
//	public static void fetchDataById(int id1) throws SQLException {
//		// Retrieve data code goes here
//		String sql = "select * from employee where id=?";
//		PreparedStatement ps = con.prepareStatement(sql);
//		Scanner sc = new Scanner(System.in);
//		ps.setInt(1, id1);
//		ResultSet res = ps.executeQuery();
//		if(res.next())
//		{
//			System.out.println("employee id: "+res.getInt(1));
//			System.out.println("employee name: "+res.getString(2));
//			System.out.println("employee company: "+res.getString(3));
//			System.out.println("employee salary: "+res.getInt(4));
//			System.out.println("employee designation: "+res.getString(5));
//			System.out.println("employee experiance: "+res.getInt(4));
//		}
//		else
//		{
//			System.out.println("No student id is found: "+id1);
//		}
//
//	}
//
//	public static void fetchDataByCompany(String company) throws SQLException {
//		// Retrieve data code goes here
//		String sql = "select * from employee where company=?";
//		PreparedStatement ps = con.prepareStatement(sql);
//		Scanner sc = new Scanner(System.in);
//		ps.setString(1, company);
//		ResultSet res = ps.executeQuery();
//		if(res.next())
//		{
//			System.out.println("employee id: "+res.getInt(1));
//			System.out.println("employee name: "+res.getString(2));
//			System.out.println("employee company: "+res.getString(3));
//			System.out.println("employee salary: "+res.getInt(4));
//			System.out.println("employee designation: "+res.getString(5));
//			System.out.println("employee experiance: "+res.getInt(4));
//		}
//		else
//		{
//			System.out.println("No student id is found: "+company);
//		}
//
//	}
//
//	public static boolean deleteDataById(int id) throws SQLException {
//
//		String sql = "delete from employee where id=?"; // Placeholder
//
//		// Step4: Convert sql query to statement
//		PreparedStatement ps = con.prepareStatement(sql);
//		ps.setInt(1, id);
//
//		// Step5: Execute Query and collect result
//
//		int x = ps.executeUpdate();
//
//		if(x>0)
//		{
//			System.out.println("Database Deleted.");
//		}
//		else
//		{
//			System.out.println("Database deletion failed");
//		}
//
//		return false;
//	}
//
//}
