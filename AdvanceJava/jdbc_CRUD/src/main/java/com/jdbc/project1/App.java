package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App 
{
    public static void main( String[] args )
    {
    	try {
			System.out.println("Java's database Connectivity check.");
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver is loaded");
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBC_DB","root","Lek!@#227");
			if(connection==null)
			{
				System.out.println("Connectivity failed.");
			}
			else
			{
				System.out.println("Connection successful.");
			}
		} 
    	catch (ClassNotFoundException e) {
    		System.out.println("Driver could not be loaded.");
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
}
