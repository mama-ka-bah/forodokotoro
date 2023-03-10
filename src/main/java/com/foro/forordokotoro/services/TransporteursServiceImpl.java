package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Repository.*;
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

    @Autowired
    ReservationRepository reservationRepository;


    public void demandeTransporteur(Long id, TransporteurAttente transporteurAttente, String url, String nomfile, MultipartFile file) throws IOException {

        Utilisateurs userExistant = utilisateursRepository.findById(id).get();
        transporteurAttente.setUserid(userExistant);
        transporteurAttente.setPhotopermis(ConfigImages.saveimg(url, nomfile, file));
        transporteurEnAttenteRepository.save(transporteurAttente);
        Notifications notifications = new Notifications();


        String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

        notifications.setContenu(message);
        notifications.setUserid(userExistant);
        notifications.setDatenotification(new Date());
        notifications.setTitre("Demande de profil");
        notifications.setLu(false);
        notificationRepository.save(notifications);

        emailSenderService.sendSimpleEmail(userExistant.getEmail(), notifications.getTitre(), notifications.getContenu());
    }


    @Override
    public ResponseEntity<?> devenirTransporteur(Long id, TransporteurAttente transporteurs, String type, String nomfile, MultipartFile file) throws IOException {

        if(utilisateursRepository.existsById(id)){
            Utilisateurs userExistant = utilisateursRepository.findById(id).get();

            if(transporteurEnAttenteRepository.findByUserid(userExistant) == null){
                demandeTransporteur(id, transporteurs, type, nomfile, file);

                String message = "Votre demande est en cours de traitement, nous vous reviendrons dans un delai de 24h";

                if (userExistant.getEmail() != null){
                    emailSenderService.sendSimpleEmail(userExistant.getEmail(),"Demande de profil",message);
                }

                return ResponseEntity.ok(new Reponse(message, 1));
            }else if(transporteurEnAttenteRepository.findByUserid(userExistant).getStatusdemande().equals(EstatusDemande.ENCOURS)){

                String messae = "Veuilez patienter vous avez d??j?? une demande en cours de traitement";

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
                return ResponseEntity.ok(new Reponse("Vous ??tes d??j?? Transporteur", 0));
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
                            transporteurRepository.DEVENIRTRANSPORTEUR(user.getId(), transporteurAttente.getDisponibilite(), transporteurAttente.getPhotopermis(), transporteurAttente.getNumeroplaque(), 0L);
                            String message = "Votre demande a ??t??e accepter, vous ??tes desormais transporteur";
                            utilisateursRepository.DONNERROLEAUSER(user.getId(), 5L);
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setDatenotification(new Date());
                            notifications.setLu(false);
                            notificationRepository.save(notifications);
                            if(user.getEmail() != null){
                                emailSenderService.sendSimpleEmail(user.getEmail(),"Acceptation de demande",message);
                            }
                            return ResponseEntity.ok(new Reponse("Demande Accepter", 1));
                        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouv??"));

            }else {
                return ResponseEntity.ok(new Reponse("Cet utilisateur n' a pas ??ffectu?? une demande", 1));
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

                            String message = "Nous sommes desol?? de vous annoncer que votre demande n'a pas ??t?? accept??";
                            notifications.setContenu(message);
                            notifications.setUserid(user);
                            notifications.setTitre("Rejet de demande");
                            notifications.setDatenotification(new Date());
                            notifications.setLu(false);
                            notificationRepository.save(notifications);
                            if(user.getEmail() != null){
                               emailSenderService.sendSimpleEmail(user.getEmail(), notifications.getTitre(), notifications.getContenu());
                            }
                            return ResponseEntity.ok(new Reponse("Demande rejeter", 1));

                        }).orElseThrow(() -> new RuntimeException("Transporteur en attente non trouv?? ! "));
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
                    t.setAdresse(transporteurs.getAdresse());
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

                    return new ResponseEntity<>("Modification re??u", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Transporteur en attente non trouv?? ! "));
    }

    @Override
    public Transporteurs recupererTransporteurParId(Long id) {
        return transporteurRepository.findById(id).get();
    }

    @Override
    public ResponseEntity<?> contacter(Transporteurs transporteurs, Utilisateurs utilisateurs) {
        Notifications notifications = new Notifications();
        String message = "Vous avez ??t?? contacter par " + utilisateurs.getNomcomplet() + " ?? la date du " + LocalDate.now();
        notifications.setTitre("Reservation");
        notifications.setContenu(message);
        notifications.setDatenotification(new Date());
        notifications.setUserid(transporteurs);
        notifications.setLu(false);
        notificationRepository.save(notifications);
        transporteurs.setNombrecontact(transporteurs.getNombrecontact() +1);

        modifierTransporteur(transporteurs.getId(), transporteurs);

        reservationRepository.save(new Reservation(utilisateurs, new Date(), EstatusDemande.ENCOURS, transporteurs));

        //emailSenderService.sendSimpleEmail(transporteurs.getEmail(), "Reservation", message);
        return ResponseEntity.ok(new Reponse(transporteurs.getNomcomplet() + " a ??t?? reserv?? avec succ??s", 1));
    }


    @Override
    public Reponse modifierReservation(Long id, Reservation reservation) {
        return reservationRepository.findById(id)
                .map(r-> {
                    if(reservation.getStatus() != null)
                        r.setStatus(reservation.getStatus());

                    reservationRepository.save(r);

                    return new Reponse("Modification re??u", 1);

                }).orElseThrow(() -> new RuntimeException("Reservation en attente non trouv?? ! "));
    }

    @Override
    public ResponseEntity<?> accepterRerservation(Reservation reservation) {

        List<Reservation> reservationsEncours = reservationRepository.findByStatusAndTransporteur(EstatusDemande.ENCOURS, reservation.getTransporteur());

        for(Reservation r: reservationsEncours){
            Reservation rTmp = new Reservation();
            rTmp.setStatus(EstatusDemande.ENATTENTE);
            modifierReservation(r.getId(), rTmp);
        }

        Transporteurs transporteurs = new Transporteurs();
        transporteurs.setDisponibilite(false);

        modifierTransporteur(reservation.getTransporteur().getId(), transporteurs);

        reservation.setStatus(EstatusDemande.ACCEPTER);
        if(modifierReservation(reservation.getId(), reservation).getStatus() == 1){
            return ResponseEntity.ok(new Reponse("Reservation accept??e", 1));
        }else {
            return ResponseEntity.ok(new Reponse("Echec", 0));
        }

    }

    @Override
    public ResponseEntity<?> rejeterRerservation(Reservation reservation) {
        reservation.setStatus(EstatusDemande.REJETER);

        if(modifierReservation(reservation.getId(), reservation).getStatus() == 1){
            return ResponseEntity.ok(new Reponse("Reservation rejet??e avec succ??s", 1));
        }else {
            return ResponseEntity.ok(new Reponse("Echec", 0));
        }

    }

    @Override
    public ResponseEntity<?> mettrefinAUneRerservation(Transporteurs transporteurs, Reservation reservation) {

        List<Reservation> reservationsEncours = reservationRepository.findByStatusAndTransporteur(EstatusDemande.ENATTENTE, reservation.getTransporteur());

        for(Reservation r: reservationsEncours){
            Reservation rTmp = new Reservation();
            rTmp.setStatus(EstatusDemande.ENCOURS);
            modifierReservation(r.getId(), rTmp);
        }

        reservation.setStatus(EstatusDemande.TERMINER);
        transporteurs.setDisponibilite(true);
        modifierTransporteur(transporteurs.getId(), transporteurs);
        if(modifierReservation(reservation.getId(), reservation).getStatus() == 1){
            return ResponseEntity.ok(new Reponse("Reservation termin??e", 1));
        } else {
            return ResponseEntity.ok(new Reponse("Echec", 0));
        }
    }


}
