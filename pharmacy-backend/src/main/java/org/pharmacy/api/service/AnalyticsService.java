/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.model.Order;
import org.pharmacy.api.model.Product;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.OrderRepository;
import org.pharmacy.api.repository.ProductRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getSalesReport() {
        Map<String, Object> report = new HashMap<>();
        List<Order> allOrders = orderRepository.findAll();

        double totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long totalOrders = allOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .count();

        long pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PENDING ||
                        o.getStatus() == Order.OrderStatus.CONFIRMED)
                .count();

        report.put("totalRevenue", totalRevenue);
        report.put("totalOrders", totalOrders);
        report.put("pendingOrders", pendingOrders);
        report.put("averageOrderValue", totalOrders > 0 ? totalRevenue / totalOrders : 0);

        return report;
    }

    public Map<String, Object> getInventoryReport() {
        Map<String, Object> report = new HashMap<>();
        List<Product> allProducts = productRepository.findAll();

        long totalProducts = allProducts.size();
        long outOfStock = allProducts.stream()
                .filter(p -> p.getStockQuantity() == 0)
                .count();

        long lowStock = allProducts.stream()
                .filter(p -> p.getStockQuantity() > 0 && p.getStockQuantity() <= p.getReorderLevel())
                .count();

        double totalInventoryValue = allProducts.stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();

        report.put("totalProducts", totalProducts);
        report.put("outOfStock", outOfStock);
        report.put("lowStock", lowStock);
        report.put("totalInventoryValue", totalInventoryValue);

        return report;
    }

    public Map<String, Object> getUsersReport() {
        Map<String, Object> report = new HashMap<>();
        List<User> allUsers = userRepository.findAll();

        long totalUsers = allUsers.size();
        long customers = allUsers.stream()
                .filter(u -> u.getRole() == User.UserRole.CUSTOMER)
                .count();

        long pharmacists = allUsers.stream()
                .filter(u -> u.getRole() == User.UserRole.PHARMACIST)
                .count();

        long admins = allUsers.stream()
                .filter(u -> u.getRole() == User.UserRole.ADMIN)
                .count();

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long newUsers = allUsers.stream()
                .filter(u -> u.getCreatedAt().isAfter(thirtyDaysAgo))
                .count();

        report.put("totalUsers", totalUsers);
        report.put("customers", customers);
        report.put("pharmacists", pharmacists);
        report.put("admins", admins);
        report.put("newUsersLast30Days", newUsers);

        return report;
    }
}