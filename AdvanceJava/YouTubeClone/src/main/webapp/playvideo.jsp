<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Play Video</title>
</head>
<body>
<h2>Video Player</h2>

<%
    String fileName = request.getParameter("fileName");
    if (fileName == null || fileName.isEmpty()) {
%>
    <p>No video selected.</p>
<%
    } else {
        // Assuming your videos are saved under "uploaded_videos" folder in webapp
        String videoPath = "uploaded_videos/" + fileName;
%>
    <video width="640" height="360" controls>
        <source src="<%= videoPath %>" type="video/mp4">
        Your browser does not support the video tag.
    </video>
<%
    }
%>

<br><a href="VideoListServlet">Back to Videos List</a>
</body>
</html>
