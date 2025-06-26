package com.youtubeclone.dao;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet("/UploadVideoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB before writing to disk
    maxFileSize = 1024 * 1024 * 50,      // max 50MB per file
    maxRequestSize = 1024 * 1024 * 60    // max 60MB per request
)
public class UploadVideoServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploaded_videos";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the absolute path to save uploaded files
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadPath = applicationPath + File.separator + UPLOAD_DIR;

        // Create the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Get the video title from the form
        String title = request.getParameter("title");

        // Get the video file part
        Part filePart = request.getPart("videoFile");
        String fileName = getFileName(filePart);

        // Write the file to the upload directory
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // Save metadata to DB
        String dbURL = "jdbc:mysql://localhost:3306/youtube_clone";
        String dbUser = "root";
        String dbPass = "Lek!@#227";  // Replace this with your actual MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            String sql = "INSERT INTO videos (title, filename) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, fileName);
            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Video uploaded successfully!</h2>");
        out.println("<p>Title: " + title + "</p>");
        out.println("<p>File: " + fileName + "</p>");
        out.println("<a href='upload.html'>Upload another video</a><br>");
        out.println("<a href='WelcomeServlet'>Back to Dashboard</a>");
    }

	
	// Utility method to get file name from part header
	private String getFileName(Part part) {
	    String contentDisp = part.getHeader("content-disposition");
	    String[] tokens = contentDisp.split(";");
	    for (String token : tokens) {
	        if (token.trim().startsWith("filename")) {
	            return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return "";
	}


}
