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
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Order items are required")
    private List<OrderItemRequest> items;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private Long prescriptionId;
}
