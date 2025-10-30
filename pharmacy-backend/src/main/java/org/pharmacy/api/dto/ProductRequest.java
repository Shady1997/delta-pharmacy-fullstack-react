/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String category;
    private String brand;
    private String imageUrl;
    private Boolean prescriptionRequired;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private Integer reorderLevel;
}
