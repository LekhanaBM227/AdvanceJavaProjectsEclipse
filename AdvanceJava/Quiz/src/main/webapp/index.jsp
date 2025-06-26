<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Start Quiz</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            background: url('images/image2.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .container {
            text-align: center;
            padding: 30px;
            border-radius: 10px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-size: 2em;
            margin-bottom: 20px;
        }
        label {
            font-size: 1.2em;
            margin-bottom: 10px;
        }
        input[type="text"] {
            padding: 10px;
            font-size: 1em;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 200px;
        }
        input[type="submit"] {
            padding: 10px 20px;
            font-size: 1em;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Quiz!</h1>
        <form action="startQuiz" method="post">
            <label for="username">Enter Your Name:</label><br>
            <input type="text" name="username" id="username" required /><br>
            <input type="submit" value="Start Quiz" />
        </form>
    </div>
</body>
</html>