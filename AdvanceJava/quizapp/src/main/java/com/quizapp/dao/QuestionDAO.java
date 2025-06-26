package com.quizapp.dao;

import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuestionDAO {

    private static final Logger logger = LoggerFactory.getLogger(QuestionDAO.class);

    public Question save(Question question) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(question);
            transaction.commit();
            logger.info("Question saved successfully for quiz: {}", question.getQuiz().getId());
            return question;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving question", e);
            throw new RuntimeException("Error saving question", e);
        }
    }

    public Question findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Question.class, id);
        } catch (Exception e) {
            logger.error("Error finding question by id: {}", id, e);
            throw new RuntimeException("Error finding question", e);
        }
    }

    public Question findByIdWithOptions(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                "SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.options WHERE q.id = :id",
                Question.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding question with options by id: {}", id, e);
            throw new RuntimeException("Error finding question", e);
        }
    }

    // Replaced findByQuizId with findByQuiz to match service usage
    public List<Question> findByQuiz(Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                "FROM Question q WHERE q.quiz = :quiz ORDER BY q.orderIndex", 
                Question.class);
            query.setParameter("quiz", quiz);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding questions by quiz id: {}", quiz.getId(), e);
            throw new RuntimeException("Error finding questions", e);
        }
    }

    // Added findByQuiz with options
    public List<Question> findByQuizWithOptions(Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                "SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.options o " +
                "WHERE q.quiz = :quiz ORDER BY q.orderIndex, o.optionOrder",
                Question.class);
            query.setParameter("quiz", quiz);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding questions with options by quiz id: {}", quiz.getId(), e);
            throw new RuntimeException("Error finding questions", e);
        }
    }

    public Long countByQuiz(Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(q) FROM Question q WHERE q.quiz = :quiz", Long.class);
            query.setParameter("quiz", quiz);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting questions by quiz id: {}", quiz.getId(), e);
            throw new RuntimeException("Error counting questions", e);
        }
    }

    public List<Question> findByType(String type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                "FROM Question q WHERE q.type = :type ORDER BY q.orderIndex",
                Question.class);
            query.setParameter("type", type);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding questions by type: {}", type, e);
            throw new RuntimeException("Error finding questions", e);
        }
    }

    public Question update(Question question) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(question);
            transaction.commit();
            logger.info("Question updated successfully: {}", question.getId());
            return question;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating question: {}", question.getId(), e);
            throw new RuntimeException("Error updating question", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Question question = session.get(Question.class, id);
            if (question != null) {
                session.remove(question);
                logger.info("Question deleted successfully: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting question with id: {}", id, e);
            throw new RuntimeException("Error deleting question", e);
        }
    }

    // You can keep or remove old methods like findByQuizId, findByQuizIdWithOptions if you want legacy support.
    // Just ensure your service uses the new methods consistently.
    public List<Question> getQuestionsByQuizId(Long quizId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Question> questions = session.createQuery("FROM Question WHERE quiz.id = :quizId", Question.class)
            .setParameter("quizId", quizId)
            .getResultList();
        session.close();
        return questions;
    }


}
