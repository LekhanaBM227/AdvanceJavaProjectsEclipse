package com.jdbc.project1;

public class Employees {
	int empid,salary,exp;
	String empname,company,designation;
	public int getEmpid() {
		return empid;
	}
	public void setEmpid(int empid) {
		this.empid = empid;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public Employees(int empid, int salary, int exp, String empname, String company, String designation) {
		super();
		this.empid = empid;
		this.salary = salary;
		this.exp = exp;
		this.empname = empname;
		this.company = company;
		this.designation = designation;
	}
}
