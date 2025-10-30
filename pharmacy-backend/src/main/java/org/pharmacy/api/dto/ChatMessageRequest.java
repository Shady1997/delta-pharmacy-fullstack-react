package org.pharmacy.api.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long receiverId;
    private String message;
}