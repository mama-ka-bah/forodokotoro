package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import org.springframework.http.ResponseEntity;

public interface AimePublicationService {
    ResponseEntity<?> ajouter(AimePublication aimePublication, Utilisateurs utilisateurs, Publications publications);
    ResponseEntity<?> modifier(Long id, AimePublication aimePublication, Publications publications, Utilisateurs utilisateurs);
    ResponseEntity<?> recupererListeDesJaimeDunePublication(Publications publications);
}
