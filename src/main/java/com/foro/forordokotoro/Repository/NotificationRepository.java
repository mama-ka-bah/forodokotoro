package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByLuAndUseridOrderByDatenotificationDesc(Boolean lu, Utilisateurs utilisateurs);
    List<Notifications> findByUseridOrderByDatenotificationDesc(Utilisateurs utilisateurs);
    List<Notifications> findAllByUseridOrderByDatenotificationDesc(Utilisateurs utilisateurs);

    List<Notifications> findByUseridAndLu(Utilisateurs utilisateurs, Boolean lu);

    @Query(value = "SELECT * FROM notifications ORDER BY datenotification Desc", nativeQuery = true)
    List<Notifications> FINDALLNOTIFICATIONS();

    @Query(value = "SELECT * FROM notifications ORDER BY datenotification Desc LIMIT 4", nativeQuery = true)
    List<Notifications> FINDQUATRERECENTESNOTIFICATIONS();
}
