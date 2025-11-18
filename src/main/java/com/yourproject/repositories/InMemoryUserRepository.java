package com.yourproject.repositories;

import com.yourproject.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * In-Memory Repository for User Entity
 * 
 * Use this for development/testing WITHOUT database.
 * For production, replace with JPA Repository.
 * 
 * Example:
 *   @Repository
 *   public interface UserRepository extends JpaRepository<User, String> {
 *       Optional<User> findByEmail(String email);
 *   }
 */
public class InMemoryUserRepository {
    private static final List<User> users = new ArrayList<>();

    static {
        // Initialize with sample data
        users.add(new User(UUID.randomUUID().toString(), "John Doe", "john@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Jane Smith", "jane@example.com"));
    }

    public User save(User user) {
        users.add(user);
        return user;
    }

    public Optional<User> findById(String id) {
        return users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst();
    }

    public boolean existsByEmail(String email) {
        return users.stream()
            .anyMatch(u -> u.getEmail().equals(email));
    }

    public void delete(String id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public void deleteAll() {
        users.clear();
    }

    public long count() {
        return users.size();
    }
}
