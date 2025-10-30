/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.dto.ProductRequest;
import org.pharmacy.api.dto.StockUpdateRequest;
import org.pharmacy.api.model.Product;
import org.pharmacy.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setPrescriptionRequired(request.getPrescriptionRequired() != null ?
                request.getPrescriptionRequired() : false);
        product.setStockQuantity(request.getStockQuantity() != null ?
                request.getStockQuantity() : 0);
        product.setReorderLevel(request.getReorderLevel() != null ?
                request.getReorderLevel() : 10);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setPrescriptionRequired(request.getPrescriptionRequired());
        product.setStockQuantity(request.getStockQuantity());
        product.setReorderLevel(request.getReorderLevel());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByPrescriptionRequired(Boolean required) {
        return productRepository.findByPrescriptionRequired(required);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() <= p.getReorderLevel())
                .toList();
    }

    @Transactional
    public Product updateStock(StockUpdateRequest request) {
        Product product = getProductById(request.getProductId());

        if ("ADD".equalsIgnoreCase(request.getOperation())) {
            product.setStockQuantity(product.getStockQuantity() + request.getQuantity());
        } else if ("SUBTRACT".equalsIgnoreCase(request.getOperation())) {
            int newQuantity = product.getStockQuantity() - request.getQuantity();
            if (newQuantity < 0) {
                throw new RuntimeException("Insufficient stock");
            }
            product.setStockQuantity(newQuantity);
        } else {
            throw new RuntimeException("Invalid operation. Use ADD or SUBTRACT");
        }

        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
}

