package com.foro.forordokotoro.security.services;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.http.ResponseEntity;

public interface UtilisateurService {
    ResponseEntity<?> modifierUtilisateur(Long id, Utilisateurs utilisateurs);

    ResponseEntity<?> modifierProfil(Long id, String nomfile);
}
