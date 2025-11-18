package com.yourproject.controllers;

import com.yourproject.models.ApiResponse;
import com.yourproject.models.CreateUserRequest;
import com.yourproject.models.User;
import com.yourproject.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * User API Endpoints
 * 
 * Base URL: /api/users
 * 
 * Endpoints:
 *   GET    /api/users           - List all users
 *   GET    /api/users/{id}      - Get user by ID
 *   POST   /api/users           - Create new user
 *   PUT    /api/users/{id}      - Update user
 *   DELETE /api/users/{id}      - Delete user
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * List all users
     * 
     * @return List of users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> listUsers() {
        return ok(userService.listUsers());
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User with specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String id) {
        return ok(userService.getUser(id));
    }

    /**
     * Create new user
     * 
     * @param request CreateUserRequest with name and email
     * @return Created user
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(
        @Valid @RequestBody CreateUserRequest request
    ) {
        return created(userService.createUser(request));
    }

    /**
     * Update user
     * 
     * @param id User ID
     * @param request Updated user data
     * @return Updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
        @PathVariable String id,
        @Valid @RequestBody CreateUserRequest request
    ) {
        return ok(userService.updateUser(id, request));
    }

    /**
     * Delete user
     * 
     * @param id User ID
     * @return Empty response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return okEmpty();
    }
}
