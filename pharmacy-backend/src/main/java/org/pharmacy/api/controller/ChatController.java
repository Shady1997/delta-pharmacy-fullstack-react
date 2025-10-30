/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pharmacy.api.dto.ApiResponse;
import org.pharmacy.api.dto.ChatMessageRequest;
import org.pharmacy.api.dto.ChatMessageResponse;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.UserRepository;
import org.pharmacy.api.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "8. Chat", description = "Real-time chat between users")
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    @PostMapping("/send")
    @Operation(summary = "Send chat message", description = "Send a message to another user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @RequestBody ChatMessageRequest request,
            Authentication authentication) {
        ChatMessageResponse response = chatService.sendMessage(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Message sent", response));
    }

    @GetMapping("/conversation/{otherUserId}")
    @Operation(summary = "Get conversation", description = "Get chat history with a specific user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getConversation(
            @PathVariable Long otherUserId,
            Authentication authentication) {

        // Get current user from email
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatMessageResponse> messages = chatService.getConversation(currentUser.getId(), otherUserId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @GetMapping("/pharmacist")
    @Operation(summary = "Get pharmacist for chat", description = "Get available pharmacist/admin for chat")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<User>> getPharmacist(Authentication authentication) {
        // Get current user from email
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User pharmacist = chatService.findPharmacistOrAdmin(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(pharmacist));
    }

    @GetMapping("/conversations")
    @Operation(summary = "Get user conversations", description = "Get list of users with chat history")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<User>>> getConversations(Authentication authentication) {
        List<User> users = chatService.getUserConversations(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/read/{messageId}")
    @Operation(summary = "Mark message as read", description = "Mark a chat message as read")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long messageId) {
        chatService.markAsRead(messageId);
        return ResponseEntity.ok(ApiResponse.success("Message marked as read"));
    }
}