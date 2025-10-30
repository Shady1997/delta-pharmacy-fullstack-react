/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.service;

import org.pharmacy.api.dto.PaymentRequest;
import org.pharmacy.api.dto.PaymentVerificationRequest;
import org.pharmacy.api.model.Order;
import org.pharmacy.api.model.Payment;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.OrderRepository;
import org.pharmacy.api.repository.PaymentRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private static final String VALID_CARD_NUMBER = "4111111111111111";

    @Transactional
    public Payment initiatePayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate card number
        String cardNumber = request.getCardNumber().replaceAll("\\s+", "");
        if (!VALID_CARD_NUMBER.equals(cardNumber)) {
            throw new RuntimeException("Invalid card number. Only Mastercard 4111111111111111 is accepted");
        }

        // Validate CVV (must be 3 digits)
        if (request.getCvv() == null || !request.getCvv().matches("\\d{3}")) {
            throw new RuntimeException("Invalid CVV. Must be 3 digits");
        }

        // Validate expiry date (must be in future)
        if (!isExpiryDateValid(request.getExpiryMonth(), request.getExpiryYear())) {
            throw new RuntimeException("Card has expired");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod("MASTERCARD");
        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        payment.setCardLastFourDigits("1111");
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        payment = paymentRepository.save(payment);

        notificationService.createNotification(
                user,
                "Payment Processing",
                "Your payment of $" + String.format("%.2f", payment.getAmount()) + " is being processed.",
                "PAYMENT_UPDATE"
        );

        return payment;
    }

    @Transactional
    public Payment verifyPayment(PaymentVerificationRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.PROCESSING) {
            throw new RuntimeException("Payment is not in processing state");
        }

        // Simulate payment verification (always succeeds for valid card)
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        // Update order status
        Order order = payment.getOrder();
        order.setStatus(Order.OrderStatus.PROCESSING);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        notificationService.createNotification(
                payment.getUser(),
                "Payment Successful",
                "Your payment of $" + String.format("%.2f", payment.getAmount()) +
                        " has been processed successfully. Transaction ID: " + payment.getTransactionId(),
                "PAYMENT_UPDATE"
        );

        return payment;
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paymentRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    private boolean isExpiryDateValid(String month, String year) {
        try {
            int expMonth = Integer.parseInt(month);
            int expYear = Integer.parseInt(year);

            // Adjust year if only 2 digits provided
            if (expYear < 100) {
                expYear += 2000;
            }

            LocalDateTime now = LocalDateTime.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();

            if (expYear < currentYear) {
                return false;
            }
            if (expYear == currentYear && expMonth < currentMonth) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}