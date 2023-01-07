package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.TransporteurEnAttenteRepository;
import com.foro.forordokotoro.Repository.TransporteurRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransporteursServiceImpl implements TransporteursService{

    @Autowired
    TransporteurRepository transporteurRepository;

    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    TransporteurEnAttenteRepository transporteurEnAttenteRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    EmailSenderService emailSenderService;


    public void demandeTransporteur(Long id, TransporteurAttente transporteurAttente){
        Utilisateurs userExistant = utilisateursRepository.findById(id).get();
        transporteurAttente.setUserid(userExistant);
        transporteurEnAttenteRepository.save(transporteurAttente);
        Notifications notifications = new Notifications();


        String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

        notifications.setContenu(message);
        notifications.setUserid(userExistant);
        notifications.setDateNotification(new Date());
        notifications.setTitre("Demande de profil");
        notificationRepository.save(notifications);

        emailSenderService.sendSimpleEmail(userExistant.getEmail(), notifications.getTitre(), notifications.getContenu());
    }


    @Override
    public ResponseEntity<?> devenirAgriculteur(Long id, TransporteurAttente transporteurs) {

        if(utilisateursRepository.existsById(id)){
            Utilisateurs userExistant = utilisateursRepository.findById(id).get();
            Notifications notifications = new Notifications();

            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " +userExistant);
            if(transporteurEnAttenteRepository.findByUserid(userExistant) == null){
                demandeTransporteur(id, transporteurs);

                String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                return ResponseEntity.ok(new Reponse(message, 1));
            }else if(transporteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.ENCOURS)){

                String messae = "Veuilez patienter vous avez déjà une demande en cours de traitement";

                return ResponseEntity.ok(new Reponse(messae, 1));
            }else if(transporteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.REJETER)){
                //SimpleDateFormat obj =  new SimpleDateFormat( "MM-jj-aaaa HH:mm:ss" );
                Date datejour = new Date();
                Date datedemande = transporteurEnAttenteRepository.findByUserid(userExistant).getDatedemande();
                long time_difference = datejour.getTime() - datedemande.getTime();
                long days_difference = (time_difference / (1000*60*60*24)) % 365;
                if (days_difference < 10){
                    return ResponseEntity.ok(new Reponse("Veuilez attendre 10 jours pour faire une nouvelle demande", 1));
                }else{
                    demandeTransporteur(id, transporteurs);

                    String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                    return ResponseEntity.ok(new Reponse(message, 1));
                }
            }else {
                return ResponseEntity.ok(new Reponse("Vous êtes déjà Transporteur", 1));
            }

        }else {
            return ResponseEntity.ok(new Reponse("Cet utiisateur n'existe pas", 1));

        }
    }


    @Override
    public ResponseEntity<?> accepterTransporteur(String username) {
        if(utilisateursRepository.existsByUsername(username)){

            Utilisateurs user = utilisateursRepository.findByUsername(username).get();
            Notifications notifications = new Notifications();

            TransporteurAttente transporteurAttente = transporteurEnAttenteRepository.findByUserid(user);
            if(transporteurAttente != null){

                return transporteurEnAttenteRepository.findById(transporteurAttente.getId())
                        .map(te-> {
                            te.setStatusdemande(EstatusDemande.ACCEPTER);
                            te.setDateacceptation(new Date());
                            transporteurEnAttenteRepository.save(te);

                            utilisateursRepository.DEVENIRAGRICULTEURDEPROFESSION(user.getId());
                            transporteurRepository.DEVENIRTRANSPORTEUR(user.getId(), transporteurAttente.getDisponibilite(), transporteurAttente.getPhotopermis(), transporteurAttente.getNumeroplaque());
                            String message = "Votre demande a étée accepter, vous êtes desormais transporteur";
                            utilisateursRepository.DONNERROLEAUSER(user.getId(), 4L);
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setDateNotification(new Date());
                            notificationRepository.save(notifications);
                            emailSenderService.sendSimpleEmail(user.getEmail(),"Acceptation de demande",message);
                            return ResponseEntity.ok(new Reponse(message, 1));
                        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            }else {
                return ResponseEntity.ok(new Reponse("Cet utilisateur n' a pas éffectué une demande", 1));
            }
        }else {
            return ResponseEntity.ok(new Reponse("Cet utilisateur n'existe pas", 0));
        }
    }

    @Override
    public ResponseEntity<?> rejeterTransporteur(String username) {

        if(utilisateursRepository.existsByUsername(username)){
            Utilisateurs user = utilisateursRepository.findByUsername(username).get();
            Notifications notifications = new Notifications();

            if(utilisateursRepository.existsByUsername(username)){
                TransporteurAttente transporteurAttente = transporteurEnAttenteRepository.findByUserid(user);
                return transporteurEnAttenteRepository.findById(transporteurAttente.getId())
                        .map(ae-> {
                            ae.setStatusdemande(EstatusDemande.REJETER);
                            //a.setDateacceptation(new Date());
                            transporteurEnAttenteRepository.save(ae);

                            String message = "Nous sommes desolé de vous annoncer que votre demande n'a pas été accepté";
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setTitre("Rejet de demande");
                            notifications.setDateNotification(new Date());
                            notificationRepository.save(notifications);
                            emailSenderService.sendSimpleEmail(user.getEmail(), notifications.getTitre(), notifications.getContenu());
                            return ResponseEntity.ok(new Reponse(message, 1));

                        }).orElseThrow(() -> new RuntimeException("Transporteur en attente non trouvé ! "));
            }else {
                return ResponseEntity.ok(new Reponse("Il n' y a pas une demande au nom de cet utilisateur", 1));
            }

        }else {
            return ResponseEntity.ok(new Reponse("Cet utilisateur n'existe pas", 1));
        }
    }
}
