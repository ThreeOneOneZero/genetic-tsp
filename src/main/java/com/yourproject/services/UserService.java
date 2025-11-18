package com.yourproject.services;

import com.yourproject.models.User;
import com.yourproject.models.CreateUserRequest;
import com.yourproject.errors.BusinessException;
import com.yourproject.errors.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User Service with In-Memory Storage
 * 
 * This service stores data in runtime memory (List).
 * Perfect for:
 *   - Development and testing
 *   - Prototyping
 *   - Testing business logic without database
 * 
 * For production, integrate JPA/Database:
 *   - See DATABASE_SETUP.md
 *   - Replace List with UserRepository (JpaRepository)
 *   - Add @Transactional and database annotations
 */
@Service
public class UserService extends BaseService {
    
    // In-memory storage
    private static final List<User> users = new ArrayList<>();

    // Initialize with sample data
    static {
        users.add(new User(UUID.randomUUID().toString(), "John Doe", "john@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Jane Smith", "jane@example.com"));
    }

    /**
     * Create a new user
     * 
     * @param request CreateUserRequest with name and email
     * @return Created User
     * @throws BusinessException if email already exists
     */
    public User createUser(CreateUserRequest request) {
        logger.info("Creating user with email: {}", request.email());
        
        // Validation: Check if email already exists
        if (users.stream().anyMatch(u -> u.getEmail().equals(request.email()))) {
            throw new BusinessException("Email already exists");
        }

        User user = new User(
            UUID.randomUUID().toString(),
            request.name(),
            request.email()
        );

        users.add(user);
        logger.info("User created successfully: {}", user.getId());
        
        return user;
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User
     * @throws NotFoundException if user not found
     */
    public User getUser(String id) {
        logger.info("Fetching user: {}", id);
        
        return users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

    /**
     * Get all users
     * 
     * @return List of all users
     */
    public List<User> listUsers() {
        logger.info("Listing all users (total: {})", users.size());
        return new ArrayList<>(users);
    }

    /**
     * Update user
     * 
     * @param id User ID
     * @param request User data to update
     * @return Updated User
     * @throws NotFoundException if user not found
     * @throws BusinessException if new email already exists
     */
    public User updateUser(String id, CreateUserRequest request) {
        logger.info("Updating user: {}", id);
        
        User user = getUser(id);

        // Check if new email is not already used by another user
        if (!user.getEmail().equals(request.email()) &&
            users.stream().anyMatch(u -> u.getEmail().equals(request.email()))) {
            throw new BusinessException("Email already exists");
        }

        // Create updated user (since User is immutable, create new one)
        User updatedUser = new User(id, request.name(), request.email());
        
        // Remove old and add new
        users.removeIf(u -> u.getId().equals(id));
        users.add(updatedUser);
        
        logger.info("User updated: {}", id);
        return updatedUser;
    }

    /**
     * Delete user by ID
     * 
     * @param id User ID
     * @throws NotFoundException if user not found
     */
    public void deleteUser(String id) {
        logger.info("Deleting user: {}", id);
        
        if (!users.stream().anyMatch(u -> u.getId().equals(id))) {
            throw new NotFoundException("User not found");
        }

        users.removeIf(u -> u.getId().equals(id));
        logger.info("User deleted: {}", id);
    }

    /**
     * Get total user count
     * 
     * @return Total number of users
     */
    public long getTotalUsers() {
        return users.size();
    }

    /**
     * Clear all users (useful for testing)
     */
    public void clearAll() {
        logger.warn("Clearing all users");
        users.clear();
    }

    /**
     * Find user by email
     * 
     * @param email User email
     * @return User if found
     * @throws NotFoundException if user not found
     */
    public User findByEmail(String email) {
        logger.info("Finding user by email: {}", email);
        
        return users.stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
