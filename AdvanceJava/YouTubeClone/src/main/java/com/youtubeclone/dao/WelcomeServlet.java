package com.youtubeclone.dao;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get current session without creating a new one
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            // Not logged in, redirect to login page
            response.sendRedirect("login.html");
            return;
        }

        String userEmail = (String) session.getAttribute("userEmail");

        out.println("<html><body>");
        out.println("<h1>Welcome to YouTube Clone, " + userEmail + "!</h1>");
        out.println("<p>This is your dashboard.</p>");
        out.println("<a href='LogoutServlet'>Logout</a>");
        out.println("</body></html>");
    }
}
