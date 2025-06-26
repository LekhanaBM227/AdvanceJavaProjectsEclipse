package com.gqt.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {

    private Connection con;
    private final String url = "jdbc:mysql://localhost:3306/employee";
    private final String username = "root";
    private final String password = "Lek!@#227";
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";

    private int id, age, salary;
    private String name, company, designation;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    // Constructor
    public Model() {
        try {
            System.out.println("⏳ Trying to load JDBC Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ JDBC Driver loaded successfully.");

            System.out.println("⏳ Attempting DB connection...");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employee?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "Lek!@#227"
            );

            if (con != null) {
                System.out.println("✅ Database connection successful.");
            } else {
                System.out.println("❌ Database connection is null.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ SQLException while connecting to DB.");
            e.printStackTrace();
        }
    }




    public boolean insertData() throws SQLException {
        if (con == null) {
            System.out.println("⚠️ Database connection is null, aborting insert.");
            return false;
        }

        String sql = "INSERT INTO employees (id, name, company, age, salary, designation) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, company);
        ps.setInt(4, age);
        ps.setInt(5, salary);
        ps.setString(6, designation);

        int rows = ps.executeUpdate();
        ps.close();
        con.close();

        return rows > 0;
    }
    
    public ArrayList<String> fetchAllData() throws SQLException {
        ArrayList<String> dataList = new ArrayList<>();
        
        String sql = "SELECT * FROM employees";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String company = rs.getString("company");
            int age = rs.getInt("age");
            int salary = rs.getInt("salary");
            String designation = rs.getString("designation");

            String row = "ID: " + id + ", Name: " + name + ", Company: " + company +
                         ", Age: " + age + ", Salary: " + salary + ", Designation: " + designation;
            dataList.add(row);
        }

        rs.close();
        ps.close();

        return dataList;
    }
    
 // Add this method to return the connection
    public Connection getConnection() {
        return con;
    }

    public boolean fetchSpecific() {
        try {
            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.name = rs.getString("name");
                this.company = rs.getString("company");
                this.age = rs.getInt("age");
                this.salary = rs.getInt("salary");
                this.designation = rs.getString("designation");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete() {
        
        try {
            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } 
        return false;  // Return false if an error occurred
    }

}
