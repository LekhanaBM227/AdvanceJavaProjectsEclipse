<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    Integer scoreObj = (Integer) session.getAttribute("score");
    int score = (scoreObj != null) ? scoreObj : 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Result</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
             background: url('images/image2.jpg') no-repeat center center fixed;
            background-size: cover;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .result-container {
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 450px;
        }
        .result-container h1 {
            font-size: 2em;
            color: #4CAF50;
            margin-bottom: 20px;
        }
        .result-container p {
            font-size: 1.2em;
            margin-bottom: 20px;
        }
        .username {
            font-size: 1.5em;
            font-weight: bold;
            color: #333;
        }
        .score {
            font-size: 1.5em;
            font-weight: bold;
            color: #FF5722;
        }
        .btn-home {
            display: inline-block;
            padding: 10px 20px;
            font-size: 1em;
            margin-top: 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .btn-home:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="result-container">
        <h1>Quiz Completed!</h1>
        <p>Thank you, <span class="username"><%= username != null ? username : "Participant" %></span>, for participating in the quiz.</p>
        <p>Your total score is: <span class="score"><%= score %></span></p>
        <a href="index.jsp" class="btn-home">Go to Home</a>
    </div>
</body>
</html>