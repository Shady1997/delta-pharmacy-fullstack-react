/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        ORDER_UPDATE,
        PAYMENT_UPDATE,
        PRESCRIPTION_UPDATE,
        SYSTEM,
        CHAT_MESSAGE  // ← Add this line
    }
}