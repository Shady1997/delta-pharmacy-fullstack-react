/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.repository;

import org.pharmacy.api.model.ChatMessage;
import org.pharmacy.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findConversation(Long userId1, Long userId2);

    @Query("SELECT m FROM ChatMessage m WHERE m.receiver.id = :userId AND m.isRead = false")
    List<ChatMessage> findUnreadMessages(Long userId);

    // Get all messages where user is sender or receiver
    @Query("SELECT m FROM ChatMessage m WHERE m.sender.id = :userId OR m.receiver.id = :userId ORDER BY m.createdAt DESC")
    List<ChatMessage> findUserMessages(Long userId);
}