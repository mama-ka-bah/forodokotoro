package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Repository.NotificationRepository;
import com.foro.forordokotoro.Repository.TransporteurEnAttenteRepository;
import com.foro.forordokotoro.Repository.TransporteurRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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


    public void demandeTransporteur(Long id, TransporteurAttente transporteurAttente, String url, String nomfile, MultipartFile file) throws IOException {

        Utilisateurs userExistant = utilisateursRepository.findById(id).get();
        transporteurAttente.setUserid(userExistant);
        transporteurAttente.setPhotopermis(ConfigImages.saveimg(url, nomfile, file));
        transporteurEnAttenteRepository.save(transporteurAttente);
        Notifications notifications = new Notifications();


        String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

        notifications.setContenu(message);
        notifications.setUserid(userExistant);
        notifications.setDateNotification(new Date());
        notifications.setTitre("Demande de profil");
        notifications.setLu(false);
        notificationRepository.save(notifications);

        emailSenderService.sendSimpleEmail(userExistant.getEmail(), notifications.getTitre(), notifications.getContenu());
    }


    @Override
    public ResponseEntity<?> devenirTransporteur(Long id, TransporteurAttente transporteurs, String type, String nomfile, MultipartFile file) throws IOException {

        if(utilisateursRepository.existsById(id)){
            Utilisateurs userExistant = utilisateursRepository.findById(id).get();
            Notifications notifications = new Notifications();

            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " +userExistant);
            if(transporteurEnAttenteRepository.findByUserid(userExistant) == null){
                demandeTransporteur(id, transporteurs, type, nomfile, file);

                String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                if (userExistant.getEmail() != null){
                    emailSenderService.sendSimpleEmail(userExistant.getEmail(),"Demande de profil",message);
                }

                return ResponseEntity.ok(new Reponse(message, 0));
            }else if(transporteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.ENCOURS)){

                String messae = "Veuilez patienter vous avez déjà une demande en cours de traitement";

                return ResponseEntity.ok(new Reponse(messae, 0));
            }else if(transporteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.REJETER)){
                LocalDate datejour = LocalDate.now();
                LocalDate datedemande = transporteurEnAttenteRepository.findByUserid(userExistant).getDatedemande();
                long days_difference = ChronoUnit.DAYS.between(datedemande, datejour);
                if (days_difference < 10){
                    return ResponseEntity.ok(new Reponse("Veuilez attendre 10 jours pour faire une nouvelle demande", 0));
                }else{
                    demandeTransporteur(id, transporteurs, type, nomfile, file);
                    String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                    if (userExistant.getEmail() != null){
                        emailSenderService.sendSimpleEmail(userExistant.getEmail(),"Demande de profil",message);
                    }
                    return ResponseEntity.ok(new Reponse(message, 1));
                }
            }else {
                return ResponseEntity.ok(new Reponse("Vous êtes déjà Transporteur", 0));
            }

        }else {
            return ResponseEntity.ok(new Reponse("Cet utiisateur n'existe pas", 0));

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
                            te.setDateacceptation(LocalDate.now());
                            transporteurEnAttenteRepository.save(te);

                            utilisateursRepository.DEVENIRTRANSPORTEURDEPROFESSION(user.getId());
                            transporteurRepository.DEVENIRTRANSPORTEUR(user.getId(), transporteurAttente.getDisponibilite(), transporteurAttente.getPhotopermis(), transporteurAttente.getNumeroplaque());
                            String message = "Votre demande a étée accepter, vous êtes desormais transporteur";
                            utilisateursRepository.DONNERROLEAUSER(user.getId(), 4L);
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setDateNotification(new Date());
                            notifications.setLu(false);
                            notificationRepository.save(notifications);
                            if(user.getEmail() != null){
                                emailSenderService.sendSimpleEmail(user.getEmail(),"Acceptation de demande",message);
                            }
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
                            notifications.setLu(false);
                            notificationRepository.save(notifications);
                            if(user.getEmail() != null){
                                emailSenderService.sendSimpleEmail(user.getEmail(), notifications.getTitre(), notifications.getContenu());
                            }
                            return ResponseEntity.ok(new Reponse(message, 1));

                        }).orElseThrow(() -> new RuntimeException("Transporteur en attente non trouvé ! "));
            }else {
                return ResponseEntity.ok(new Reponse("Il n' y a pas une demande au nom de cet utilisateur", 1));
            }

        }else {
            return ResponseEntity.ok(new Reponse("Cet utilisateur n'existe pas", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierTransporteur(Long id, Transporteurs transporteurs) {
        return transporteurRepository.findById(id)
                .map(t-> {
                    if(transporteurs.getPassword() != null)
                    t.setPassword(transporteurs.getPassword());
                    if(transporteurs.getEmail() != null)
                    t.setAdresse(transporteurs.getEmail());
                    if(transporteurs.getNomcomplet() != null)
                    t.setNomcomplet(transporteurs.getNomcomplet());
                    if(transporteurs.getUsername() != null)
                    t.setUsername(transporteurs.getUsername());
                    if(transporteurs.getNumeroplaque() != null)
                        t.setNumeroplaque(transporteurs.getNumeroplaque());
                    if(transporteurs.getPhotopermis() != null)
                        t.setPhotopermis(transporteurs.getPhotopermis());
                    if(transporteurs.getDisponibilite() != null)
                        t.setDisponibilite(transporteurs.getDisponibilite());
                    if(transporteurs.getEtat() != null)
                    t.setEtat(transporteurs.getEtat());
                    transporteurRepository.save(t);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Transporteur en attente non trouvé ! "));
    }

    @Override
    public Transporteurs recupererTransporteurParId(Long id) {
        return transporteurRepository.findById(id).get();
    }


}
