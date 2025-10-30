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
import org.pharmacy.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics and analytics")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Get role-based dashboard statistics")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats(Authentication authentication) {
        Map<String, Object> stats = dashboardService.getDashboardStats(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get analytics data", description = "Get detailed analytics (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics() {
        Map<String, Object> analytics = dashboardService.getAnalytics();
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }
}