package com.quizapp;

import com.quizapp.entity.*;
import com.quizapp.service.*;
import com.quizapp.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizApplication {
    private static final Logger logger = LoggerFactory.getLogger(QuizApplication.class);

    private static final UserService userService = new UserService();
    private static final QuizService quizService = new QuizService();
    private static final QuestionService questionService = new QuestionService();
    private static final QuizAttemptService quizAttemptService = new QuizAttemptService();
    private static final Scanner scanner = new Scanner(System.in);

    private static User currentUser = null;
    
    // Flag to track if this is the first time a user is accessing the interface
    private static boolean isFirstUserAccess = true;

    public static void main(String[] args) {
        logger.info("Starting Quiz Application...");
        
        System.out.println("🚀 === QUIZ APPLICATION STARTING ===");
        System.out.println("📝 Setting up initial data...");

        try {
            initializeSampleData();
            
            System.out.println("✅ Application ready!");
            System.out.println("💡 Test Accounts Available:");
            System.out.println("   🔧 Admin: username=admin, password=admin");
            System.out.println("   👤 User: username=testuser, password=test123");
            System.out.println("   📝 Or register a new account!");
            System.out.println("📋 Sample Quizzes Available:");
            System.out.println("   🎯 Java Basics Quiz (5 questions)");
            System.out.println("   🎯 Database Fundamentals (3 questions)");
            
            runApplication();
        } catch (Exception e) {
            logger.error("Application error: ", e);
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void runApplication() {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                // Based on user role, show different interfaces
                if ("ADMIN".equals(currentUser.getRole())) {
                    showAdminMenu();
                } else {
                    showUserQuizInterface();
                }
            }
        }
    }

    private static void showLoginMenu() {
        while (true) {
            System.out.println("\n=== QUIZ APPLICATION ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Admin Tools (Troubleshooting)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number like 1, 2, 3, or 4.");
                continue;
            }

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    showAdminTools();
                    break;
                case 4:
                    System.out.println("👋 Exiting application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
    }

    private static void showAdminTools() {
        System.out.println("\n=== ADMIN TOOLS & TROUBLESHOOTING ===");
        System.out.println("1. Activate Admin Account");
        System.out.println("2. Activate User Account");
        System.out.println("3. List All Users (Debug)");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        String input = scanner.nextLine();
        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
            return;
        }

        switch (choice) {
            case 1:
                try {
                    activateAdminAccount();
                } catch (Exception e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
                break;
            case 2:
                System.out.print("Enter username to activate: ");
                String username = scanner.nextLine();
                try {
                    activateUserAccount(username);
                } catch (Exception e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    debugListUsers();
                } catch (Exception e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
                break;
            case 4:
                return;
            default:
                System.out.println("⚠️ Invalid choice.");
        }
    }

    private static void activateUserAccount(String username) {
        try {
            System.out.println("🔧 Attempting to activate user: " + username);
            User user = userService.getUserByUsername(username);
            
            if (user != null) {
                try {
                    // Try direct method call first (most reliable)
                    try {
                        userService.activateUser(user.getId());
                        System.out.println("✅ User '" + username + "' activated successfully via activateUser!");
                        return;
                    } catch (Exception directMethodError) {
                        // Fall back to other methods if direct call fails
                        System.out.println("⚠️ Direct activation failed, trying alternative methods...");
                    }
                    
                    // Try using the setIsActive method directly
                    try {
                        user.setIsActive(true);
                        userService.updateUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toString());
                        System.out.println("✅ User '" + username + "' activated successfully via direct method call!");
                        return;
                    } catch (Exception directCallError) {
                        // Fall back to reflection if direct call fails
                        System.out.println("⚠️ Direct method call failed, trying reflection...");
                    }
                    
                    // Last resort: use reflection
                    if (hasMethod(user, "setIsActive")) {
                        // Use boolean.class (primitive) not Boolean.class (wrapper)
                        user.getClass().getMethod("setIsActive", boolean.class).invoke(user, true);
                        
                        if (hasMethod(userService, "updateUser")) {
                            userService.getClass().getMethod("updateUser", User.class).invoke(userService, user);
                        } else if (hasMethod(userService, "saveUser")) {
                            userService.getClass().getMethod("saveUser", User.class).invoke(userService, user);
                        }
                        
                        System.out.println("✅ User '" + username + "' activated successfully via reflection!");
                    } else {
                        System.out.println("⚠️ Cannot activate - setIsActive method not found");
                    }
                } catch (Exception e) {
                    System.out.println("❌ Activation failed: " + e.getMessage());
                }
            } else {
                System.out.println("❌ User '" + username + "' not found");
            }
        } catch (Exception e) {
            System.out.println("❌ Could not find user: " + e.getMessage());
        }
    }

    private static void debugListUsers() {
        System.out.println("\n=== USER DEBUG INFO ===");
        try {
            // Get all users first
            System.out.println("📋 Attempting to list all users in the system...");
            List<User> allUsers = userService.getAllUsers();
            System.out.println("📊 Total users in database: " + allUsers.size());
            
            if (allUsers.isEmpty()) {
                System.out.println("⚠️ No users found in the database!");
                System.out.println("💡 You may need to restart the application to initialize sample data.");
                return;
            }
            
            // Display all users
            System.out.println("\n=== ALL USERS ===");
            for (User user : allUsers) {
                System.out.println("👤 User ID: " + user.getId());
                System.out.println("   Username: " + user.getUsername());
                System.out.println("   Email: " + user.getEmail());
                System.out.println("   Role: " + user.getRole());
                System.out.println("   Active: " + user.getIsActive());
                System.out.println("   Created: " + user.getCreatedAt());
                System.out.println();
            }
            
            // Try to get admin user specifically
            try {
                User admin = userService.getUserByUsername("admin");
                if (admin != null) {
                    System.out.println("\n=== ADMIN USER DETAILS ===");
                    System.out.println("👤 Admin found:");
                    System.out.println("   ID: " + admin.getId());
                    System.out.println("   Username: " + admin.getUsername());
                    System.out.println("   Email: " + admin.getEmail());
                    System.out.println("   Role: " + admin.getRole());
                    System.out.println("   Active: " + admin.getIsActive());
                    System.out.println("   Created: " + admin.getCreatedAt());
                }
            } catch (Exception e) {
                System.out.println("⚠️ Admin user lookup failed: " + e.getMessage());
            }
            
            // Try to get test user specifically
            try {
                User testUser = userService.getUserByUsername("testuser");
                if (testUser != null) {
                    System.out.println("\n=== TEST USER DETAILS ===");
                    System.out.println("👤 Test user found:");
                    System.out.println("   ID: " + testUser.getId());
                    System.out.println("   Username: " + testUser.getUsername());
                    System.out.println("   Email: " + testUser.getEmail());
                    System.out.println("   Role: " + testUser.getRole());
                    System.out.println("   Active: " + testUser.getIsActive());
                    System.out.println("   Created: " + testUser.getCreatedAt());
                }
            } catch (Exception e) {
                System.out.println("⚠️ Test user lookup failed: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error listing users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void login() {
        System.out.println("\n=== USER LOGIN ===");
        
        // Get username with validation
        String username = "";
        while (username.trim().isEmpty()) {
            System.out.print("Username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("❌ Username cannot be empty");
            }
        }
        
        // Get password with validation
        String password = "";
        while (password.isEmpty()) {
            System.out.print("Password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("❌ Password cannot be empty");
            }
        }

        try {
            // First check if the user exists in the database
            User user = null;
            try {
                user = userService.getUserByUsername(username);
                if (user == null) {
                    System.out.println("❌ Login failed: User not found");
                    return;
                }
            } catch (Exception e) {
                System.out.println("❌ Login failed: User not found");
                return;
            }
            
            // Now authenticate with password
            try {
                user = userService.authenticateUser(username, password);
                
                // Check if user is active
                if (!user.getIsActive()) {
                    System.out.println("❌ Login failed: Account is inactive");
                    
                    // Provide additional help for inactive account
                    System.out.println("💡 Your account is inactive. Possible solutions:");
                    System.out.println("   1. Contact admin to activate your account");
                    System.out.println("   2. Try registering again if this is a new account");
                    
                    // Offer to activate if admin credentials
                    if ("admin".equals(username.toLowerCase())) {
                        System.out.print("🔧 Attempt admin account activation? (y/n): ");
                        String choice = scanner.nextLine();
                        if (choice.toLowerCase().startsWith("y")) {
                            try {
                                // Try to manually activate admin account
                                activateAdminAccount();
                                
                                // Try login again after activation
                                System.out.println("🔄 Trying login again after activation...");
                                user = userService.authenticateUser(username, password);
                                if (!user.getIsActive()) {
                                    System.out.println("❌ Account still inactive. Please contact system administrator.");
                                    return;
                                }
                                
                                // If we get here, login after activation is successful
                                currentUser = user;
                                System.out.println("✅ Login successful! Welcome " + user.getUsername());
                                
                                // Check user role and redirect accordingly
                                if (user.getRole().toString().equals("ADMIN")) {
                                    System.out.println("🔧 Admin access granted. Redirecting to admin panel...");
                                } else {
                                    System.out.println("📚 Redirecting to quiz interface...");
                                    System.out.println("💡 You'll be automatically taken to a quiz");
                                }
                                
                                // Return to allow main loop to handle redirection
                                return;
                            } catch (Exception activationError) {
                                System.out.println("❌ Admin activation failed: " + activationError.getMessage());
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                
                // If we get here, login is successful
                currentUser = user;
                System.out.println("✅ Login successful! Welcome " + user.getUsername());

                // Check user role and redirect accordingly
                if (user.getRole().toString().equals("ADMIN")) {
                    System.out.println("🔧 Admin access granted. Redirecting to admin panel...");
                } else {
                    System.out.println("📚 Redirecting to quiz interface...");
                    System.out.println("💡 You'll be automatically taken to a quiz");
                    
                    // Return from login method to allow main loop to handle redirection
                    return;
                }
                
                // Important: Return from the method to allow the main application loop to handle redirection
                return;
                
            } catch (Exception e) {
                // Handle specific authentication errors
                if (e.getMessage().contains("Invalid credentials")) {
                    System.out.println("❌ Login failed: Incorrect password");
                    System.out.println("💡 Please check your password and try again");
                } else if (e.getMessage().toLowerCase().contains("inactive")) {
                    System.out.println("❌ Login failed: Account is inactive");
                    System.out.println("💡 Your account is inactive. Please contact admin to activate it.");
                    
                    // Offer to activate if admin credentials
                    if ("admin".equals(username.toLowerCase())) {
                        System.out.print("🔧 Attempt admin account activation? (y/n): ");
                        String choice = scanner.nextLine();
                        if (choice.toLowerCase().startsWith("y")) {
                            try {
                                activateAdminAccount();
                                System.out.println("🔄 Please try logging in again");
                            } catch (Exception activationError) {
                                System.out.println("❌ Admin activation failed: " + activationError.getMessage());
                            }
                        }
                    }
                } else {
                    System.out.println("❌ Login failed: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Login failed: " + e.getMessage());
        }
    }

    private static void activateAdminAccount() {
        try {
            System.out.println("🔧 Attempting to activate admin account...");
            User admin = userService.getUserByUsername("admin");
            
            if (admin != null) {
                // Print current status
                System.out.println("📊 Current admin status - Username: " + admin.getUsername() + 
                                  ", Active: " + admin.getIsActive() + 
                                  ", Role: " + admin.getRole());
                
                // Try direct method call first (most reliable)
                try {
                    userService.activateUser(admin.getId());
                    System.out.println("✅ Admin account activated successfully via activateUser!");
                    System.out.println("💡 Try logging in again");
                    return;
                } catch (Exception directMethodError) {
                    // Fall back to other methods if direct call fails
                    System.out.println("⚠️ Direct activation failed: " + directMethodError.getMessage());
                    System.out.println("⚠️ Trying alternative methods...");
                }
                
                // Try using the setIsActive method directly
                try {
                    admin.setIsActive(true);
                    userService.updateUser(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole().toString());
                    System.out.println("✅ Admin account activated successfully via direct method call!");
                    System.out.println("💡 Try logging in again");
                    return;
                } catch (Exception directCallError) {
                    // Fall back to reflection if direct call fails
                    System.out.println("⚠️ Direct method call failed: " + directCallError.getMessage());
                    System.out.println("⚠️ Trying reflection...");
                }
                
                // Last resort: use reflection
                try {
                    if (hasMethod(admin, "setIsActive")) {
                        // Use boolean.class (primitive) not Boolean.class (wrapper)
                        admin.getClass().getMethod("setIsActive", boolean.class).invoke(admin, true);
                        
                        // Try to save the updated user with the correct method
                        try {
                            userService.updateUser(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole().toString());
                            System.out.println("✅ Admin account activated and saved successfully!");
                        } catch (Exception updateError) {
                            System.out.println("⚠️ Error updating user after activation: " + updateError.getMessage());
                            
                            // Try alternative update methods
                            if (hasMethod(userService, "updateUser")) {
                                try {
                                    userService.getClass().getMethod("updateUser", Long.class, String.class, String.class, String.class)
                                        .invoke(userService, admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole().toString());
                                    System.out.println("✅ Admin account updated via reflection!");
                                } catch (Exception e) {
                                    System.out.println("⚠️ Reflection update failed: " + e.getMessage());
                                }
                            }
                        }
                        
                        System.out.println("✅ Admin account activation process completed!");
                        System.out.println("💡 Try logging in again");
                    } else {
                        System.out.println("⚠️ Cannot activate - setIsActive method not found");
                    }
                } catch (Exception e) {
                    System.out.println("❌ Activation failed: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Could not find admin user");
            }
        } catch (Exception e) {
            System.out.println("❌ Could not find admin user: " + e.getMessage());
        }
    }

    private static void showAdminMenu() {
        while (currentUser != null) {
            System.out.println("\n=== ADMIN PANEL - " + currentUser.getUsername().toUpperCase() + " ===");
            System.out.println("1. Create New Quiz");
            System.out.println("2. View All Quizzes");
            System.out.println("3. View All Users");
            System.out.println("4. View Quiz Statistics");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    createQuiz();
                    break;
                case 2:
                    viewAllQuizzes();
                    break;
                case 3:
                    viewAllUsers();
                    break;
                case 4:
                    viewQuizStatistics();
                    break;
                case 5:
                    logout();
                    return;
                default:
                    System.out.println("⚠️ Invalid choice. Please select 1-5.");
            }
        }
    }

    private static void showUserQuizInterface() {
        // Show a welcome message the first time
        System.out.println("\n🎉 Welcome to the Quiz Application, " + currentUser.getUsername() + "!");
        System.out.println("📝 Here you can take quizzes and track your progress.");
        
        // Automatically start a quiz on first entry after login
        if (isFirstUserAccess) {
            System.out.println("\n💡 Starting a quiz automatically as requested...");
            takeQuiz();
            isFirstUserAccess = false;
        }
        
        while (currentUser != null) {
            System.out.println("\n=== QUIZ INTERFACE - " + currentUser.getUsername().toUpperCase() + " ===");
            System.out.println("1. View Available Quizzes");
            System.out.println("2. Take a Quiz");
            System.out.println("3. View My Quiz Attempts");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAvailableQuizzes();
                    break;
                case 2:
                    takeQuiz();
                    break;
                case 3:
                    viewMyAttempts();
                    break;
                case 4:
                    logout();
                    return;
                default:
                    System.out.println("⚠️ Invalid choice. Please select 1-4.");
            }
        }
    }

    private static void logout() {
        System.out.println("👋 Logged out successfully. Goodbye " + currentUser.getUsername() + "!");
        currentUser = null;
        
        // Reset the first access flag so that when a user logs in again, they'll be taken to a quiz
        isFirstUserAccess = true;
    }

    private static void register() {
        System.out.println("\n=== USER REGISTRATION ===");
        
        // Get user input with validation
        String username = "";
        while (username.trim().isEmpty()) {
            System.out.print("Username (min 3 characters): ");
            username = scanner.nextLine().trim();
            if (username.length() < 3) {
                System.out.println("❌ Username must be at least 3 characters long");
                username = "";
            }
        }
        
        String email = "";
        while (email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("❌ Please enter a valid email address");
                email = "";
            }
        }
        
        String password = "";
        while (password.length() < 6) {
            System.out.print("Password (min 6 characters): ");
            password = scanner.nextLine();
            if (password.length() < 6) {
                System.out.println("❌ Password must be at least 6 characters long");
            }
        }

        try {
            System.out.println("🔄 Processing registration for: " + username);
            
            // Check if username already exists in database
            boolean usernameExists = false;
            try {
                User existingUser = userService.getUserByUsername(username);
                if (existingUser != null) {
                    System.out.println("❌ Username already exists. Please choose another username.");
                    usernameExists = true;
                    return;
                }
            } catch (Exception e) {
                // Username doesn't exist, which is good for registration
                System.out.println("✅ Username is available");
            }
            
            if (usernameExists) {
                return;
            }
            
            // Check if email already exists in database
            boolean emailExists = false;
            try {
                User existingUser = userService.getUserByEmail(email);
                if (existingUser != null) {
                    System.out.println("❌ Email already exists. Please use another email or try to recover your account.");
                    emailExists = true;
                    return;
                }
            } catch (Exception e) {
                // Email doesn't exist, which is good for registration
                System.out.println("✅ Email is available");
            }
            
            if (emailExists) {
                return;
            }
            
            // Create the user
            System.out.println("🔄 Creating new user account...");
            User user = userService.createUser(username, email, password, "USER");
            
            // Ensure user is active
            if (user != null) {
                System.out.println("✅ User created with ID: " + user.getId());
                
                // Directly use the UserService method to activate the user
                try {
                    System.out.println("🔄 Activating account...");
                    userService.activateUser(user.getId());
                    System.out.println("🔓 Account activated successfully");
                } catch (Exception activationError) {
                    System.out.println("⚠️ Account created but activation failed: " + activationError.getMessage());
                    
                    // Fallback to direct method call if available
                    try {
                        System.out.println("🔄 Trying fallback activation...");
                        user.setIsActive(true);
                        userService.updateUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toString());
                        System.out.println("🔓 Account activated via fallback method");
                    } catch (Exception fallbackError) {
                        System.out.println("⚠️ Fallback activation failed: " + fallbackError.getMessage());
                        System.out.println("💡 Please contact admin to activate your account");
                    }
                }
                
                // Verify the user was actually saved and activated
                try {
                    User verifyUser = userService.getUserByUsername(username);
                    System.out.println("📊 User verification - Username: " + verifyUser.getUsername() + 
                                      ", Active: " + verifyUser.getIsActive() + 
                                      ", Role: " + verifyUser.getRole());
                } catch (Exception verifyError) {
                    System.out.println("⚠️ Could not verify user status: " + verifyError.getMessage());
                }
                
                System.out.println("✅ Registration successful!");
                System.out.println("🎉 You can now login and start taking quizzes!");
                
                // Ask if they want to login immediately
                System.out.print("Would you like to login now? (y/n): ");
                String loginChoice = scanner.nextLine();
                
                if (loginChoice.toLowerCase().startsWith("y")) {
                    // Attempt automatic login with database validation
                    try {
                        System.out.println("🔄 Attempting automatic login...");
                        User authenticatedUser = userService.authenticateUser(username, password);
                        if (authenticatedUser != null && authenticatedUser.getIsActive()) {
                            currentUser = authenticatedUser;
                            System.out.println("✅ Login successful! Welcome " + authenticatedUser.getUsername());
                            
                            if (authenticatedUser.getRole().toString().equals("ADMIN")) {
                                System.out.println("🔧 Admin access granted. Redirecting to admin panel...");
                            } else {
                                System.out.println("📚 Redirecting to quiz interface...");
                                System.out.println("💡 You'll be automatically taken to a quiz");
                            }
                            return; // Exit registration and go to main loop
                        } else {
                            System.out.println("⚠️ Auto-login failed: Account may be inactive");
                            System.out.println("💡 Please try logging in manually");
                        }
                    } catch (Exception loginError) {
                        System.out.println("⚠️ Auto-login failed: " + loginError.getMessage());
                        System.out.println("💡 Please try logging in manually");
                    }
                }
            } else {
                System.out.println("❌ Registration failed. Please try again later.");
            }
        } catch (Exception e) {
            System.out.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to check if a method exists
    private static boolean hasMethod(Object obj, String methodName) {
        try {
            java.lang.reflect.Method[] methods = obj.getClass().getMethods();
            for (java.lang.reflect.Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static void viewAvailableQuizzes() {
        try {
            System.out.println("\n=== AVAILABLE QUIZZES ===");
            List<Quiz> quizzes = quizService.getAllActiveQuizzes();
            
            if (quizzes.isEmpty()) {
                System.out.println("📝 No quizzes available at the moment.");
                System.out.println("💡 Please check back later or contact an administrator.");
                return;
            }

            System.out.println("📋 Here are the quizzes you can take:");
            System.out.println("----------------------------------------");
            
            for (Quiz quiz : quizzes) {
                System.out.println("🎯 Quiz #" + quiz.getId() + ": " + quiz.getTitle());
                System.out.println("   📖 Description: " + quiz.getDescription());
                
                // Show time limit if available
                if (quiz.getTimeLimit() != null) {
                    System.out.println("   ⏱️ Time Limit: " + quiz.getTimeLimit() + " seconds");
                }
                
                // Show question count if available
                try {
                    int questionCount = questionService.getQuestionCountByQuizId(quiz.getId());
                    System.out.println("   ❓ Questions: " + questionCount);
                } catch (Exception e) {
                    // Ignore if we can't get question count
                }
                
                System.out.println("   👤 Created by: " + quiz.getCreator().getUsername());
                System.out.println("----------------------------------------");
            }
            
            System.out.println("\n💡 To take a quiz, select option 2 from the main menu.");
            
        } catch (Exception e) {
            System.out.println("❌ Error loading quizzes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewAllQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            if (quizzes.isEmpty()) {
                System.out.println("📝 No quizzes found.");
                return;
            }

            System.out.println("\n=== ALL QUIZZES (ADMIN VIEW) ===");
            for (Quiz quiz : quizzes) {
                System.out.println("🎯 " + quiz.getId() + ". " + quiz.getTitle());
                System.out.println("   📖 Description: " + quiz.getDescription());
                System.out.println("   👤 Creator: " + quiz.getCreator().getUsername());
                System.out.println("   📊 Status: " + (quiz.getIsActive() ? "Active" : "Inactive"));
                if (quiz.getTimeLimit() != null) {
                    System.out.println("   ⏱️ Time Limit: " + quiz.getTimeLimit() + " seconds");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading quizzes: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        try {
            // Assuming you have a method to get all users
            System.out.println("\n=== ALL USERS (ADMIN VIEW) ===");
            System.out.println("👤 This feature would show all registered users");
            System.out.println("💡 Implement userService.getAllUsers() method for full functionality");
        } catch (Exception e) {
            System.out.println("❌ Error loading users: " + e.getMessage());
        }
    }

    private static void viewQuizStatistics() {
        try {
            System.out.println("\n=== QUIZ STATISTICS (ADMIN VIEW) ===");
            System.out.println("📊 This feature would show quiz attempt statistics");
            System.out.println("💡 Implement statistical queries for full functionality");
        } catch (Exception e) {
            System.out.println("❌ Error loading statistics: " + e.getMessage());
        }
    }

    private static void takeQuiz() {
        // First show available quizzes
        viewAvailableQuizzes();
        
        System.out.print("Enter Quiz ID to take (or 0 to go back): ");
        String input = scanner.nextLine();
        
        try {
            Long quizId = Long.parseLong(input);
            if (quizId == 0) {
                return;
            }

            Quiz quiz = quizService.getQuizById(quizId);
            if (quiz == null) {
                System.out.println("❌ Quiz not found.");
                return;
            }

            System.out.println("\n=== STARTING QUIZ: " + quiz.getTitle() + " ===");
            if (quiz.getTimeLimit() != null) {
                System.out.println("⏱️ Time Limit: " + quiz.getTimeLimit() + " seconds");
            }
            System.out.println("Press Enter to start...");
            scanner.nextLine();

            QuizAttempt attempt = quizAttemptService.startQuizAttempt(currentUser.getId(), quizId);

            int score = 0;
            List<Question> questions = questionService.getQuestionsByQuizId(quizId);
            List<UserAnswer> userAnswers = new ArrayList<>();

            for (int qIndex = 0; qIndex < questions.size(); qIndex++) {
                Question question = questions.get(qIndex);
                System.out.println("\n📝 Question " + (qIndex + 1) + "/" + questions.size());
                System.out.println("Q: " + question.getQuestionText());
                List<Option> options = questionService.getOptionsByQuestion(question.getId());

                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i).getOptionText());
                }

                System.out.print("Your answer (1-" + options.size() + "): ");
                String answerInput = scanner.nextLine();
                
                UserAnswer userAnswer = new UserAnswer(attempt, question);

                try {
                    int answerIndex = Integer.parseInt(answerInput) - 1;
                    
                    if (answerIndex >= 0 && answerIndex < options.size()) {
                        Option selectedOption = options.get(answerIndex);
                        userAnswer.setSelectedOption(selectedOption);

                        if (selectedOption.getIsCorrect()) {
                            score++;
                            System.out.println("✅ Correct!");
                        } else {
                            System.out.println("❌ Incorrect. The correct answer was: " + getCorrectAnswer(options));
                        }
                    } else {
                        userAnswer.setIsSkipped(true);
                        System.out.println("⚠️ Invalid option selected. Question skipped.");
                    }
                } catch (NumberFormatException e) {
                    userAnswer.setIsSkipped(true);
                    System.out.println("⚠️ Invalid input. Question skipped.");
                }

                userAnswers.add(userAnswer);
            }

            double percentage = questions.isEmpty() ? 0 : (score * 100.0 / questions.size());

            QuizAttempt completedAttempt = quizAttemptService.completeQuizAttempt(
                    attempt.getId(), userAnswers, score, percentage);

            System.out.println("\n🎉 === QUIZ COMPLETED ===");
            System.out.println("📊 Your Score: " + score + "/" + questions.size());
            System.out.println("📈 Percentage: " + String.format("%.2f", percentage) + "%");
            System.out.println("⏱️ Completed at: " + completedAttempt.getCompletedDate());
            
            if (percentage >= 70) {
                System.out.println("🏆 Congratulations! You passed!");
            } else {
                System.out.println("📚 Keep studying and try again!");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid quiz ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("❌ Error taking quiz: " + e.getMessage());
            logger.error("Error in takeQuiz", e);
        }
    }

    private static String getCorrectAnswer(List<Option> options) {
        for (Option option : options) {
            if (option.getIsCorrect()) {
                return option.getOptionText();
            }
        }
        return "Unknown";
    }

    private static void viewMyAttempts() {
        try {
            List<QuizAttempt> attempts = quizAttemptService.getAttemptsByUserId(currentUser.getId());
            if (attempts.isEmpty()) {
                System.out.println("📝 No quiz attempts found.");
                return;
            }

            System.out.println("\n=== YOUR QUIZ ATTEMPTS ===");
            for (QuizAttempt attempt : attempts) {
                System.out.println("🎯 Quiz: " + attempt.getQuiz().getTitle());
                System.out.println("📅 Date: " + attempt.getAttemptDate());
                System.out.println("📊 Score: " + attempt.getScore());
                System.out.println("📈 Percentage: " + String.format("%.2f", attempt.getPercentageScore()) + "%");
                System.out.println("✅ Status: " + (attempt.getIsCompleted() ? "Completed" : "In Progress"));
                System.out.println("---");
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading attempts: " + e.getMessage());
        }
    }

    private static void createQuiz() {
        System.out.println("\n=== CREATE NEW QUIZ ===");
        System.out.print("Quiz Title: ");
        String title = scanner.nextLine();
        System.out.print("Quiz Description: ");
        String description = scanner.nextLine();
        System.out.print("Time Limit (seconds, 0 for no limit): ");
        
        int timeLimit;
        try {
            timeLimit = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid time limit. Setting to no limit.");
            timeLimit = 0;
        }

        try {
            Quiz savedQuiz = quizService.createQuiz(
                    title,
                    description,
                    timeLimit > 0 ? timeLimit : null,
                    null,
                    null,
                    currentUser.getId()
            );

            System.out.print("Number of questions: ");
            int numQuestions;
            try {
                numQuestions = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Setting to 1 question.");
                numQuestions = 1;
            }

            for (int i = 0; i < numQuestions; i++) {
                System.out.println("\n📝 Question " + (i + 1) + ":");
                System.out.print("Question text: ");
                String questionText = scanner.nextLine();

                System.out.print("Points for this question: ");
                double points;
                try {
                    points = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid points. Setting to 1.0");
                    points = 1.0;
                }

                Question savedQuestion = questionService.createQuestion(
                        questionText, "MULTIPLE_CHOICE", questionText, points, i, savedQuiz.getId());

                System.out.print("Number of options: ");
                int numOptions;
                try {
                    numOptions = Integer.parseInt(scanner.nextLine());
                    if (numOptions < 2) {
                        System.out.println("⚠️ Minimum 2 options required. Setting to 2.");
                        numOptions = 2;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid number. Setting to 4 options.");
                    numOptions = 4;
                }

                for (int j = 0; j < numOptions; j++) {
                    System.out.print("Option " + (j + 1) + ": ");
                    String optionText = scanner.nextLine();
                    System.out.print("Is this correct? (y/n): ");
                    boolean isCorrect = scanner.nextLine().toLowerCase().startsWith("y");

                    questionService.addOptionToQuestion(savedQuestion.getId(), optionText, isCorrect);
                }
            }

            quizService.activateQuiz(savedQuiz.getId());
            System.out.println("🎉 Quiz created and activated successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error creating quiz: " + e.getMessage());
            logger.error("Error in createQuiz", e);
        }
    }

    private static void initializeSampleData() {
        try {
            User admin = null;
            try {
                admin = userService.getUserByUsername("admin");
            } catch (Exception ignored) {}

            if (admin == null) {
                admin = userService.createUser("admin", "admin@quiz.com", "admin", "ADMIN");
                
                // Ensure admin account is active using multiple approaches
                try {
                    if (hasMethod(admin, "setIsActive")) {
                        admin.getClass().getMethod("setIsActive", Boolean.class).invoke(admin, true);
                        System.out.println("🔓 Admin account activated via setIsActive");
                    }
                    
                    // Try to save the activated admin
                    if (hasMethod(userService, "updateUser")) {
                        userService.getClass().getMethod("updateUser", User.class).invoke(userService, admin);
                    } else if (hasMethod(userService, "saveUser")) {
                        userService.getClass().getMethod("saveUser", User.class).invoke(userService, admin);
                    }
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Admin activation failed: " + e.getMessage());
                }
                
                logger.info("Admin user created with username: admin, password: admin");
                System.out.println("🔧 Admin account created: username=admin, password=admin");
            } else {
                // Try to activate existing admin if inactive
                try {
                    if (hasMethod(admin, "getIsActive")) {
                        Boolean isActive = (Boolean) admin.getClass().getMethod("getIsActive").invoke(admin);
                        if (isActive == null || !isActive) {
                            System.out.println("⚠️ Existing admin account is inactive. Attempting activation...");
                            activateAdminAccount();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("💡 Could not check admin activation status");
                }
            }

            // Also create a sample regular user for testing
            User sampleUser = null;
            try {
                sampleUser = userService.getUserByUsername("testuser");
            } catch (Exception ignored) {
                sampleUser = userService.createUser("testuser", "test@quiz.com", "test123", "USER");
                
                // Activate sample user
                try {
                    if (hasMethod(sampleUser, "setIsActive")) {
                        sampleUser.getClass().getMethod("setIsActive", Boolean.class).invoke(sampleUser, true);
                        
                        if (hasMethod(userService, "updateUser")) {
                            userService.getClass().getMethod("updateUser", User.class).invoke(userService, sampleUser);
                        } else if (hasMethod(userService, "saveUser")) {
                            userService.getClass().getMethod("saveUser", User.class).invoke(userService, sampleUser);
                        }
                        System.out.println("🔓 Sample user activated");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Sample user activation failed: " + e.getMessage());
                }
                
                logger.info("Sample user created with username: testuser, password: test123");
                System.out.println("👤 Sample user created: username=testuser, password=test123");
            }

            List<Quiz> quizzes = quizService.getAllQuizzes();
            if (quizzes.isEmpty()) {
                System.out.println("📝 Creating sample quizzes for testing...");
                createSampleQuiz(admin);
                logger.info("Sample quizzes created");
                System.out.println("✅ Sample quizzes created successfully!");
                System.out.println("💡 Users can now take these quizzes after logging in.");
            } else {
                System.out.println("📝 Sample quizzes already exist: " + quizzes.size() + " quizzes found");
            }

        } catch (Exception e) {
            logger.warn("Error initializing sample data: " + e.getMessage());
        }
    }

    private static void createSampleQuiz(User admin) {
        try {
            // Create Java Basics Quiz
            Quiz javaQuiz = quizService.createQuiz(
                    "Java Basics Quiz",
                    "Test your knowledge of Java fundamentals",
                    900, // 15 minutes
                    null,
                    70.0, // passing score
                    admin.getId()
            );

            // Question 1
            Question q1 = questionService.createQuestion(
                    "Which of the following is not a Java keyword?",
                    "MULTIPLE_CHOICE", 
                    "The keyword 'implements' (with an 's') is used to implement interfaces, but 'implement' is not a keyword.",
                    1.0, 1, javaQuiz.getId());

            questionService.addOptionToQuestion(q1.getId(), "class", false, 1);
            questionService.addOptionToQuestion(q1.getId(), "interface", false, 2);
            questionService.addOptionToQuestion(q1.getId(), "implement", true, 3);
            questionService.addOptionToQuestion(q1.getId(), "extends", false, 4);

            // Question 2
            Question q2 = questionService.createQuestion(
                    "What is the default value of an int variable in Java?",
                    "MULTIPLE_CHOICE", 
                    "Primitive numeric types in Java default to 0 when declared as class members.",
                    1.0, 2, javaQuiz.getId());

            questionService.addOptionToQuestion(q2.getId(), "0", true, 1);
            questionService.addOptionToQuestion(q2.getId(), "null", false, 2);
            questionService.addOptionToQuestion(q2.getId(), "1", false, 3);
            questionService.addOptionToQuestion(q2.getId(), "undefined", false, 4);

            // Question 3
            Question q3 = questionService.createQuestion(
                    "Which of the following is a valid way to create an array in Java?",
                    "MULTIPLE_CHOICE", 
                    "Arrays can be created using the 'new' keyword followed by the type and size.",
                    1.0, 3, javaQuiz.getId());

            questionService.addOptionToQuestion(q3.getId(), "int array = new int[10];", true, 1);
            questionService.addOptionToQuestion(q3.getId(), "int array[10] = new int;", false, 2);
            questionService.addOptionToQuestion(q3.getId(), "array int = new int(10);", false, 3);
            questionService.addOptionToQuestion(q3.getId(), "int array = int[10];", false, 4);

            // Question 4
            Question q4 = questionService.createQuestion(
                    "What is the output of the following code?\nString s1 = \"Hello\";\nString s2 = \"Hello\";\nSystem.out.println(s1 == s2);",
                    "MULTIPLE_CHOICE", 
                    "String literals are stored in the string pool, so identical literals reference the same object.",
                    1.0, 4, javaQuiz.getId());

            questionService.addOptionToQuestion(q4.getId(), "true", true, 1);
            questionService.addOptionToQuestion(q4.getId(), "false", false, 2);
            questionService.addOptionToQuestion(q4.getId(), "Compilation error", false, 3);
            questionService.addOptionToQuestion(q4.getId(), "Runtime error", false, 4);

            // Question 5
            Question q5 = questionService.createQuestion(
                    "Which statement is true about Java?",
                    "MULTIPLE_CHOICE", 
                    "Java is platform-independent because it compiles to bytecode that runs on the JVM.",
                    1.0, 5, javaQuiz.getId());

            questionService.addOptionToQuestion(q5.getId(), "Java is a purely object-oriented language", false, 1);
            questionService.addOptionToQuestion(q5.getId(), "Java is platform-independent", true, 2);
            questionService.addOptionToQuestion(q5.getId(), "Java does not support multiple inheritance through classes", false, 3);
            questionService.addOptionToQuestion(q5.getId(), "Java does not support method overloading", false, 4);

            // Activate the Java quiz
            quizService.activateQuiz(javaQuiz.getId());
            System.out.println("✅ Java Basics Quiz created with 5 questions");

            // Create a second quiz on Database Concepts
            Quiz dbQuiz = quizService.createQuiz(
                    "Database Fundamentals",
                    "Test your knowledge of SQL and database concepts",
                    600, // 10 minutes
                    null,
                    70.0, // passing score
                    admin.getId()
            );

            // DB Question 1
            Question dbq1 = questionService.createQuestion(
                    "Which SQL statement is used to retrieve data from a database?",
                    "MULTIPLE_CHOICE", 
                    "SELECT is used to query and retrieve data from database tables.",
                    1.0, 1, dbQuiz.getId());

            questionService.addOptionToQuestion(dbq1.getId(), "SELECT", true, 1);
            questionService.addOptionToQuestion(dbq1.getId(), "UPDATE", false, 2);
            questionService.addOptionToQuestion(dbq1.getId(), "INSERT", false, 3);
            questionService.addOptionToQuestion(dbq1.getId(), "DELETE", false, 4);

            // DB Question 2
            Question dbq2 = questionService.createQuestion(
                    "What does SQL stand for?",
                    "MULTIPLE_CHOICE", 
                    "SQL stands for Structured Query Language.",
                    1.0, 2, dbQuiz.getId());

            questionService.addOptionToQuestion(dbq2.getId(), "Structured Query Language", true, 1);
            questionService.addOptionToQuestion(dbq2.getId(), "Simple Query Language", false, 2);
            questionService.addOptionToQuestion(dbq2.getId(), "Sequential Query Language", false, 3);
            questionService.addOptionToQuestion(dbq2.getId(), "Standard Query Language", false, 4);

            // DB Question 3
            Question dbq3 = questionService.createQuestion(
                    "Which of the following is not a valid SQL data type?",
                    "MULTIPLE_CHOICE", 
                    "ARRAY is not a standard SQL data type in most database systems.",
                    1.0, 3, dbQuiz.getId());

            questionService.addOptionToQuestion(dbq3.getId(), "VARCHAR", false, 1);
            questionService.addOptionToQuestion(dbq3.getId(), "BOOLEAN", false, 2);
            questionService.addOptionToQuestion(dbq3.getId(), "ARRAY", true, 3);
            questionService.addOptionToQuestion(dbq3.getId(), "DATE", false, 4);

            // Activate the DB quiz
            quizService.activateQuiz(dbQuiz.getId());
            System.out.println("✅ Database Fundamentals Quiz created with 3 questions");

        } catch (Exception e) {
            logger.error("Error creating sample quizzes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}