package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationControlleur {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/recupererLesNotificationLuDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsLuDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){

        }
        return notificationRepository.findByLuAndUseridOrderByDatenotification(true, utilisateurs);
    }


    @GetMapping("/recupererLesNotificationNonLuDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsNonLuDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        return notificationRepository.findByLuAndUseridOrderByDatenotification(false, utilisateurs);
    }


    @GetMapping("/recupererLesNotificationDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        return notificationRepository.findByUseridOrderByDatenotification(utilisateurs);
    }

    @PatchMapping("/modifier/{idnotif}")
    public Object modifier(@PathVariable Long idnotif, @RequestBody Notifications notifications){
        return notificationService.mettrejourNotification(idnotif, notifications);
    }
}
