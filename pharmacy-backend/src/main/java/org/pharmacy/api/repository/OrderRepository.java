package org.pharmacy.api.repository;

import org.pharmacy.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(Order.OrderStatus status);

    long countByStatus(Order.OrderStatus status);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, Order.OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    Double sumTotalAmount();

    @Query("SELECT COALESCE(AVG(o.totalAmount), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    Double averageOrderAmount();
}