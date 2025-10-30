package org.pharmacy.api.controller;

import lombok.RequiredArgsConstructor;
import org.pharmacy.api.dto.ApiResponse;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/{userId}/role")
    @Transactional
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String roleStr = request.get("role");
        user.setRole(User.UserRole.valueOf(roleStr));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", user));
    }
    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
