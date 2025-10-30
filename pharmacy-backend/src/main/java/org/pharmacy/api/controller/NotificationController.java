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
import org.pharmacy.api.dto.ApiResponse;
import org.pharmacy.api.model.Notification;
import org.pharmacy.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "6. Notifications", description = "User notification management")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user notifications", description = "Retrieve all notifications for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @GetMapping("/{userId}/unread")
    @Operation(summary = "Get unread notifications", description = "Retrieve unread notifications for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notification));
    }
}

