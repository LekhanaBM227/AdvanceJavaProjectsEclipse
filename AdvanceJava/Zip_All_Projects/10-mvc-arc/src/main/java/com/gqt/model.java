package com.gqt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class model {
private String url="jdbc:mysql://localhost:3306/college";
private String username="root";
private String password="Lek!@#227";
private Connection con=null;
private PreparedStatement pstmt=null;
private ResultSet res=null;

private String usn=null;
private String name=null;
private int marks1=0;
private int marks2=0;
private int marks3=0;
private float percent=0.0f;

public model() {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con=DriverManager.getConnection(url, username, password);
	}

	catch(Exception e)
	{
		e.printStackTrace();
	}
}

public String getUsn() {
	return usn;
}

public void setUsn(String usn) {
	this.usn = usn;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getMarks1() {
	return marks1;
}

public void setMarks1(int marks1) {
	this.marks1 = marks1;
}

public int getMarks2() {
	return marks2;
}

public void setMarks2(int marks2) {
	this.marks2 = marks2;
}

public int getMarks3() {
	return marks3;
}

public void setMarks3(int marks3) {
	this.marks3 = marks3;
}

public float getPercent() {
	return percent;
}

public void setPercent(float percent) {
	this.percent = percent;
}
//executing a query 
public void Getresult() {
	try {
		pstmt = con.prepareStatement("SELECT * FROM student WHERE usn = ? ");
        pstmt.setString(1, usn);
        res = pstmt.executeQuery();
        while (res.next()) {
            usn = res.getString(1);
            name = res.getString(2);
            marks1 = res.getInt(3);
            marks2 = res.getInt(4);
            marks3 = res.getInt(5);
            percent = res.getFloat(6);
}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
}
public void closeConnection() {
	try {
		res.close();
		pstmt.close();
		con.close();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
}
}
