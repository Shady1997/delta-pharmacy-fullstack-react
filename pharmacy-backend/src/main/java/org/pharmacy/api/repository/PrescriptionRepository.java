package org.pharmacy.api.repository;

import org.pharmacy.api.model.Prescription;
import org.pharmacy.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByUser(User user);
    List<Prescription> findByUserId(Long userId);
    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);

    long countByStatus(Prescription.PrescriptionStatus status);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, Prescription.PrescriptionStatus status);
}