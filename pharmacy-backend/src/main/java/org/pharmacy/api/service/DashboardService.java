/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import lombok.RequiredArgsConstructor;
import org.pharmacy.api.model.Order;
import org.pharmacy.api.model.Prescription;
import org.pharmacy.api.model.Product;
import org.pharmacy.api.model.User;
import org.pharmacy.api.model.SupportTicket;
import org.pharmacy.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final SupportTicketRepository supportTicketRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> stats = new HashMap<>();

        if ("ADMIN".equals(user.getRole().name())) {
            // Admin sees everything
            stats.put("totalProducts", productRepository.count());
            stats.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
            stats.put("pendingPrescriptions", prescriptionRepository.countByStatus(Prescription.PrescriptionStatus.PENDING));
            stats.put("supportTickets", supportTicketRepository.countByStatus(SupportTicket.TicketStatus.OPEN));  // ← CHANGED
            stats.put("totalUsers", userRepository.count());
            stats.put("lowStock", productRepository.countByStockQuantityLessThan(10));
            stats.put("totalOrders", orderRepository.count());
            stats.put("totalRevenue", orderRepository.sumTotalAmount());

        } else if ("PHARMACIST".equals(user.getRole().name())) {
            // Pharmacist sees operational data
            stats.put("pendingPrescriptions", prescriptionRepository.countByStatus(Prescription.PrescriptionStatus.PENDING));
            stats.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
            stats.put("supportTickets", supportTicketRepository.countByStatus(SupportTicket.TicketStatus.OPEN));  // ← CHANGED
            stats.put("lowStock", productRepository.countByStockQuantityLessThan(10));
            stats.put("totalProducts", productRepository.count());
            stats.put("processingOrders", orderRepository.countByStatus(Order.OrderStatus.PROCESSING));

        } else {
            // Customer sees their own data
            stats.put("myOrders", orderRepository.countByUserId(user.getId()));
            stats.put("pendingOrders", orderRepository.countByUserIdAndStatus(user.getId(), Order.OrderStatus.PENDING));
            stats.put("myPrescriptions", prescriptionRepository.countByUserId(user.getId()));
            stats.put("pendingPrescriptions", prescriptionRepository.countByUserIdAndStatus(user.getId(), Prescription.PrescriptionStatus.PENDING));
            stats.put("totalProducts", productRepository.count());
            stats.put("myTickets", supportTicketRepository.countByUserId(user.getId()));
        }

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        // Sales Analytics
        Map<String, Object> salesData = new HashMap<>();
        salesData.put("totalRevenue", orderRepository.sumTotalAmount());
        salesData.put("totalOrders", orderRepository.count());
        salesData.put("averageOrderValue", orderRepository.averageOrderAmount());
        salesData.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        salesData.put("completedOrders", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        analytics.put("sales", salesData);

        // Inventory Analytics
        Map<String, Object> inventoryData = new HashMap<>();
        inventoryData.put("totalProducts", productRepository.count());
        inventoryData.put("lowStockProducts", productRepository.countByStockQuantityLessThan(10));
        inventoryData.put("outOfStock", productRepository.countByStockQuantity(0));
        inventoryData.put("totalValue", productRepository.sumInventoryValue());
        analytics.put("inventory", inventoryData);

        // User Analytics
        Map<String, Object> userData = new HashMap<>();
        userData.put("totalUsers", userRepository.count());
        userData.put("admins", userRepository.countByRole(User.UserRole.ADMIN));
        userData.put("pharmacists", userRepository.countByRole(User.UserRole.PHARMACIST));
        userData.put("customers", userRepository.countByRole(User.UserRole.CUSTOMER));
        analytics.put("users", userData);

        // Prescription Analytics
        Map<String, Object> prescriptionData = new HashMap<>();
        prescriptionData.put("totalPrescriptions", prescriptionRepository.count());
        prescriptionData.put("pending", prescriptionRepository.countByStatus(Prescription.PrescriptionStatus.PENDING));
        prescriptionData.put("approved", prescriptionRepository.countByStatus(Prescription.PrescriptionStatus.APPROVED));
        prescriptionData.put("rejected", prescriptionRepository.countByStatus(Prescription.PrescriptionStatus.REJECTED));
        analytics.put("prescriptions", prescriptionData);

        // Support Analytics
        Map<String, Object> supportData = new HashMap<>();
        supportData.put("totalTickets", supportTicketRepository.count());
        supportData.put("openTickets", supportTicketRepository.countByStatus(SupportTicket.TicketStatus.OPEN));  // ← CHANGED
        supportData.put("resolvedTickets", supportTicketRepository.countByStatus(SupportTicket.TicketStatus.RESOLVED));  // ← CHANGED
        analytics.put("support", supportData);

        return analytics;
    }
}