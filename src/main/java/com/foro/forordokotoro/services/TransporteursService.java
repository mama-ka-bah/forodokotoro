package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Reservation;
import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Transporteurs;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TransporteursService {
    ResponseEntity<?> devenirTransporteur(Long id, TransporteurAttente transporteurs, String type, String nomfile, MultipartFile file) throws IOException;

    //List<Transporteurs> recupererTransporteurNonAccepter();

    ResponseEntity<?> accepterTransporteur(String username);

    ResponseEntity<?> rejeterTransporteur(String username);

    //ResponseEntity<?> modifierAgriculteur(Long id, Transporteurs transporteurs);

    ResponseEntity<?> modifierTransporteur(Long id, Transporteurs transporteurs);

    Transporteurs recupererTransporteurParId(Long id);

    ResponseEntity<?> contacter(Transporteurs transporteurs, Utilisateurs utilisateurs);

//    ResponseEntity<?> accepterReservation(Long id, Transporteurs transporteurs);

    Reponse modifierReservation(Long id, Reservation reservation);

    ResponseEntity<?> accepterRerservation(Reservation reservation);

    ResponseEntity<?> rejeterRerservation(Reservation reservation);

    ResponseEntity<?> mettrefinAUneRerservation(Transporteurs transporteurs, Reservation reservation);
}
