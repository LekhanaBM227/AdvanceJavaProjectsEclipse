package com.quizapp.dao;

import com.quizapp.entity.Quiz;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.User;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuizAttemptDAO {
    private static final Logger logger = LoggerFactory.getLogger(QuizAttemptDAO.class);

    public QuizAttempt save(QuizAttempt quizAttempt) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(quizAttempt);
            transaction.commit();
            logger.info("Quiz attempt saved successfully for user: {} and quiz: {}", 
                       quizAttempt.getUser().getId(), quizAttempt.getQuiz().getId());
            return quizAttempt;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving quiz attempt", e);
            throw new RuntimeException("Error saving quiz attempt", e);
        }
    }

    public QuizAttempt findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(QuizAttempt.class, id);
        } catch (Exception e) {
            logger.error("Error finding quiz attempt by id: {}", id, e);
            throw new RuntimeException("Error finding quiz attempt", e);
        }
    }

    public QuizAttempt findByIdWithDetails(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT DISTINCT qa FROM QuizAttempt qa " +
                "LEFT JOIN FETCH qa.user " +
                "LEFT JOIN FETCH qa.quiz " +
                "LEFT JOIN FETCH qa.userAnswers ua " +
                "LEFT JOIN FETCH ua.question " +
                "WHERE qa.id = :id", QuizAttempt.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding quiz attempt with details by id: {}", id, e);
            throw new RuntimeException("Error finding quiz attempt with details", e);
        }
    }

    public List<QuizAttempt> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT qa FROM QuizAttempt qa " +
                "WHERE qa.user.id = :userId " +
                "ORDER BY qa.attemptDate DESC", QuizAttempt.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding quiz attempts by user id: {}", userId, e);
            throw new RuntimeException("Error finding quiz attempts by user", e);
        }
    }

    public List<QuizAttempt> findByQuizId(Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT qa FROM QuizAttempt qa " +
                "WHERE qa.quiz.id = :quizId " +
                "ORDER BY qa.attemptDate DESC", QuizAttempt.class);
            query.setParameter("quizId", quizId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding quiz attempts by quiz id: {}", quizId, e);
            throw new RuntimeException("Error finding quiz attempts by quiz", e);
        }
    }

    public List<QuizAttempt> findByUserAndQuiz(Long userId, Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT qa FROM QuizAttempt qa " +
                "WHERE qa.user.id = :userId AND qa.quiz.id = :quizId " +
                "ORDER BY qa.attemptDate DESC", QuizAttempt.class);
            query.setParameter("userId", userId);
            query.setParameter("quizId", quizId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding quiz attempts by user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error finding quiz attempts by user and quiz", e);
        }
    }

    public QuizAttempt findLatestAttempt(Long userId, Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT qa FROM QuizAttempt qa " +
                "WHERE qa.user.id = :userId AND qa.quiz.id = :quizId " +
                "ORDER BY qa.attemptDate DESC", QuizAttempt.class);
            query.setParameter("userId", userId);
            query.setParameter("quizId", quizId);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding latest quiz attempt for user {} and quiz {}", userId, quizId, e);
            throw new RuntimeException("Error finding latest quiz attempt", e);
        }
    }

    public Long countAttemptsByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.user.id = :userId", Long.class);
            query.setParameter("userId", userId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting quiz attempts by user: {}", userId, e);
            throw new RuntimeException("Error counting quiz attempts", e);
        }
    }

    public Long countAttemptsByQuiz(Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.quiz.id = :quizId", Long.class);
            query.setParameter("quizId", quizId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting quiz attempts by quiz: {}", quizId, e);
            throw new RuntimeException("Error counting quiz attempts", e);
        }
    }

    public Double getAverageScoreByQuiz(Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT AVG(qa.score) FROM QuizAttempt qa WHERE qa.quiz.id = :quizId", Double.class);
            query.setParameter("quizId", quizId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error calculating average score for quiz: {}", quizId, e);
            throw new RuntimeException("Error calculating average score", e);
        }
    }

    public QuizAttempt update(QuizAttempt quizAttempt) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(quizAttempt);
            transaction.commit();
            logger.info("Quiz attempt updated successfully: {}", quizAttempt.getId());
            return quizAttempt;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating quiz attempt", e);
            throw new RuntimeException("Error updating quiz attempt", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            QuizAttempt quizAttempt = session.get(QuizAttempt.class, id);
            if (quizAttempt != null) {
                session.remove(quizAttempt);
                logger.info("Quiz attempt deleted successfully: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting quiz attempt: {}", id, e);
            throw new RuntimeException("Error deleting quiz attempt", e);
        }
    }

    public List<QuizAttempt> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<QuizAttempt> query = session.createQuery(
                "SELECT qa FROM QuizAttempt qa ORDER BY qa.attemptDate DESC", QuizAttempt.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding all quiz attempts", e);
            throw new RuntimeException("Error finding all quiz attempts", e);
        }
    }
}