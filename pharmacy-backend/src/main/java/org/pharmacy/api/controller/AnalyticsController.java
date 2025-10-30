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
import org.pharmacy.api.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "9. Analytics", description = "Reports and analytics (Admin/Pharmacist only)")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales")
    @Operation(summary = "Get sales report", description = "Retrieve sales analytics and revenue data")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesReport() {
        Map<String, Object> report = analyticsService.getSalesReport();
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/inventory")
    @Operation(summary = "Get inventory report", description = "Retrieve inventory statistics and stock analysis")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryReport() {
        Map<String, Object> report = analyticsService.getInventoryReport();
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/users")
    @Operation(summary = "Get users report", description = "Retrieve user statistics and registration data")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUsersReport() {
        Map<String, Object> report = analyticsService.getUsersReport();
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
