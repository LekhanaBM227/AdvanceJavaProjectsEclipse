package com.gqt.controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.gqt.model.Model;

@WebServlet("/Insert")
public class InsertController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("employeeName");
        String company = req.getParameter("company");
        int age = Integer.parseInt(req.getParameter("age"));
        int salary = Integer.parseInt(req.getParameter("salary"));
        String designation = req.getParameter("designation");

        Model m = new Model();
        m.setId(id);
        m.setName(name);
        m.setCompany(company);
        m.setAge(age);
        m.setSalary(salary);
        m.setDesignation(designation);

        boolean success = false;
        try {
            success = m.insertData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (success) {
            resp.sendRedirect("insertSuccess.html");
        } else {
            resp.sendRedirect("insertFailure.html");
        }
    }
}
