package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("*")
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

    @GetMapping("/recupererLesNotificationNonLuDunUser/{idUser}")
    public List<Notifications> recupererLesNotificationsNonLuDunUser(@PathVariable Long idUser){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
            System.out.println("ccc: " +utilisateurs);
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

    @GetMapping("/recuperertouslesnotification")
    public ResponseEntity<?> recupererLesNotification(){
        return ResponseEntity.ok(notificationRepository.FINDALLNOTIFICATIONS());
    }

    @GetMapping("/recupererlesquatredernierenotification")
    public ResponseEntity<?> recupererLesQuatreDerniereNotification(){
        return ResponseEntity.ok(notificationRepository.FINDQUATRERECENTESNOTIFICATIONS());
    }



    @PatchMapping("/modifier/{idnotif}")
    public Object modifier(@PathVariable Long idnotif, @RequestBody Notifications notifications){
        return notificationService.mettrejourNotification(idnotif, notifications);
    }



    @PatchMapping("/marquerlesNotificationdunusercommelus/{idUser}")
    public ResponseEntity<?> marquerLesNotificationDunUserCommeLus(@PathVariable Long idUser, @RequestBody Notifications notifications){
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = utilisateursRepository.findById(idUser).get();
        }catch (Exception e){
            return ResponseEntity.ok(new Reponse("Echec", 1));
        }

        List<Notifications> notifs = notificationRepository.findByUseridAndLu(utilisateurs, false);

        for (Notifications n: notifs){
            notificationService.mettrejourNotification(n.getId(), notifications);
        }

        return ResponseEntity.ok(new Reponse("Notification lu", 1));
    }

}
