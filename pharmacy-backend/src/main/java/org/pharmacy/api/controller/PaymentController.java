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
import org.pharmacy.api.dto.PaymentRequest;
import org.pharmacy.api.dto.PaymentVerificationRequest;
import org.pharmacy.api.model.Payment;
import org.pharmacy.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "5. Payments", description = "Payment processing and verification")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate payment", description = "Start payment process for an order")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Payment>> initiatePayment(@RequestBody PaymentRequest request) {
        Payment payment = paymentService.initiatePayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment initiated", payment));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify payment", description = "Verify and complete payment transaction")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Payment>> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        Payment payment = paymentService.verifyPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment verified", payment));
    }

    @GetMapping("/history")
    @Operation(summary = "Get payment history", description = "Retrieve payment history for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentHistory(@RequestParam Long userId) {
        List<Payment> payments = paymentService.getPaymentHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }
}
