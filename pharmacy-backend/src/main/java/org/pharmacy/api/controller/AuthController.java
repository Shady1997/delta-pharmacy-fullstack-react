/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pharmacy.api.dto.*;
import org.pharmacy.api.model.User;
import org.pharmacy.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "User registration, login, and profile management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate and receive JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user (client-side token removal)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<String>> logout() {
        // In stateless JWT, logout is handled client-side by removing token
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieve authenticated user's profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<User>> getProfile(Authentication authentication) {
        User user = authService.getProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/update")
    @Operation(summary = "Update profile", description = "Update user profile and/or password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<User>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        User user = authService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", user));
    }
}
