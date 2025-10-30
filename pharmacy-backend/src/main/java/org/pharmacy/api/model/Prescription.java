/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fileName;
    private String fileType;
    private String filePath; // Mock path

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.PENDING;

    private String doctorName;
    private String notes;
    private String rejectionReason;

    private LocalDateTime uploadedAt = LocalDateTime.now();
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    public enum PrescriptionStatus {
        PENDING, APPROVED, REJECTED
    }
}