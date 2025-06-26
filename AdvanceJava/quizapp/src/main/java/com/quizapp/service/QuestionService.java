package com.quizapp.service;

import com.quizapp.dao.QuestionDAO;
import com.quizapp.dao.OptionDAO;
import com.quizapp.dao.QuizDAO;
import com.quizapp.entity.Question;
import com.quizapp.entity.Option;
import com.quizapp.entity.Quiz;
import com.quizapp.exception.QuestionNotFoundException;
import com.quizapp.exception.QuizNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
    private final QuestionDAO questionDAO;
    private final OptionDAO optionDAO;
    private final QuizDAO quizDAO;

    public void deleteOption(Long optionId) {
        Option option = optionDAO.findById(optionId);
        if (option == null) {
            throw new IllegalArgumentException("Option not found with id: " + optionId);
        }
        
        optionDAO.delete(optionId);
        logger.info("Option deleted successfully with id: {}", optionId);
    }

    public List<Option> getOptionsByQuestion(Long questionId) {
        Question question = getQuestionById(questionId);
        return optionDAO.findByQuestion(question);
    }

    public long getQuestionCountByQuiz(Long quizId) {
        if (quizId == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null");
        }
        
        Quiz quiz = quizDAO.findById(quizId);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
        
        return questionDAO.countByQuiz(quiz);
    }
    
    public int getQuestionCountByQuizId(Long quizId) {
        if (quizId == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null");
        }
        
        try {
            Quiz quiz = quizDAO.findById(quizId);
            if (quiz == null) {
                return 0;
            }
            
            List<Question> questions = questionDAO.findByQuiz(quiz);
            return questions.size();
        } catch (Exception e) {
            logger.error("Error counting questions for quiz id {}: {}", quizId, e.getMessage());
            return 0;
        }
    }

    public Double getTotalPointsByQuiz(Long quizId) {
        if (quizId == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null");
        }
        
        Quiz quiz = quizDAO.findById(quizId);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
        
        List<Question> questions = questionDAO.findByQuiz(quiz);
        return questions.stream()
                .mapToDouble(Question::getPoints)
                .sum();
    }

    public void reorderQuestions(Long quizId, List<Long> questionIds) {
        Quiz quiz = quizDAO.findById(quizId);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
        
        for (int i = 0; i < questionIds.size(); i++) {
            Question question = getQuestionById(questionIds.get(i));
            if (!question.getQuiz().getId().equals(quizId)) {
                throw new IllegalArgumentException("Question " + questionIds.get(i) + " does not belong to quiz " + quizId);
            }
            
            question.setOrderIndex(i + 1);
            question.setUpdatedAt(LocalDateTime.now());
            questionDAO.update(question);
        }
        
        logger.info("Questions reordered successfully for quiz id: {}", quizId);
    }

    private void validateQuestionInput(String text, String type, Double points) {
        validateQuestionText(text);
        validateQuestionType(type);
        validatePoints(points);
    }

    private void validateQuestionText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        
        if (text.length() > 1000) {
            throw new IllegalArgumentException("Question text cannot exceed 1000 characters");
        }
    }

    private void validateQuestionType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Question type cannot be empty");
        }
        
        String upperType = type.toUpperCase();
        if (!upperType.equals("SINGLE_CHOICE") && !upperType.equals("MULTIPLE_CHOICE") && 
            !upperType.equals("TRUE_FALSE") && !upperType.equals("TEXT")) {
            throw new IllegalArgumentException("Invalid question type. Must be SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_FALSE, or TEXT");
        }
    }

    private void validatePoints(Double points) {
        if (points != null && points <= 0) {
            throw new IllegalArgumentException("Points must be greater than 0");
        }
        
        if (points != null && points > 100) {
            throw new IllegalArgumentException("Points cannot exceed 100");
        }
    }

    private void validateOptionInput(String text, Boolean isCorrect) {
        validateOptionText(text);
        
        if (isCorrect == null) {
            throw new IllegalArgumentException("Option correctness cannot be null");
        }
    }

    private void validateOptionText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Option text cannot be empty");
        }
        
        if (text.length() > 500) {
            throw new IllegalArgumentException("Option text cannot exceed 500 characters");
        }
    } public QuestionService() {
        this.questionDAO = new QuestionDAO();
        this.optionDAO = new OptionDAO();
        this.quizDAO = new QuizDAO();
    }

    public QuestionService(QuestionDAO questionDAO, OptionDAO optionDAO, QuizDAO quizDAO) {
        this.questionDAO = questionDAO;
        this.optionDAO = optionDAO;
        this.quizDAO = quizDAO;
    }

    public Question createQuestion(String text, String type, String explanation, 
                                   Double points, Integer orderIndex, Long quizId) {
        logger.info("Creating question for quiz id: {}", quizId);
        
        // Validate input
        validateQuestionInput(text, type, points);
        
        // Verify quiz exists
        Quiz quiz = quizDAO.findById(quizId);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
        
        // Create new question
        Question question = new Question();
        question.setText(text.trim());
        question.setType(type.toUpperCase());
        question.setExplanation(explanation != null ? explanation.trim() : null);
        question.setPoints(points != null ? points : 1.0);
        question.setOrderIndex(orderIndex);
        question.setQuiz(quiz);
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        
        Question savedQuestion = questionDAO.save(question);
        logger.info("Question created successfully with id: {}", savedQuestion.getId());
        return savedQuestion;
    }

    public Question getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Question ID cannot be null");
        }
        
        Question question = questionDAO.findById(id);
        if (question == null) {
            throw new QuestionNotFoundException("Question not found with id: " + id);
        }
        
        return question;
    }

    public List<Question> getQuestionsByQuiz(Long quizId) {
        if (quizId == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null");
        }
        
        Quiz quiz = quizDAO.findById(quizId);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
        
        return questionDAO.findByQuiz(quiz);
    }

    public List<Question> getQuestionsByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Question type cannot be empty");
        }
        
        return questionDAO.findByType(type.toUpperCase());
    }

    public Question updateQuestion(Long id, String text, String type, String explanation, 
                                   Double points, Integer orderIndex) {
        Question question = getQuestionById(id);
        
        boolean isModified = false;
        
        if (text != null && !text.trim().isEmpty() && !text.equals(question.getText())) {
            validateQuestionText(text);
            question.setText(text.trim());
            isModified = true;
        }
        
        if (type != null && !type.trim().isEmpty() && !type.toUpperCase().equals(question.getType())) {
            validateQuestionType(type);
            question.setType(type.toUpperCase());
            isModified = true;
        }
        
        if (explanation != null && !explanation.equals(question.getExplanation())) {
            question.setExplanation(explanation.trim().isEmpty() ? null : explanation.trim());
            isModified = true;
        }
        
        if (points != null && !points.equals(question.getPoints())) {
            validatePoints(points);
            question.setPoints(points);
            isModified = true;
        }
        
        if (orderIndex != null && !orderIndex.equals(question.getOrderIndex())) {
            question.setOrderIndex(orderIndex);
            isModified = true;
        }
        
        if (isModified) {
            question.setUpdatedAt(LocalDateTime.now());
            Question updatedQuestion = questionDAO.update(question);
            logger.info("Question updated successfully with id: {}", id);
            return updatedQuestion;
        }
        
        return question;
    }

    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        
        // Delete associated options first
        List<Option> options = optionDAO.findByQuestion(question);
        for (Option option : options) {
            optionDAO.delete(option.getId());
        }
        
        questionDAO.delete(id);
        logger.info("Question and associated options deleted successfully with id: {}", id);
    }

    public Option addOptionToQuestion(Long questionId, String text, Boolean isCorrect) {
        logger.info("Adding option to question id: {}", questionId);
        
        Question question = getQuestionById(questionId);
        
        // Validate option input
        validateOptionInput(text, isCorrect);
        
        // For single choice questions, ensure only one correct answer
        if ("SINGLE_CHOICE".equals(question.getType()) && isCorrect) {
            List<Option> existingOptions = optionDAO.findByQuestion(question);
            for (Option option : existingOptions) {
                if (option.getIsCorrect()) {
                    throw new IllegalStateException("Single choice question can have only one correct answer");
                }
            }
        }
        
        Option option = new Option();
        option.setText(text.trim());
        option.setIsCorrect(isCorrect);
        option.setQuestion(question);
        
        Option savedOption = optionDAO.save(option);
        logger.info("Option added successfully with id: {}", savedOption.getId());
        return savedOption;
    }

    public Option updateOption(Long optionId, String text, Boolean isCorrect) {
        Option option = optionDAO.findById(optionId);
        if (option == null) {
            throw new IllegalArgumentException("Option not found with id: " + optionId);
        }
        
        Question question = option.getQuestion();
        
        // For single choice questions, ensure only one correct answer
        if ("SINGLE_CHOICE".equals(question.getType()) && isCorrect && !option.getIsCorrect()) {
            List<Option> existingOptions = optionDAO.findByQuestion(question);
            for (Option existingOption : existingOptions) {
                if (existingOption.getIsCorrect() && !existingOption.getId().equals(optionId)) {
                    throw new IllegalStateException("Single choice question can have only one correct answer");
                }
            }
        }
        
        boolean isModified = false;
        
        if (text != null && !text.trim().isEmpty() && !text.equals(option.getText())) {
            validateOptionText(text);
            option.setText(text.trim());
            isModified = true;
        }
        
        if (isCorrect != null && !isCorrect.equals(option.getIsCorrect())) {
            option.setIsCorrect(isCorrect);
            isModified = true;
        }
        
        if (isModified) {
            Option updatedOption = optionDAO.update(option);
            logger.info("Option updated successfully with id: {}", optionId);
            return updatedOption;
        }
        
        return option;
    }
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionDAO.getQuestionsByQuizId(quizId);
    }
    
 // Overload 1: Without displayOrder
    public void addOptionToQuestion(Long questionId, String optionText, boolean isCorrect) {
        addOptionToQuestion(questionId, optionText, isCorrect, null);
    }

    // Overload 2: With displayOrder
    public void addOptionToQuestion(Long questionId, String optionText, boolean isCorrect, Integer displayOrder) {
        List<Question> question = questionDAO.getQuestionsByQuizId(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        Option option = new Option();
        option.setOptionText(optionText);
        option.setIsCorrect(isCorrect);
        option.setDisplayOrder(displayOrder);
        option.setQuestion((Question) question);

        // ✅ Correct method call to DAO
        optionDAO.save(option);
    }



}
