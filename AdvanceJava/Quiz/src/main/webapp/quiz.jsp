<%@ page import="com.gqt.project.Question" %>

<%
	Question question = (Question) request.getAttribute("question");

    int questionNo = (Integer) request.getAttribute("questionNo");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Quiz - Question <%= questionNo %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
             background: url('images/image2.jpg') no-repeat center center fixed;
            background-size: cover;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .quiz-container {
            background-color: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            width: 500px;
        }

        h2 {
            text-align: center;
            color: #333;
        }

        p {
            font-size: 18px;
            margin-bottom: 20px;
        }

        .option {
            margin-bottom: 10px;
            display: block;
            font-size: 16px;
        }

        input[type="submit"] {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 20px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="quiz-container">
        <h2>Question <%= questionNo %></h2>
        <form action="quiz" method="post">
            <p><%= question.getQuestionText() %></p>
            <label class="option">
                <input type="radio" name="option" value="A" required> <%= question.getOptionA() %>
            </label>
            <label class="option">
                <input type="radio" name="option" value="B"> <%= question.getOptionB() %>
            </label>
            <label class="option">
                <input type="radio" name="option" value="C"> <%= question.getOptionC() %>
            </label>
            <label class="option">
                <input type="radio" name="option" value="D"> <%= question.getOptionD() %>
            </label>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>