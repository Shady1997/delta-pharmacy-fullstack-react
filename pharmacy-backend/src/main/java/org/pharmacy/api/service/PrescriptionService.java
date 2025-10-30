/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.model.Prescription;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.PrescriptionRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public Prescription uploadPrescription(Long userId, String fileName, String fileType, String doctorName, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Prescription prescription = new Prescription();
        prescription.setUser(user);
        prescription.setFileName(fileName);
        prescription.setFileType(fileType);
        prescription.setFilePath("/mock/prescriptions/" + UUID.randomUUID().toString());
        prescription.setDoctorName(doctorName);
        prescription.setNotes(notes);
        prescription.setStatus(Prescription.PrescriptionStatus.PENDING);

        prescription = prescriptionRepository.save(prescription);

        notificationService.createNotification(
                user,
                "Prescription Uploaded",
                "Your prescription has been uploaded successfully and is pending review.",
                "PRESCRIPTION_UPDATE"
        );

        return prescription;
    }

    public List<Prescription> getUserPrescriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return prescriptionRepository.findByUser(user);
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }

    @Transactional
    public Prescription approvePrescription(Long id, String pharmacistEmail) {
        Prescription prescription = getPrescriptionById(id);
        User pharmacist = userRepository.findByEmail(pharmacistEmail)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        prescription.setStatus(Prescription.PrescriptionStatus.APPROVED);
        prescription.setReviewedBy(pharmacist);
        prescription.setReviewedAt(LocalDateTime.now());

        prescription = prescriptionRepository.save(prescription);

        notificationService.createNotification(
                prescription.getUser(),
                "Prescription Approved",
                "Your prescription has been approved by our pharmacist.",
                "PRESCRIPTION_UPDATE"
        );

        return prescription;
    }

    @Transactional
    public Prescription rejectPrescription(Long id, String pharmacistEmail, String reason) {
        Prescription prescription = getPrescriptionById(id);
        User pharmacist = userRepository.findByEmail(pharmacistEmail)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        prescription.setStatus(Prescription.PrescriptionStatus.REJECTED);
        prescription.setReviewedBy(pharmacist);
        prescription.setReviewedAt(LocalDateTime.now());
        prescription.setRejectionReason(reason);

        prescription = prescriptionRepository.save(prescription);

        notificationService.createNotification(
                prescription.getUser(),
                "Prescription Rejected",
                "Your prescription has been rejected. Reason: " + reason,
                "PRESCRIPTION_UPDATE"
        );

        return prescription;
    }

    public List<Prescription> getPendingPrescriptions() {
        return prescriptionRepository.findByStatus(Prescription.PrescriptionStatus.PENDING);
    }
}
