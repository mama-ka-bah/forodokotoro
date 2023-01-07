package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
}
