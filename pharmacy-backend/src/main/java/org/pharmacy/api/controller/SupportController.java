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
import org.pharmacy.api.dto.SupportTicketRequest;
import org.pharmacy.api.model.SupportTicket;
import org.pharmacy.api.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@Tag(name = "8. Support", description = "Customer support ticket management")
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/ticket")
    @Operation(summary = "Create support ticket", description = "Create a new support ticket")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<SupportTicket>> createTicket(
            @Valid @RequestBody SupportTicketRequest request,
            Authentication authentication) {
        SupportTicket ticket = supportService.createTicket(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Ticket created successfully", ticket));
    }

    @GetMapping("/ticket/{id}")
    @Operation(summary = "Get ticket details", description = "Retrieve details of a specific support ticket")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<SupportTicket>> getTicket(@PathVariable Long id) {
        SupportTicket ticket = supportService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }

    @GetMapping("/tickets")
    @Operation(summary = "Get user tickets", description = "Retrieve all tickets for the authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<SupportTicket>>> getUserTickets(Authentication authentication) {
        List<SupportTicket> tickets = supportService.getUserTickets(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    @GetMapping("/tickets/all")
    @Operation(summary = "Get all tickets", description = "Retrieve all support tickets (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<SupportTicket>>> getAllTickets() {
        List<SupportTicket> tickets = supportService.getAllTickets();
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    @PutMapping("/ticket/{id}/status")
    @Operation(summary = "Update ticket status", description = "Change ticket status (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<SupportTicket>> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        SupportTicket ticket = supportService.updateTicketStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Ticket status updated", ticket));
    }

    @PostMapping("/ticket/{id}/response")
    @Operation(summary = "Add ticket response", description = "Add response to a ticket (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<SupportTicket>> addResponse(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String response = body.get("response");
        SupportTicket ticket = supportService.addResponse(id, response);
        return ResponseEntity.ok(ApiResponse.success("Response added", ticket));
    }
}
