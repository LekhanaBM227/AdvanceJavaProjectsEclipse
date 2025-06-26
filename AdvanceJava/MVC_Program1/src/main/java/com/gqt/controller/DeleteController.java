package com.gqt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.gqt.model.Model;

@WebServlet("/Delete")  // Servlet URL mapping
public class DeleteController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));

            Model model = new Model();
            model.setId(id);
            boolean result = model.delete();

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();

            if (result) {
                out.println("<h3>✅ Employee deleted successfully!</h3>");
            } else {
                out.println("<h3>❌ Failed to delete employee. ID may not exist.</h3>");
            }
            out.println("<a href='index.html'>Back</a>");

        } catch (NumberFormatException e) {
            resp.getWriter().println("<h3>⚠️ Invalid ID format</h3>");
        }
    }
}
