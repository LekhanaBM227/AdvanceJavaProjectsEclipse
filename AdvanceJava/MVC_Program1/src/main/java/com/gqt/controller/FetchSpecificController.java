package com.gqt.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.gqt.model.Model;

@WebServlet("/FetchSpecific")
public class FetchSpecificController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("➡️ FetchSpecificController servlet called");

        int id = Integer.parseInt(req.getParameter("id"));
        Model m = new Model();
        m.setId(id);
        boolean found = m.fetchSpecific();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (found) {
            out.println("<h2>Employee Details:</h2>");
            out.println("<p>ID: " + m.getId() + "</p>");
            out.println("<p>Name: " + m.getName() + "</p>");
            out.println("<p>Company: " + m.getCompany() + "</p>");
            out.println("<p>Age: " + m.getAge() + "</p>");
            out.println("<p>Salary: " + m.getSalary() + "</p>");
            out.println("<p>Designation: " + m.getDesignation() + "</p>");
        } else {
            out.println("<h3>No employee found with ID: " + id + "</h3>");
        }
        out.println("<a href='index.html'>Back</a>");
    }
}

