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
import org.pharmacy.api.dto.ProductRequest;
import org.pharmacy.api.dto.StockUpdateRequest;
import org.pharmacy.api.model.Product;
import org.pharmacy.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "2. Products", description = "Product and inventory management")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve list of all medicines/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve detailed information about a specific product")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @PostMapping("/products")
    @Operation(summary = "Add new product", description = "Create a new product (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", product));
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "Update product", description = "Update existing product information (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Product product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }

    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete product", description = "Remove product from system (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @GetMapping("/inventory/stock-levels")
    @Operation(summary = "Get stock levels", description = "View low stock products")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Product>>> getStockLevels() {
        List<Product> lowStockProducts = productService.getLowStockProducts();
        return ResponseEntity.ok(ApiResponse.success(lowStockProducts));
    }

    @PostMapping("/inventory/update-stock")
    @Operation(summary = "Update stock", description = "Add or subtract product stock quantity (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Product>> updateStock(@Valid @RequestBody StockUpdateRequest request) {
        Product product = productService.updateStock(request);
        return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", product));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search and filter products by name, category, or prescription requirement")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String filter) {

        if (query != null && !query.isEmpty()) {
            List<Product> products = productService.searchProducts(query);
            return ResponseEntity.ok(ApiResponse.success(products));
        }

        if ("prescription_required".equals(filter)) {
            List<Product> products = productService.getProductsByPrescriptionRequired(true);
            return ResponseEntity.ok(ApiResponse.success(products));
        }

        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}
