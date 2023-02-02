package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Notifications;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Object mettrejourNotification(Long id, Notifications notifications) {
        return notificationRepository.findById(id)
                .map(n-> {
                    if(notifications.getLu() != null)
                        n.setLu(notifications.getLu());
                    notificationRepository.save(n);
                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("notification non trouvé ! "));
    }
}
