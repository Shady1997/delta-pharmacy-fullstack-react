package org.pharmacy.api.repository;

import org.pharmacy.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByPrescriptionRequired(boolean prescriptionRequired);
    List<Product> findByNameContainingIgnoreCase(String name);

    long countByStockQuantityLessThan(int quantity);
    long countByStockQuantity(int quantity);

    @Query("SELECT COALESCE(SUM(p.price * p.stockQuantity), 0) FROM Product p")
    Double sumInventoryValue();
}