package org.pharmacy.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pharmacy.api.dto.ApiResponse;
import org.pharmacy.api.model.Prescription;
import org.pharmacy.api.repository.PrescriptionRepository;  // ← ADD THIS IMPORT
import org.pharmacy.api.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "3. Prescriptions", description = "Prescription upload and approval management")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;  // ← ADD THIS

    @PostMapping("/upload")
    @Operation(summary = "Upload prescription", description = "Upload prescription document for review")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Prescription>> uploadPrescription(
            @RequestParam Long userId,
            @RequestParam String fileName,
            @RequestParam String fileType,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) String notes) {

        Prescription prescription = prescriptionService.uploadPrescription(
                userId, fileName, fileType, doctorName, notes);
        return ResponseEntity.ok(ApiResponse.success("Prescription uploaded successfully", prescription));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user prescriptions", description = "Retrieve all prescriptions for a specific user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Prescription>>> getUserPrescriptionsByPathVariable(@PathVariable Long userId) {
        List<Prescription> prescriptions = prescriptionService.getUserPrescriptions(userId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve prescription", description = "Approve a prescription (Pharmacist/Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Prescription>> approvePrescription(
            @PathVariable Long id,
            Authentication authentication) {
        Prescription prescription = prescriptionService.approvePrescription(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Prescription approved", prescription));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject prescription", description = "Reject a prescription with reason (Pharmacist/Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Prescription>> rejectPrescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        String reason = body.getOrDefault("reason", "No reason provided");
        Prescription prescription = prescriptionService.rejectPrescription(id, authentication.getName(), reason);
        return ResponseEntity.ok(ApiResponse.success("Prescription rejected", prescription));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending prescriptions", description = "Retrieve all pending prescriptions (Pharmacist/Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Prescription>>> getPendingPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getPendingPrescriptions();
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get all prescriptions", description = "Retrieve all prescriptions (Admin/Pharmacist only)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Prescription>>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }

    @GetMapping("/user/{userId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get user prescriptions", description = "Retrieve prescriptions for a specific user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<List<Prescription>>> getUserPrescriptionsByUser(@PathVariable Long userId) {
        List<Prescription> prescriptions = prescriptionRepository.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions));
    }
}