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
import org.pharmacy.api.dto.ReviewRequest;
import org.pharmacy.api.model.Review;
import org.pharmacy.api.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "7. Reviews", description = "Product reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Submit review", description = "Create a review and rating for a product")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Review>> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        Review review = reviewService.createReview(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Review submitted successfully", review));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product reviews", description = "Retrieve all reviews for a specific product with average rating")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getProductReviews(productId);
        Double averageRating = reviewService.getAverageRating(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews);
        response.put("averageRating", averageRating);
        response.put("totalReviews", reviews.size());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
