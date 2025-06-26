package com.quizapp.service;

import com.quizapp.dao.QuizDAO;
import com.quizapp.dao.QuestionDAO;
import com.quizapp.dao.UserDAO;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.Question;
import com.quizapp.entity.User;
import com.quizapp.exception.QuizNotFoundException;
import com.quizapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);
    private final QuizDAO quizDAO;
    private final QuestionDAO questionDAO;
    private final UserDAO userDAO;

    public QuizService() {
        this.quizDAO = new QuizDAO();
        this.questionDAO = new QuestionDAO();
        this.userDAO = new UserDAO();
    }

    public QuizService(QuizDAO quizDAO, QuestionDAO questionDAO, UserDAO userDAO) {
        this.quizDAO = quizDAO;
        this.questionDAO = questionDAO;
        this.userDAO = userDAO;
    }

    public Quiz createQuiz(String title, String description, Integer timeLimit, Integer maxAttempts, 
                          Double passingScore, Long creatorId) {
        logger.info("Creating quiz: {} by user id: {}", title, creatorId);
        
        // Validate input
        validateQuizInput(title, description, timeLimit, maxAttempts, passingScore);
        
        // Verify creator exists
        User creator = userDAO.findById(creatorId);
        if (creator == null) {
            throw new UserNotFoundException("Creator not found with id: " + creatorId);
        }
        
        // Create new quiz
        Quiz quiz = new Quiz();
        quiz.setTitle(title.trim());
        quiz.setDescription(description != null ? description.trim() : null);
        quiz.setTimeLimit(timeLimit);
        quiz.setMaxAttempts(maxAttempts);
        quiz.setPassingScore(passingScore);
        quiz.setCreator(creator);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        quiz.setIsActive(true);
        
        Quiz savedQuiz = quizDAO.save(quiz);
        logger.info("Quiz created successfully with id: {}", savedQuiz.getId());
        return savedQuiz;
    }

    public Quiz getQuizById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null");
        }
        
        Quiz quiz = quizDAO.findById(id);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + id);
        }
        
        return quiz;
    }

    public List<Quiz> getQuizzesByCreator(Long creatorId) {
        if (creatorId == null) {
            throw new IllegalArgumentException("Creator ID cannot be null");
        }
        
        User creator = userDAO.findById(creatorId);
        if (creator == null) {
            throw new UserNotFoundException("Creator not found with id: " + creatorId);
        }
        
        return quizDAO.findByCreator(creator);
    }

    public List<Quiz> getAllActiveQuizzes() {
        return quizDAO.findActiveQuizzes();
    }

    public List<Quiz> getAllQuizzes() {
        return quizDAO.findAll();
    }

    public List<Quiz> searchQuizzes(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveQuizzes();
        }
        
        return quizDAO.searchByTitleOrDescription(searchTerm.trim());
    }

    public Quiz updateQuiz(Long id, String title, String description, Integer timeLimit, 
                          Integer maxAttempts, Double passingScore) {
        Quiz quiz = getQuizById(id);
        
        boolean isModified = false;
        
        if (title != null && !title.trim().isEmpty() && !title.equals(quiz.getTitle())) {
            validateTitle(title);
            quiz.setTitle(title.trim());
            isModified = true;
        }
        
        if (description != null && !description.equals(quiz.getDescription())) {
            quiz.setDescription(description.trim().isEmpty() ? null : description.trim());
            isModified = true;
        }
        
        if (timeLimit != null && !timeLimit.equals(quiz.getTimeLimit())) {
            validateTimeLimit(timeLimit);
            quiz.setTimeLimit(timeLimit);
            isModified = true;
        }
        
        if (maxAttempts != null && !maxAttempts.equals(quiz.getMaxAttempts())) {
            validateMaxAttempts(maxAttempts);
            quiz.setMaxAttempts(maxAttempts);
            isModified = true;
        }
        
        if (passingScore != null && !passingScore.equals(quiz.getPassingScore())) {
            validatePassingScore(passingScore);
            quiz.setPassingScore(passingScore);
            isModified = true;
        }
        
        if (isModified) {
            quiz.setUpdatedAt(LocalDateTime.now());
            Quiz updatedQuiz = quizDAO.update(quiz);
            logger.info("Quiz updated successfully with id: {}", id);
            return updatedQuiz;
        }
        
        return quiz;
    }

    public void activateQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        
        // Validate quiz has questions before activating
        List<Question> questions = questionDAO.findByQuiz(quiz);
        if (questions.isEmpty()) {
            throw new IllegalStateException("Cannot activate quiz without questions");
        }
        
        quiz.setIsActive(true);
        quiz.setUpdatedAt(LocalDateTime.now());
        
        quizDAO.update(quiz);
        logger.info("Quiz activated successfully with id: {}", id);
    }

    public void deactivateQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quiz.setIsActive(false);
        quiz.setUpdatedAt(LocalDateTime.now());
        
        quizDAO.update(quiz);
        logger.info("Quiz deactivated successfully with id: {}", id);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        
        // Check if quiz has any attempts
        // In a real application, you might want to prevent deletion if attempts exist
        // or cascade delete the attempts as well
        
        quizDAO.delete(id);
        logger.info("Quiz deleted successfully with id: {}", id);
    }

    public long getTotalQuizCount() {
        return quizDAO.count();
    }

    public long getActiveQuizCount() {
        return quizDAO.countActiveQuizzes();
    }

    public long getQuizCountByCreator(Long creatorId) {
        if (creatorId == null) {
            throw new IllegalArgumentException("Creator ID cannot be null");
        }
        
        User creator = userDAO.findById(creatorId);
        if (creator == null) {
            throw new UserNotFoundException("Creator not found with id: " + creatorId);
        }
        
        return quizDAO.countByCreator(creator);
    }

    private void validateQuizInput(String title, String description, Integer timeLimit, 
                                   Integer maxAttempts, Double passingScore) {
        validateTitle(title);
        validateTimeLimit(timeLimit);
        validateMaxAttempts(maxAttempts);
        validatePassingScore(passingScore);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz title cannot be empty");
        }
        
        if (title.length() > 200) {
            throw new IllegalArgumentException("Quiz title cannot exceed 200 characters");
        }
    }

    private void validateTimeLimit(Integer timeLimit) {
        if (timeLimit != null && timeLimit <= 0) {
            throw new IllegalArgumentException("Time limit must be greater than 0");
        }
        
        if (timeLimit != null && timeLimit > 7200) { // 2 hours max
            throw new IllegalArgumentException("Time limit cannot exceed 7200 seconds (2 hours)");
        }
    }

    private void validateMaxAttempts(Integer maxAttempts) {
        if (maxAttempts != null && maxAttempts <= 0) {
            throw new IllegalArgumentException("Max attempts must be greater than 0");
        }
        
        if (maxAttempts != null && maxAttempts > 50) {
            throw new IllegalArgumentException("Max attempts cannot exceed 50");
        }
    }

    private void validatePassingScore(Double passingScore) {
        if (passingScore != null && (passingScore < 0 || passingScore > 100)) {
            throw new IllegalArgumentException("Passing score must be between 0 and 100");
        }
    }
}