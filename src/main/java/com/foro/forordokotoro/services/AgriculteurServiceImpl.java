package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Repository.AgriculteurEnAttenteRepository;
import com.foro.forordokotoro.Repository.AgriculteursRepository;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AgriculteurServiceImpl implements AgriculteurService{

    @Autowired
    AgriculteursRepository agriculteursRepository;

    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    AgriculteurEnAttenteRepository agriculteurEnAttenteRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<?> AjouterAgriculteur(Agriculteurs agriculteurs) {
        agriculteursRepository.save(agriculteurs);
        return ResponseEntity.ok(
                new Reponse("Agriculteur ajouter avec succès", 200)
        );
    }

    public void demandeAgricuteur(Long id, AgricuteurAttente agriculteur){
        Utilisateurs userExistant = utilisateursRepository.findById(id).get();
        agriculteur.setUserid(userExistant);
        agriculteurEnAttenteRepository.save(agriculteur);
        Notifications notifications = new Notifications();


        String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

        notifications.setContenu(message);
        notifications.setUserid(userExistant);
        notifications.setDateNotification(new Date());
        notifications.setTitre("Demande de profil");
        notificationRepository.save(notifications);
    }

    @Override
    public ResponseEntity<?> DevenirAgriculteur(Long id, AgricuteurAttente agriculteur) {

        Utilisateurs userExistant = utilisateursRepository.findById(id).get();
        Notifications notifications = new Notifications();

        if(utilisateursRepository.existsById(id)){
            if(agriculteurEnAttenteRepository.findByUserid(userExistant) == null){
                demandeAgricuteur(id, agriculteur);

                String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                return ResponseEntity.ok(new Reponse(message, 1));
            }else if(agriculteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.ENCOURS)){

                String messae = "Veuilez patienter vous avez déjà une demande en cours de traitement";

                return ResponseEntity.ok(new Reponse(messae, 1));
            }else if(agriculteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.REJETER)){
                //SimpleDateFormat obj =  new SimpleDateFormat( "MM-jj-aaaa HH:mm:ss" );
                Date datejour = new Date();
                Date datedemande = agriculteurEnAttenteRepository.findByUserid(userExistant).getDatedemande();
                long time_difference = datejour.getTime() - datedemande.getTime();
                long days_difference = (time_difference / (1000*60*60*24)) % 365;
                if (days_difference < 10){
                    return ResponseEntity.ok(new Reponse("Veuilez attendre 10 jours pour faire une nouvelle demande", 1));
                }else{
                    demandeAgricuteur(id, agriculteur);

                    String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                    return ResponseEntity.ok(new Reponse(message, 1));
                }
            }else {
                return ResponseEntity.ok(new Reponse("Vous êtes déjà agriculteur", 1));
            }
        }else {
            return ResponseEntity.ok(new Reponse("Cet utilisateur nexiste pas", 1));
        }

    }


    @Override
    public ResponseEntity<?> accepterAgriculteur(String username) {

        if(utilisateursRepository.existsByUsername(username)){

            Utilisateurs user = utilisateursRepository.findByUsername(username).get();
            Notifications notifications = new Notifications();

            AgricuteurAttente agricuteurAttente = agriculteurEnAttenteRepository.findByUserid(user);
            if(agricuteurAttente != null){

                return agriculteurEnAttenteRepository.findById(agricuteurAttente.getId())
                        .map(ae-> {
                            ae.setStatusdemande(EstatusDemande.ACCEPTER);
                            ae.setDateacceptation(new Date());
                            agriculteurEnAttenteRepository.save(ae);

                            utilisateursRepository.DEVENIRAGRICULTEURDEPROFESSION(user.getId());
                            agriculteursRepository.DEVENIRAGRICULTEUR(user.getId(), agricuteurAttente.getPhotocarteidentite());
                            utilisateursRepository.DONNERROLEAUSER(user.getId(), 3L);
                            String message = "Votre demande a étée accepter, vous êtes desormais agriculteur";
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setDateNotification(new Date());
                            notificationRepository.save(notifications);
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
    public ResponseEntity<?> rejeterAgriculteur(String username) {

        Utilisateurs user = utilisateursRepository.findByUsername(username).get();
        Notifications notifications = new Notifications();

        if(user != null){
            AgricuteurAttente agricuteurAttente = agriculteurEnAttenteRepository.findByUserid(user);
            return agriculteurEnAttenteRepository.findById(agricuteurAttente.getId())
                    .map(ae-> {
                        ae.setStatusdemande(EstatusDemande.REJETER);
                        //a.setDateacceptation(new Date());
                        agriculteurEnAttenteRepository.save(ae);

                        String message = "Nous sommes desolé de vous annoncer que votre demande n'a pas été accepté";
                        notifications.setContenu(message);
                        notifications.setUserid(user);
                        notifications.setDateNotification(new Date());
                        notificationRepository.save(notifications);
                        return ResponseEntity.ok(new Reponse(message, 1));

                    }).orElseThrow(() -> new RuntimeException("Agriculteur en attente non trouvé ! "));
        }else {
            return ResponseEntity.ok(new Reponse("Il n' y a pas une demande au nom de cet utilisateur", 1));
        }

       }

}
