package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Transporteurs;
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
}
