/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.model.Notification;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.NotificationRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Notification createNotification(User user, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.valueOf(type));
        notification.setRead(false);  // ← Changed from setIsRead to setRead

        notification = notificationRepository.save(notification);

        // Mock sending notification (in real app, would send push/email/SMS)
        mockSendNotification(notification);

        return notification;
    }

    private void mockSendNotification(Notification notification) {
        // Mock implementation - would integrate with FCM, SendGrid, Twilio, etc.
        System.out.println("📧 Sending notification to " + notification.getUser().getEmail());
        System.out.println("   Title: " + notification.getTitle());
        System.out.println("   Message: " + notification.getMessage());
    }

    public List<Notification> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUser(user);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserAndRead(user, false);  // ← Changed from IsRead to Read
    }

    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);  // ← Changed from setIsRead to setRead
        return notificationRepository.save(notification);
    }
}