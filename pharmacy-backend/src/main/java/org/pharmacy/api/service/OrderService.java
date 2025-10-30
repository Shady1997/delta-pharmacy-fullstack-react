/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 */
package org.pharmacy.api.service;

import org.pharmacy.api.dto.OrderRequest;
import org.pharmacy.api.dto.OrderItemRequest;
import org.pharmacy.api.model.*;
import org.pharmacy.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final NotificationService notificationService;

    @Transactional
    public Order createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(Order.OrderStatus.PENDING);

        if (request.getPrescriptionId() != null) {
            Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                    .orElseThrow(() -> new RuntimeException("Prescription not found"));

            if (prescription.getStatus() != Prescription.PrescriptionStatus.APPROVED) {
                throw new RuntimeException("Prescription must be approved before ordering");
            }
            order.setPrescription(prescription);
        }

        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getPrescriptionRequired() && request.getPrescriptionId() == null) {
                throw new RuntimeException("Product requires prescription: " + product.getName());
            }

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice() * itemRequest.getQuantity());

            order.getItems().add(orderItem);
            totalAmount += orderItem.getSubtotal();

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        notificationService.createNotification(
                user,
                "Order Created",
                "Your order #" + order.getId() + " has been placed successfully.",
                "ORDER_UPDATE"
        );

        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        Order.OrderStatus oldStatus = order.getStatus();

        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());

            // If changing FROM delivered TO another status, return stock
            if (oldStatus == Order.OrderStatus.DELIVERED && newStatus != Order.OrderStatus.DELIVERED) {
                returnStockToInventory(order);
            }

            // If changing TO cancelled, return stock
            if (newStatus == Order.OrderStatus.CANCELLED && oldStatus != Order.OrderStatus.CANCELLED) {
                returnStockToInventory(order);
            }

            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());

            order = orderRepository.save(order);

            notificationService.createNotification(
                    order.getUser(),
                    "Order Status Updated",
                    "Your order #" + order.getId() + " status is now: " + status,
                    "ORDER_UPDATE"
            );

            return order;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel delivered order");
        }

        // Return stock to inventory
        returnStockToInventory(order);

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        notificationService.createNotification(
                order.getUser(),
                "Order Cancelled",
                "Your order #" + order.getId() + " has been cancelled.",
                "ORDER_UPDATE"
        );
    }

    /**
     * Return stock to inventory when order is cancelled or status changed from delivered
     */
    private void returnStockToInventory(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }
}