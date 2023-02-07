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
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
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

        return notificationRepository.findByLuAndUseridOrderByDatenotificationDesc(true, utilisateurs);
    }


/*
    @GetMapping("/recupererLesNotificationLuDunUser/{idUser}")
    public List<Notifications> recupererToutesLesNotificationsDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){

        }
        return notificationRepository.findByLuAndUseridOrderByDatenotification(true, utilisateurs);
    }

 */



    @GetMapping("/recupererLesNotificationNonLuDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsNonLuDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        return notificationRepository.findByLuAndUseridOrderByDatenotificationDesc(false, utilisateurs);
    }


    @GetMapping("/recupererNombreDeNotificationNonLuDunUser/{idUser}")
    public int recupererNombreDeNotificationsNonLuDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        System.out.println(notificationRepository.findByLuAndUseridOrderByDatenotificationDesc(false, utilisateurs).size());

        return notificationRepository.findByLuAndUseridOrderByDatenotificationDesc(false, utilisateurs).size();
    }







    @GetMapping("/recupererLesNotificationDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        return notificationRepository.findByUseridOrderByDatenotificationDesc(utilisateurs);
    }



    @PatchMapping("/modifier/{idnotif}")
    public Object modifier(@PathVariable Long idnotif, @RequestBody Notifications notifications){
        return notificationService.mettrejourNotification(idnotif, notifications);
    }



    @PatchMapping("/marquerlesNotificationdunusercommelus/{idUser}")
    public void marquerLesNotificationDunUserCommeLus(@PathVariable Long idUser, @RequestBody Notifications notifications){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){}

        List<Notifications> notifs = notificationRepository.findByUseridAndLu(utilisateurs, false);

        for (Notifications n: notifs){
            notificationService.mettrejourNotification(n.getId(), notifications);
        }
    }

}
