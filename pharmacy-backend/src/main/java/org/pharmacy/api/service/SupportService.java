/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.service;

import org.pharmacy.api.dto.SupportTicketRequest;
import org.pharmacy.api.model.SupportTicket;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.SupportTicketRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    @Transactional
    public SupportTicket createTicket(SupportTicketRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(SupportTicket.TicketStatus.OPEN);

        if (request.getPriority() != null) {
            try {
                ticket.setPriority(SupportTicket.TicketPriority.valueOf(request.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                ticket.setPriority(SupportTicket.TicketPriority.MEDIUM);
            }
        }

        return supportTicketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public SupportTicket getTicketById(Long id) {
        return supportTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Transactional(readOnly = true)
    public List<SupportTicket> getUserTickets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return supportTicketRepository.findByUserId(user.getId());  // ← CHANGED from findByUser(user)
    }

    @Transactional(readOnly = true)
    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }

    @Transactional
    public SupportTicket updateTicketStatus(Long id, String status) {
        SupportTicket ticket = getTicketById(id);

        try {
            ticket.setStatus(SupportTicket.TicketStatus.valueOf(status.toUpperCase()));
            ticket.setUpdatedAt(LocalDateTime.now());
            return supportTicketRepository.save(ticket);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }

    @Transactional
    public SupportTicket addResponse(Long id, String response) {
        SupportTicket ticket = getTicketById(id);
        ticket.setResponse(response);
        ticket.setStatus(SupportTicket.TicketStatus.RESOLVED);
        ticket.setUpdatedAt(LocalDateTime.now());
        return supportTicketRepository.save(ticket);
    }

    public String mockChatWithPharmacist(String message) {
        // Mock chatbot response
        return "Thank you for your message: '" + message + "'. A pharmacist will respond shortly. For urgent matters, please call our helpline.";
    }
}