package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByLuAndUseridOrderByDatenotificationDesc(Boolean lu, Utilisateurs utilisateurs);
    List<Notifications> findByUseridOrderByDatenotificationDesc(Utilisateurs utilisateurs);
    List<Notifications> findAllByUseridOrderByDatenotificationDesc(Utilisateurs utilisateurs);

    List<Notifications> findByUseridAndLu(Utilisateurs utilisateurs, Boolean lu);
}
