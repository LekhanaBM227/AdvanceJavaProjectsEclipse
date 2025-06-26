package com.jdbc.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Pro8_AllCRUDMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/gqt_adv_db";
		String user = "root";
		String pwd = "Lek!@#227";
		Connection con = DriverManager.getConnection(url,user,pwd);
	}

}
