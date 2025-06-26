package com.quizapp.service;

import com.quizapp.dao.UserDAO;
import com.quizapp.entity.User;
import com.quizapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    // Remove userRepository - use userDAO instead

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(String username, String email, String password, String role) {
        logger.info("Creating user: {}", username);
        
        // Validate input
        validateUserInput(username, email, password);
        
        // Check if username already exists
        try {
            User existingUser = getUserByUsername(username);
            if (existingUser != null) {
                throw new IllegalArgumentException("Username already exists: " + username);
            }
        } catch (UserNotFoundException e) {
            // User doesn't exist, which is good for registration
        }
        
        // Check if email already exists
        try {
            User existingUser = getUserByEmail(email);
            if (existingUser != null) {
                throw new IllegalArgumentException("Email already exists: " + email);
            }
        } catch (UserNotFoundException e) {
            // Email doesn't exist, which is good for registration
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(password); // In real app, hash this password
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Explicitly set isActive to TRUE - this is critical for database insertion
        user.setIsActive(Boolean.TRUE);
        
        // Debug log to verify isActive is set
        logger.info("Creating user with isActive={}, username={}", user.getIsActive(), user.getUsername());
        
        // Set user role if provided
        if (role != null && !role.isEmpty()) {
            validateRole(role);
            user.setRole(User.UserRole.valueOf(role));
        }
        
        User savedUser = userDAO.save(user);
        logger.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    public User authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = getUserByUsername(username.trim());
        
        // In a real application, you would hash the password and compare hashes
        // Debug log to help troubleshoot password issues
        logger.debug("Comparing passwords - Input: '{}', Stored: '{}'", password, user.getPassword());
        
        if (!password.equals(user.getPassword())) {
            logger.warn("Password mismatch for user: {}", username);
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        if (!user.getIsActive()) {
            logger.warn("Inactive account for user: {}", username);
            throw new IllegalArgumentException("User account is inactive");
        }
        
        logger.info("User authenticated successfully: {}", username);
        return user;
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        User user = userDAO.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        
        return user;
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
        
        return user;
    }

    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        
        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getActiveUsers() {
        return userDAO.findActiveUsers();
    }

    public User updateUser(Long id, String username, String email, String role) {
        User user = getUserById(id);
        
        boolean isModified = false;
        
        if (username != null && !username.trim().isEmpty() && !username.equals(user.getUsername())) {
            validateUsername(username);
            // Check if new username already exists
            try {
                User existingUser = getUserByUsername(username);
                if (existingUser != null && !existingUser.getId().equals(id)) {
                    throw new IllegalArgumentException("Username already exists: " + username);
                }
            } catch (UserNotFoundException e) {
                // Username doesn't exist, which is good
            }
            user.setUsername(username.trim());
            isModified = true;
        }
        
        if (email != null && !email.trim().isEmpty() && !email.equals(user.getEmail())) {
            validateEmail(email);
            // Check if new email already exists
            try {
                User existingUser = getUserByEmail(email);
                if (existingUser != null && !existingUser.getId().equals(id)) {
                    throw new IllegalArgumentException("Email already exists: " + email);
                }
            } catch (UserNotFoundException e) {
                // Email doesn't exist, which is good
            }
            user.setEmail(email.trim().toLowerCase());
            isModified = true;
        }
        
        if (role != null && !role.equals(user.getRole().toString())) {
            validateRole(role);
            user.setRole(User.UserRole.valueOf(role));
            isModified = true;
        }
        
        if (isModified) {
            User updatedUser = userDAO.update(user);
            logger.info("User updated successfully with id: {}", id);
            return updatedUser;
        }
        
        return user;
    }

    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(Boolean.TRUE);
        userDAO.update(user);
        logger.info("User activated successfully with id: {}", id);
    }

    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(Boolean.FALSE);
        userDAO.update(user);
        logger.info("User deactivated successfully with id: {}", id);
    }

    public void deleteUser(Long id) {
        // Verify user exists before deletion (will throw UserNotFoundException if not found)
        getUserById(id);
        userDAO.delete(id);
        logger.info("User deleted successfully with id: {}", id);
    }

    public long getTotalUserCount() {
        return userDAO.count();
    }

    public long getActiveUserCount() {
        return userDAO.countActiveUsers();
    }

    private void validateUserInput(String username, String email, String password) {
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username cannot exceed 50 characters");
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email cannot exceed 100 characters");
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        if (password.length() > 100) {
            throw new IllegalArgumentException("Password cannot exceed 100 characters");
        }
    }

    private void validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        
        try {
            User.UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role must be either 'USER' or 'ADMIN'");
        }
    }
}