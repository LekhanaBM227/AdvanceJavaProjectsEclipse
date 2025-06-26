package com.quizapp.dao;

import com.quizapp.entity.User;
import com.quizapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public User save(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // CRITICAL: Ensure isActive is set before persisting
            // Force set isActive to true regardless of current value
            user.setIsActive(true);
            
            // Debug log to verify isActive is set
            logger.info("Saving user with isActive={}, username={}", user.getIsActive(), user.getUsername());
            
            session.persist(user);
            transaction.commit();
            logger.info("User saved successfully: {}", user.getUsername());
            return user;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            logger.error("Error finding user by id: {}", id, e);
            throw new RuntimeException("Error finding user", e);
        }
    }

    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user by username: {}", username, e);
            throw new RuntimeException("Error finding user by username", e);
        }
    }

    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user by email: {}", email, e);
            throw new RuntimeException("Error finding user by email", e);
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u ORDER BY u.createdAt DESC", User.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding all users", e);
            throw new RuntimeException("Error finding all users", e);
        }
    }

    public List<User> findActiveUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC", User.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding active users", e);
            throw new RuntimeException("Error finding active users", e);
        }
    }

    public User update(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Ensure isActive field is set
            if (user.getIsActive() == null) {
                user.setIsActive(Boolean.TRUE);
                logger.warn("User {} had null isActive value, setting to TRUE", user.getId());
            } else if (user.getIsActive() == false) {
                logger.warn("User {} is being updated with isActive=FALSE", user.getId());
            }
            
            // Debug log to verify isActive is set
            logger.info("Updating user with isActive={}, username={}, id={}", 
                       user.getIsActive(), user.getUsername(), user.getId());
            
            User mergedUser = (User) session.merge(user);
            transaction.commit();
            logger.info("User updated successfully: {}", user.getId());
            return mergedUser;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating user: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("User deleted successfully: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user: {}", id, e);
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public Long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting users", e);
            throw new RuntimeException("Error counting users", e);
        }
    }

    public Long countActiveUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.isActive = true", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting active users", e);
            throw new RuntimeException("Error counting active users", e);
        }
    }
}