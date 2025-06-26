package com.quizapp.dao;

import com.quizapp.entity.Option;
import com.quizapp.entity.Question;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OptionDAO {

    private static final Logger logger = LoggerFactory.getLogger(OptionDAO.class);

    public Option save(Option option) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(option);
            transaction.commit();
            logger.info("Option saved successfully for question: {}", option.getQuestion().getId());
            return option;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving option", e);
            throw new RuntimeException("Error saving option", e);
        }
    }

    public Option findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Option.class, id);
        } catch (Exception e) {
            logger.error("Error finding option by id: {}", id, e);
            throw new RuntimeException("Error finding option", e);
        }
    }

    public List<Option> findByQuestion(Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Option> query = session.createQuery(
                "FROM Option o WHERE o.question = :question ORDER BY o.optionOrder",
                Option.class);
            query.setParameter("question", question);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding options by question id: {}", question.getId(), e);
            throw new RuntimeException("Error finding options", e);
        }
    }

    public Option update(Option option) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(option);
            transaction.commit();
            logger.info("Option updated successfully: {}", option.getId());
            return option;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating option: {}", option.getId(), e);
            throw new RuntimeException("Error updating option", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Option option = session.get(Option.class, id);
            if (option != null) {
                session.remove(option);
                logger.info("Option deleted successfully: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting option with id: {}", id, e);
            throw new RuntimeException("Error deleting option", e);
        }
    }

    public Long countByQuestion(Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(o) FROM Option o WHERE o.question = :question", Long.class);
            query.setParameter("question", question);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting options by question id: {}", question.getId(), e);
            throw new RuntimeException("Error counting options", e);
        }
    }
}
