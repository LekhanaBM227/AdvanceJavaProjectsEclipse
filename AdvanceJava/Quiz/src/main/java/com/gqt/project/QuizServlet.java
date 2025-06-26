package com.gqt.project;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Integer score = (Integer) session.getAttribute("score");
        Integer questionNo = (Integer) session.getAttribute("questionNo");
        String username = (String) session.getAttribute("username");

        // Safety checks
        if (score == null) {
            score = 0;
        }
        if (questionNo == null) {
            questionNo = 0;
        }

        String selectedOption = request.getParameter("option");
        if (selectedOption != null && questionNo < questions.size()) {
            Question currentQuestion = questions.get(questionNo);
            if (selectedOption.charAt(0) == currentQuestion.getCorrectOption()) {
                score += 10;
            }
        }

        session.setAttribute("score", score);
        questionNo++;
        session.setAttribute("questionNo", questionNo);

        if (questionNo < questions.size()) {
            Question nextQuestion = questions.get(questionNo);
            request.setAttribute("question", nextQuestion);
            request.setAttribute("questionNo", questionNo + 1);
            request.getRequestDispatcher("quiz.jsp").forward(request, response);
        } else {
            // No need to re-fetch or reset session
            response.sendRedirect("result.jsp");
        }
    }
}
