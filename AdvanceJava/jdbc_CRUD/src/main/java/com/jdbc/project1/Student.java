package com.jdbc.project1;

public class Student {

	int id;
	String name,email,college;
	long phn;
	public Student(int id, String name, String email, String college, long phn) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.college = college;
		this.phn = phn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public long getPhn() {
		return phn;
	}
	public void setPhn(long phn) {
		this.phn = phn;
	}
	
}
