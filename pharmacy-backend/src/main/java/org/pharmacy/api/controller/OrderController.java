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
import org.pharmacy.api.dto.OrderRequest;
import org.pharmacy.api.dto.OrderStatusRequest;
import org.pharmacy.api.model.Order;
import org.pharmacy.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "4. Orders", description = "Order creation and management")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Place a new order with selected products")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success("Order created successfully", order));
    }

    @GetMapping("/{userId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get user orders", description = "Retrieve order history for a specific user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get all orders", description = "Retrieve all orders (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Change order status (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        Order order = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", null));
    }
}

