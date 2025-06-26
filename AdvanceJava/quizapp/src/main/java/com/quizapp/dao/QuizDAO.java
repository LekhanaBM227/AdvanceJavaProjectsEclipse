package com.quizapp.dao;

import com.quizapp.entity.Quiz;
import com.quizapp.entity.User;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuizDAO {

    private static final Logger logger = LoggerFactory.getLogger(QuizDAO.class);

    public Quiz save(Quiz quiz) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(quiz);
            transaction.commit();
            logger.info("Quiz saved successfully: {}", quiz.getTitle());
            return quiz;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving quiz: {}", quiz.getTitle(), e);
            throw new RuntimeException("Error saving quiz", e);
        }
    }

    public Quiz findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Quiz.class, id);
        } catch (Exception e) {
            logger.error("Error finding quiz by id: {}", id, e);
            throw new RuntimeException("Error finding quiz", e);
        }
    }

    public Quiz findByIdWithQuestions(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery(
                "SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.id = :id",
                Quiz.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding quiz with questions by id: {}", id, e);
            throw new RuntimeException("Error finding quiz", e);
        }
    }

    public List<Quiz> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery("FROM Quiz q ORDER BY q.createdAt DESC", Quiz.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding all quizzes", e);
            throw new RuntimeException("Error finding quizzes", e);
        }
    }

    public List<Quiz> findActiveQuizzes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery(
                "FROM Quiz q WHERE q.isActive = true ORDER BY q.createdAt DESC",
                Quiz.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding active quizzes", e);
            throw new RuntimeException("Error finding active quizzes", e);
        }
    }

    public List<Quiz> findByCreatedBy(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery(
                "FROM Quiz q WHERE q.createdBy.id = :userId ORDER BY q.createdAt DESC",
                Quiz.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding quizzes by creator: {}", userId, e);
            throw new RuntimeException("Error finding quizzes", e);
        }
    }

    public Quiz update(Quiz quiz) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(quiz);
            transaction.commit();
            logger.info("Quiz updated successfully: {}", quiz.getTitle());
            return quiz;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating quiz: {}", quiz.getTitle(), e);
            throw new RuntimeException("Error updating quiz", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Quiz quiz = session.get(Quiz.class, id);
            if (quiz != null) {
                session.remove(quiz);
                logger.info("Quiz deleted successfully: {}", quiz.getTitle());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting quiz with id: {}", id, e);
            throw new RuntimeException("Error deleting quiz", e);
        }
    }

    public void updateTotalQuestions(Long quizId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Long> countQuery = session.createQuery(
                "SELECT COUNT(q) FROM Question q WHERE q.quiz.id = :quizId", Long.class);
            countQuery.setParameter("quizId", quizId);
            Long questionCount = countQuery.uniqueResult();

            Query updateQuery = session.createQuery(
                "UPDATE Quiz q SET q.totalQuestions = :count WHERE q.id = :quizId");
            updateQuery.setParameter("count", questionCount.intValue());
            updateQuery.setParameter("quizId", quizId);
            updateQuery.executeUpdate();

            transaction.commit();
            logger.info("Quiz total questions updated: {} questions for quiz {}", questionCount, quizId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating total questions for quiz: {}", quizId, e);
            throw new RuntimeException("Error updating quiz questions count", e);
        }
    }

    // Implementations of the missing methods

    public List<Quiz> findByCreator(User creator) {
        if (creator == null || creator.getId() == null) {
            throw new IllegalArgumentException("Creator or Creator ID cannot be null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery(
                "FROM Quiz q WHERE q.createdBy = :creator ORDER BY q.createdAt DESC",
                Quiz.class);
            query.setParameter("creator", creator);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding quizzes by creator: {}", creator.getId(), e);
            throw new RuntimeException("Error finding quizzes", e);
        }
    }

    public List<Quiz> searchByTitleOrDescription(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findActiveQuizzes();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            Query<Quiz> query = session.createQuery(
                "FROM Quiz q WHERE lower(q.title) LIKE :pattern OR lower(q.description) LIKE :pattern ORDER BY q.createdAt DESC",
                Quiz.class);
            query.setParameter("pattern", pattern);
            return query.list();
        } catch (Exception e) {
            logger.error("Error searching quizzes by term: {}", searchTerm, e);
            throw new RuntimeException("Error searching quizzes", e);
        }
    }

    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(q) FROM Quiz q", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting quizzes", e);
            throw new RuntimeException("Error counting quizzes", e);
        }
    }

    public long countActiveQuizzes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(q) FROM Quiz q WHERE q.isActive = true", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting active quizzes", e);
            throw new RuntimeException("Error counting active quizzes", e);
        }
    }

    public long countByCreator(User creator) {
        if (creator == null || creator.getId() == null) {
            throw new IllegalArgumentException("Creator or Creator ID cannot be null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(q) FROM Quiz q WHERE q.createdBy = :creator", Long.class);
            query.setParameter("creator", creator);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting quizzes by creator: {}", creator.getId(), e);
            throw new RuntimeException("Error counting quizzes", e);
        }
    }
}
