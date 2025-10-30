package org.pharmacy.api.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private Long userId;
    private String cardNumber;
    private String cardHolderName;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
}