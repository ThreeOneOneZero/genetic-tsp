package com.yourproject.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Exemplo de DTO para requisição
public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    String email
) {}
