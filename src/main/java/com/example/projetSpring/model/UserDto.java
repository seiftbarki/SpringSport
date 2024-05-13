package com.example.projetSpring.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {

    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 0, message = "Age cannot be negative")
    private int age;

    @Digits(integer = 8, fraction = 0, message = "Phone number must be 8 digits")
    private int phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Getters and setters omitted for brevity
}
