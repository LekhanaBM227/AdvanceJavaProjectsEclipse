package com.gqt.project;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/quizdb";
    private final String DB_USER = "root";
    private final String DB_PASS = "Rakshi@2003";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            PreparedStatement checkUser = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            checkUser.setString(1, username);
            ResultSet rs = checkUser.executeQuery();

            if (rs.next()) {
                // User exists, check password
                String storedPass = rs.getString("password");
                if (storedPass.equals(password)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("score", 0);
                    session.setAttribute("questionNo", 0);
                    response.sendRedirect("startQuiz");
                } else {
                    request.setAttribute("error", "Incorrect password!");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            } else {
                // User not found, register
                PreparedStatement insert = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)");
                insert.setString(1, username);
                insert.setString(2, password);
                insert.executeUpdate();

                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("score", 0);
                session.setAttribute("questionNo", 0);
                response.sendRedirect("startQuiz");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}