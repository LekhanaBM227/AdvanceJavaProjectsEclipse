package com.jdbc.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.jdbc.project1.AppCrick;
import com.jdbc.utils.ConnectionUtils;
import com.jdbc.utils.Cricketer;

public class Cricketer_Model {
	private Connection con;
	int con_cnt = 2;
	private PreparedStatement ps;

	public Cricketer_Model() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(ConnectionUtils.url, ConnectionUtils.user, ConnectionUtils.pwd);

			if (con != null) {
				System.out.println("Connection established!");
				System.out.println("=============================");
			} else {
				System.out.println("Connection establishment failed");
				AppCrick ap = new AppCrick();
				if (con_cnt != 0) {
					ap.main(null);
				} else {
					System.exit(0);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean insertData(Cricketer c) {

		try {
			String sql = "Insert into cricketer(name, country, age, matches, runs, wickets, catches, mom, average) values(?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, c.getName());
			ps.setString(2, c.getCountry());
			ps.setInt(3, c.getAge());
			ps.setInt(4, c.getMatches());
			ps.setInt(5, c.getRuns());
			ps.setInt(6, c.getWickets());
			ps.setInt(7, c.getCatches());
			ps.setInt(8, c.getMom());
			ps.setDouble(9, c.getAverage());
			
			int x = ps.executeUpdate();
			if(x>0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}
