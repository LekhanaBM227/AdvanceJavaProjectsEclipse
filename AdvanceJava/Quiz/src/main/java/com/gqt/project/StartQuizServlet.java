package com.gqt.project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/startQuiz")
public class StartQuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        QuestionDAO dao = new QuestionDAO();
        List<Question> questions = dao.getAllQuestions();

        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("questions", questions);
        session.setAttribute("score", 0);
        session.setAttribute("questionNo", 0);

        if (questions.isEmpty()) {
            response.getWriter().println("No questions found in database.");
            return;
        }

        Question firstQuestion = questions.get(0);
        request.setAttribute("question", firstQuestion);
        request.setAttribute("questionNo", 1);
        request.getRequestDispatcher("quiz.jsp").forward(request, response);
    }
}
