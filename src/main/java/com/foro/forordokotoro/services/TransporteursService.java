package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Controlleurs.TransporteursControlleur;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Transporteurs;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransporteursService {
    ResponseEntity<?> devenirAgriculteur(Long id, TransporteurAttente transporteurs);

    //List<Transporteurs> recupererTransporteurNonAccepter();

    ResponseEntity<?> accepterTransporteur(String username);

    ResponseEntity<?> rejeterTransporteur(String username);

}
