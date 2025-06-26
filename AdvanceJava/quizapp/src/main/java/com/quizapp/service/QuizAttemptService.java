package com.quizapp.service;

import com.quizapp.dao.QuizAttemptDAO;
import com.quizapp.dao.QuizDAO;
import com.quizapp.dao.UserDAO;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.User;
import com.quizapp.entity.UserAnswer;
import com.quizapp.entity.Question;
import com.quizapp.entity.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class QuizAttemptService {
    private static final Logger logger = LoggerFactory.getLogger(QuizAttemptService.class);

    private final QuizAttemptDAO quizAttemptDAO;
    private final QuizDAO quizDAO;
    private final UserDAO userDAO;

    public QuizAttemptService() {
        this.quizAttemptDAO = new QuizAttemptDAO();
        this.quizDAO = new QuizDAO();
        this.userDAO = new UserDAO();
    }

    public QuizAttemptService(QuizAttemptDAO quizAttemptDAO, QuizDAO quizDAO, UserDAO userDAO) {
        this.quizAttemptDAO = quizAttemptDAO;
        this.quizDAO = quizDAO;
        this.userDAO = userDAO;
    }

    public QuizAttempt startQuizAttempt(Long userId, Long quizId) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found with id: " + userId);
            }

            Quiz quiz = quizDAO.findById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Quiz not found with id: " + quizId);
            }

            QuizAttempt quizAttempt = new QuizAttempt();
            quizAttempt.setUser(user);
            quizAttempt.setQuiz(quiz);
            quizAttempt.setAttemptDate(LocalDateTime.now());
            quizAttempt.setIsCompleted(false);
            quizAttempt.setScore(0.0);

            return quizAttemptDAO.save(quizAttempt);

        } catch (Exception e) {
            logger.error("Error starting quiz attempt for user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error starting quiz attempt", e);
        }
    }

    public QuizAttempt submitQuizAttempt(Long attemptId, Map<Long, String> userAnswers) {
        try {
            QuizAttempt attempt = quizAttemptDAO.findByIdWithDetails(attemptId);
            if (attempt == null) {
                throw new IllegalArgumentException("Quiz attempt not found with id: " + attemptId);
            }

            if (Boolean.TRUE.equals(attempt.getIsCompleted())) {
                throw new IllegalStateException("Quiz attempt is already completed");
            }

            // Calculate score and save user answers
            int totalQuestions = attempt.getQuiz().getQuestions().size();
            int correctAnswers = 0;

            for (Question question : attempt.getQuiz().getQuestions()) {
                String userAnswerText = userAnswers.get(question.getId());

                UserAnswer answer = new UserAnswer();
                answer.setQuizAttempt(attempt);
                answer.setQuestion(question);
                
                // Handle different answer types
                boolean isCorrect = false;
                if (userAnswerText != null) {
                    // For multiple choice questions, check if selected option is correct
                    if ("SINGLE_CHOICE".equals(question.getType()) || "MULTIPLE_CHOICE".equals(question.getType())) {
                        try {
                            Long selectedOptionId = Long.parseLong(userAnswerText);
                            Option selectedOption = question.getOptions().stream()
                                    .filter(opt -> opt.getId().equals(selectedOptionId))
                                    .findFirst()
                                    .orElse(null);
                            
                            if (selectedOption != null) {
                                answer.setSelectedOption(selectedOption.getOptionText());
                                isCorrect = Boolean.TRUE.equals(selectedOption.getIsCorrect());
                            }
                        } catch (NumberFormatException e) {
                            // If not a number, treat as text answer
                            answer.setSelectedOption(userAnswerText);
                            isCorrect = userAnswerText.equals(question.getCorrectAnswer());
                        }
                    } else {
                        // For text or true/false questions
                        answer.setSelectedOption(userAnswerText);
                        isCorrect = userAnswerText.equals(question.getCorrectAnswer());
                    }
                }

                answer.setIsCorrect(isCorrect);

                if (isCorrect) {
                    correctAnswers++;
                }

                attempt.getUserAnswers().add(answer);
            }

            // Calculate percentage score
            double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
            attempt.setScore(score);
            attempt.setIsCompleted(true);
            attempt.setCompletedDate(LocalDateTime.now());

            return quizAttemptDAO.update(attempt);

        } catch (Exception e) {
            logger.error("Error submitting quiz attempt: {}", attemptId, e);
            throw new RuntimeException("Error submitting quiz attempt", e);
        }
    }

    public QuizAttempt getQuizAttemptById(Long id) {
        try {
            return quizAttemptDAO.findById(id);
        } catch (Exception e) {
            logger.error("Error getting quiz attempt by id: {}", id, e);
            throw new RuntimeException("Error getting quiz attempt", e);
        }
    }

    public QuizAttempt getQuizAttemptWithDetails(Long id) {
        try {
            return quizAttemptDAO.findByIdWithDetails(id);
        } catch (Exception e) {
            logger.error("Error getting quiz attempt with details: {}", id, e);
            throw new RuntimeException("Error getting quiz attempt with details", e);
        }
    }

    public List<QuizAttempt> getUserQuizAttempts(Long userId) {
        try {
            return quizAttemptDAO.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error getting quiz attempts for user: {}", userId, e);
            throw new RuntimeException("Error getting user quiz attempts", e);
        }
    }

    public List<QuizAttempt> getQuizAttempts(Long quizId) {
        try {
            return quizAttemptDAO.findByQuizId(quizId);
        } catch (Exception e) {
            logger.error("Error getting attempts for quiz: {}", quizId, e);
            throw new RuntimeException("Error getting quiz attempts", e);
        }
    }

    public List<QuizAttempt> getUserQuizAttempts(Long userId, Long quizId) {
        try {
            return quizAttemptDAO.findByUserAndQuiz(userId, quizId);
        } catch (Exception e) {
            logger.error("Error getting quiz attempts for user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error getting user quiz attempts", e);
        }
    }

    public QuizAttempt getLatestAttempt(Long userId, Long quizId) {
        try {
            return quizAttemptDAO.findLatestAttempt(userId, quizId);
        } catch (Exception e) {
            logger.error("Error getting latest attempt for user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error getting latest attempt", e);
        }
    }

    public boolean canUserRetakeQuiz(Long userId, Long quizId) {
        try {
            Quiz quiz = quizDAO.findById(quizId);
            if (quiz == null) {
                return false;
            }

            // If unlimited attempts allowed
            if (quiz.getMaxAttempts() == null || quiz.getMaxAttempts() <= 0) {
                return true;
            }

            List<QuizAttempt> attempts = quizAttemptDAO.findByUserAndQuiz(userId, quizId);
            return attempts.size() < quiz.getMaxAttempts();

        } catch (Exception e) {
            logger.error("Error checking if user can retake quiz", e);
            return false;
        }
    }

    public Long getUserAttemptCount(Long userId) {
        try {
            return quizAttemptDAO.countAttemptsByUser(userId);
        } catch (Exception e) {
            logger.error("Error getting attempt count for user: {}", userId, e);
            throw new RuntimeException("Error getting user attempt count", e);
        }
    }

    public Long getQuizAttemptCount(Long quizId) {
        try {
            return quizAttemptDAO.countAttemptsByQuiz(quizId);
        } catch (Exception e) {
            logger.error("Error getting attempt count for quiz: {}", quizId, e);
            throw new RuntimeException("Error getting quiz attempt count", e);
        }
    }

    public Double getQuizAverageScore(Long quizId) {
        try {
            return quizAttemptDAO.getAverageScoreByQuiz(quizId);
        } catch (Exception e) {
            logger.error("Error getting average score for quiz: {}", quizId, e);
            throw new RuntimeException("Error getting quiz average score", e);
        }
    }

    public void deleteQuizAttempt(Long attemptId) {
        try {
            quizAttemptDAO.delete(attemptId);
            logger.info("Quiz attempt deleted: {}", attemptId);
        } catch (Exception e) {
            logger.error("Error deleting quiz attempt: {}", attemptId, e);
            throw new RuntimeException("Error deleting quiz attempt", e);
        }
    }

    public List<QuizAttempt> getAllQuizAttempts() {
        try {
            return quizAttemptDAO.findAll();
        } catch (Exception e) {
            logger.error("Error getting all quiz attempts", e);
            throw new RuntimeException("Error getting all quiz attempts", e);
        }
    }

    public boolean hasUserAttemptedQuiz(Long userId, Long quizId) {
        try {
            List<QuizAttempt> attempts = quizAttemptDAO.findByUserAndQuiz(userId, quizId);
            return !attempts.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking if user has attempted quiz", e);
            return false;
        }
    }

    public QuizAttempt getBestAttempt(Long userId, Long quizId) {
        try {
            List<QuizAttempt> attempts = quizAttemptDAO.findByUserAndQuiz(userId, quizId);
            return attempts.stream()
                    .filter(attempt -> Boolean.TRUE.equals(attempt.getIsCompleted()))
                    .max((a1, a2) -> Double.compare(a1.getScore(), a2.getScore()))
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error getting best attempt for user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error getting best attempt", e);
        }
    }

	public List<QuizAttempt> getAttemptsByUserId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public QuizAttempt completeQuizAttempt(Long id, List<UserAnswer> userAnswers, int score, double percentage) {
		// TODO Auto-generated method stub
		return null;
	}
}