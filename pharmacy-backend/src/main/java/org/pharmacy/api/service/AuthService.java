/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.model.User.UserRole;
import org.pharmacy.api.dto.*;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.UserRepository;
import org.pharmacy.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        // Set role from request, default to CUSTOMER if not provided
        String roleStr = request.getRole() != null ? request.getRole() : "CUSTOMER";
        user.setRole(UserRole.valueOf(roleStr));  // ← Changed from Role to UserRole

        userRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse(token, "Bearer", user.getId(), user.getEmail(),
                user.getFullName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()  // ← This is correct now
        );
    }

    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateProfile(String email, UpdateProfileRequest request) {
        User user = getProfile(email);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.save(user);
    }
}
