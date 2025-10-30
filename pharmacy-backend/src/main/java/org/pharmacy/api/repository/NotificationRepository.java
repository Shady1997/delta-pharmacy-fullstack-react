package org.pharmacy.api.repository;

import org.pharmacy.api.model.Notification;
import org.pharmacy.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndRead(User user, Boolean read);  // ← Changed from IsRead to Read
}