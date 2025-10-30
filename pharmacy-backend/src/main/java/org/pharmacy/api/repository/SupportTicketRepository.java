package org.pharmacy.api.repository;

import org.pharmacy.api.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserId(Long userId);
    List<SupportTicket> findByStatus(SupportTicket.TicketStatus status);  // ← CHANGED

    long countByStatus(SupportTicket.TicketStatus status);  // ← CHANGED
    long countByUserId(Long userId);
}