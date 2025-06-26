package com.quizapp.dao;

import com.quizapp.entity.Question;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.UserAnswer;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserAnswerDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserAnswerDAO.class);

    public UserAnswer save(UserAnswer userAnswer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(userAnswer);
            transaction.commit();
            logger.info("User answer saved successfully with id: {}", userAnswer.getId());
            return userAnswer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving user answer", e);
            throw new RuntimeException("Error saving user answer", e);
        }
    }

    public UserAnswer findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(UserAnswer.class, id);
        } catch (Exception e) {
            logger.error("Error finding user answer by id: {}", id, e);
            throw new RuntimeException("Error finding user answer", e);
        }
    }

    public UserAnswer findByIdWithDetails(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "SELECT DISTINCT ua FROM UserAnswer ua " +
                "LEFT JOIN FETCH ua.quizAttempt " +
                "LEFT JOIN FETCH ua.question " +
                "LEFT JOIN FETCH ua.selectedOption " +
                "WHERE ua.id = :id", UserAnswer.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user answer with details by id: {}", id, e);
            throw new RuntimeException("Error finding user answer", e);
        }
    }

    public List<UserAnswer> findByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId ORDER BY ua.answeredAt",
                UserAnswer.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding user answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error finding user answers", e);
        }
    }

    public List<UserAnswer> findByQuizAttemptIdWithDetails(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "SELECT DISTINCT ua FROM UserAnswer ua " +
                "LEFT JOIN FETCH ua.question " +
                "LEFT JOIN FETCH ua.selectedOption " +
                "WHERE ua.quizAttempt.id = :quizAttemptId " +
                "ORDER BY ua.answeredAt", UserAnswer.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding user answers with details by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error finding user answers", e);
        }
    }

    public UserAnswer findByQuizAttemptIdAndQuestionId(Long quizAttemptId, Long questionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.question.id = :questionId",
                UserAnswer.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            query.setParameter("questionId", questionId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user answer by quiz attempt id: {} and question id: {}", quizAttemptId, questionId, e);
            throw new RuntimeException("Error finding user answer", e);
        }
    }

    public List<UserAnswer> findByQuestionId(Long questionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.question.id = :questionId ORDER BY ua.answeredAt DESC",
                UserAnswer.class);
            query.setParameter("questionId", questionId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding user answers by question id: {}", questionId, e);
            throw new RuntimeException("Error finding user answers", e);
        }
    }

    public List<UserAnswer> findCorrectAnswersByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = true ORDER BY ua.answeredAt",
                UserAnswer.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding correct answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error finding correct answers", e);
        }
    }

    public List<UserAnswer> findIncorrectAnswersByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = false ORDER BY ua.answeredAt",
                UserAnswer.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding incorrect answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error finding incorrect answers", e);
        }
    }

    public List<UserAnswer> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery("FROM UserAnswer ua ORDER BY ua.answeredAt DESC", UserAnswer.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding all user answers", e);
            throw new RuntimeException("Error finding user answers", e);
        }
    }

    public UserAnswer update(UserAnswer userAnswer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(userAnswer);
            transaction.commit();
            logger.info("User answer updated successfully with id: {}", userAnswer.getId());
            return userAnswer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating user answer", e);
            throw new RuntimeException("Error updating user answer", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            UserAnswer userAnswer = session.get(UserAnswer.class, id);
            if (userAnswer != null) {
                session.remove(userAnswer);
                transaction.commit();
                logger.info("User answer deleted successfully with id: {}", id);
            } else {
                logger.warn("User answer not found with id: {}", id);
                throw new RuntimeException("User answer not found");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user answer with id: {}", id, e);
            throw new RuntimeException("Error deleting user answer", e);
        }
    }

    public void deleteByQuizAttemptId(Long quizAttemptId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId");
            query.setParameter("quizAttemptId", quizAttemptId);
            int deletedCount = query.executeUpdate();
            transaction.commit();
            logger.info("Deleted {} user answers for quiz attempt id: {}", deletedCount, quizAttemptId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error deleting user answers", e);
        }
    }

    public long countByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId", Long.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting user answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error counting user answers", e);
        }
    }

    public long countCorrectAnswersByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = true", Long.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting correct answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error counting correct answers", e);
        }
    }

    public long countIncorrectAnswersByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = false", Long.class);
            query.setParameter("quizAttemptId", quizAttemptId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting incorrect answers by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error counting incorrect answers", e);
        }
    }

    public Double getAccuracyByQuizAttemptId(Long quizAttemptId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> totalQuery = session.createQuery(
                "SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId", Long.class);
            totalQuery.setParameter("quizAttemptId", quizAttemptId);
            Long total = totalQuery.uniqueResult();

            if (total == 0) {
                return 0.0;
            }

            Query<Long> correctQuery = session.createQuery(
                "SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = true", Long.class);
            correctQuery.setParameter("quizAttemptId", quizAttemptId);
            Long correct = correctQuery.uniqueResult();

            return (correct.doubleValue() / total.doubleValue()) * 100.0;
        } catch (Exception e) {
            logger.error("Error calculating accuracy by quiz attempt id: {}", quizAttemptId, e);
            throw new RuntimeException("Error calculating accuracy", e);
        }
    }

    // New methods required by the service
    public UserAnswer findByAttemptAndQuestion(QuizAttempt attempt, Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.attempt.id = :attemptId AND ua.question.id = :questionId",
                UserAnswer.class);
            query.setParameter("attemptId", attempt.getId());
            query.setParameter("questionId", question.getId());
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user answer by attempt and question", e);
            throw new RuntimeException("Error finding user answer", e);
        }
    }

    public List<UserAnswer> findByAttempt(QuizAttempt attempt) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserAnswer> query = session.createQuery(
                "FROM UserAnswer ua WHERE ua.attempt.id = :attemptId ORDER BY ua.answeredAt",
                UserAnswer.class);
            query.setParameter("attemptId", attempt.getId());
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding user answers by attempt", e);
            throw new RuntimeException("Error finding user answers", e);
        }
    }
}