/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.service;

import lombok.RequiredArgsConstructor;
import org.pharmacy.api.dto.ChatMessageRequest;
import org.pharmacy.api.dto.ChatMessageResponse;
import org.pharmacy.api.model.ChatMessage;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.ChatMessageRepository;
import org.pharmacy.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(request.getMessage());
        message.setIsRead(false);

        message = chatMessageRepository.save(message);

        // Send notification to receiver
        notificationService.createNotification(
                receiver,
                "New Message from " + sender.getFullName(),
                request.getMessage().length() > 50
                        ? request.getMessage().substring(0, 50) + "..."
                        : request.getMessage(),
                "CHAT_MESSAGE"
        );

        // If sender is customer, notify ALL other pharmacists and admins
        if (sender.getRole() == User.UserRole.CUSTOMER) {
            notifyAllStaff(sender, request.getMessage(), receiver.getId());
        }

        return mapToResponse(message);
    }

    /**
     * Notify all pharmacists and admins about customer message (except the one already notified)
     */
    private void notifyAllStaff(User customer, String message, Long excludeUserId) {
        List<User> staff = userRepository.findAll().stream()
                .filter(u -> (u.getRole() == User.UserRole.PHARMACIST || u.getRole() == User.UserRole.ADMIN)
                        && !u.getId().equals(excludeUserId))
                .collect(Collectors.toList());

        for (User staffMember : staff) {
            notificationService.createNotification(
                    staffMember,
                    "New Customer Message from " + customer.getFullName(),
                    message.length() > 50 ? message.substring(0, 50) + "..." : message,
                    "CHAT_MESSAGE"
            );
        }
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversation(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("🔍 Getting conversation:");
        System.out.println("   User 1: " + user1.getFullName() + " (" + user1.getRole() + ")");
        System.out.println("   User 2: " + user2.getFullName() + " (" + user2.getRole() + ")");

        List<ChatMessage> messages;

        // If staff (admin/pharmacist) viewing customer conversation
        if ((user1.getRole() == User.UserRole.PHARMACIST || user1.getRole() == User.UserRole.ADMIN)
                && user2.getRole() == User.UserRole.CUSTOMER) {

            System.out.println("📨 Staff viewing customer - showing ALL customer messages");
            // Show all messages where customer is sender or receiver
            messages = chatMessageRepository.findUserMessages(userId2);

        } else if (user1.getRole() == User.UserRole.CUSTOMER &&
                (user2.getRole() == User.UserRole.PHARMACIST || user2.getRole() == User.UserRole.ADMIN)) {

            System.out.println("📨 Customer viewing staff - showing ALL their messages");
            // Show all messages where customer is sender or receiver
            messages = chatMessageRepository.findUserMessages(userId1);

        } else {
            System.out.println("📨 Standard 1-to-1 conversation");
            // Normal conversation between two specific users
            messages = chatMessageRepository.findConversation(userId1, userId2);
        }

        System.out.println("   ✅ Returning " + messages.size() + " messages");

        return messages.stream()
                .map(this::mapToResponse)
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsRead(true);
        chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getUnreadMessages(Long userId) {
        List<ChatMessage> messages = chatMessageRepository.findUnreadMessages(userId);
        return messages.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<User> getUserConversations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("🔍 Getting conversations for: " + user.getFullName() + " (Role: " + user.getRole() + ")");

        // If user is pharmacist or admin, show ALL CUSTOMERS in the system
        if (user.getRole() == User.UserRole.PHARMACIST || user.getRole() == User.UserRole.ADMIN) {
            List<User> allCustomers = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == User.UserRole.CUSTOMER)
                    .collect(Collectors.toList());

            System.out.println("📋 Found " + allCustomers.size() + " total customers in system");
            for (User customer : allCustomers) {
                System.out.println("   - Customer: " + customer.getFullName());
            }
            return allCustomers;
        }

        // For customers, show staff they've messaged
        List<ChatMessage> messages = chatMessageRepository.findUserMessages(user.getId());
        System.out.println("📋 Found " + messages.size() + " messages for customer");

        Set<User> conversationUsers = new LinkedHashSet<>();
        for (ChatMessage message : messages) {
            if (!message.getSender().getId().equals(user.getId())) {
                conversationUsers.add(message.getSender());
            }
            if (!message.getReceiver().getId().equals(user.getId())) {
                conversationUsers.add(message.getReceiver());
            }
        }

        return new ArrayList<>(conversationUsers);
    }

    @Transactional(readOnly = true)
    public List<User> getAllCustomerConversations() {
        System.out.println("🔍 Getting all customer conversations...");

        // Get all chat messages
        List<ChatMessage> allMessages = chatMessageRepository.findAll();
        System.out.println("📨 Total messages in database: " + allMessages.size());

        if (allMessages.isEmpty()) {
            System.out.println("⚠️ No messages found in database");
            return new ArrayList<>();
        }

        Set<User> customers = new LinkedHashSet<>();
        for (ChatMessage message : allMessages) {
            System.out.println("   Message: " + message.getSender().getFullName() + " -> " + message.getReceiver().getFullName());

            // Add customer if they are sender
            if (message.getSender().getRole() == User.UserRole.CUSTOMER) {
                customers.add(message.getSender());
                System.out.println("   ✅ Added customer (sender): " + message.getSender().getFullName());
            }
            // Add customer if they are receiver
            if (message.getReceiver().getRole() == User.UserRole.CUSTOMER) {
                customers.add(message.getReceiver());
                System.out.println("   ✅ Added customer (receiver): " + message.getReceiver().getFullName());
            }
        }

        System.out.println("📋 Total unique customers found: " + customers.size());
        for (User customer : customers) {
            System.out.println("   - " + customer.getFullName() + " (" + customer.getEmail() + ")");
        }

        return new ArrayList<>(customers);
    }

    /**
     * Find pharmacist or admin for customer to chat with
     * Uses round-robin to distribute load evenly
     */
    @Transactional(readOnly = true)
    public User findPharmacistOrAdmin(Long customerId) {
        List<User> staff = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.UserRole.PHARMACIST || u.getRole() == User.UserRole.ADMIN)
                .collect(Collectors.toList());

        if (staff.isEmpty()) {
            throw new RuntimeException("No pharmacist or admin available");
        }

        // Simple round-robin: use customer ID to distribute load
        // This ensures the same customer always gets the same staff member initially
        int index = (int) (customerId % staff.size());
        return staff.get(index);
    }

    private ChatMessageResponse mapToResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSenderId(message.getSender().getId());
        response.setSenderName(message.getSender().getFullName());
        response.setReceiverId(message.getReceiver().getId());
        response.setReceiverName(message.getReceiver().getFullName());
        response.setMessage(message.getMessage());
        response.setIsRead(message.getIsRead());
        response.setCreatedAt(message.getCreatedAt());
        return response;
    }
}