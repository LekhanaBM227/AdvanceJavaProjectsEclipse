package com.youtubeclone.dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/VideoListServlet")
public class VideoListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String dbURL = "jdbc:mysql://localhost:3306/youtube_clone";
        String dbUser = "root";
        String dbPass = "Lek!@#227"; // Replace with your password

        out.println("<h1>Uploaded Videos</h1>");
        out.println("<a href='upload.html'>Upload New Video</a><br><br>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            String sql = "SELECT * FROM videos ORDER BY upload_time DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            out.println("<ul>");
            while (rs.next()) {
                String title = rs.getString("title");
                String filename = rs.getString("filename");

                // Link to video file inside uploaded_videos folder
                out.println("<li><a href='uploaded_videos/" + filename + "'>" + title + "</a></li>");
            }
            out.println("</ul>");

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p>Error fetching videos.</p>");
            e.printStackTrace(out);
        }
    }
}

