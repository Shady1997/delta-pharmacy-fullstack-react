package org.pharmacy.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageResponse {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}