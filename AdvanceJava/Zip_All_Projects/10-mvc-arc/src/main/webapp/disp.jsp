<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
out.println(session.getAttribute("USN"));
out.println(session.getAttribute("NAME"));
out.println(session.getAttribute("MARKS1"));
out.println(session.getAttribute("MARKS2"));
out.println(session.getAttribute("MARKS3"));
out.println(session.getAttribute("PERCENT"));

%>

</body>
</html>