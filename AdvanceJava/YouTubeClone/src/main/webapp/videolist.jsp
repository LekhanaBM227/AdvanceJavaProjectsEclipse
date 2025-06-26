<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Uploaded Videos</title>
</head>
<body>
<h2>Uploaded Videos</h2>

<c:if test="${not empty videos}">
    <ul>
        <c:forEach var="video" items="${videos}">
            <li>
                <a href="playvideo.jsp?fileName=${video.fileName}">${video.title}</a>
            </li>
        </c:forEach>
    </ul>
</c:if>

<c:if test="${empty videos}">
    <p>No videos found.</p>
</c:if>

<a href="upload.html">Upload New Video</a>

</body>
</html>
