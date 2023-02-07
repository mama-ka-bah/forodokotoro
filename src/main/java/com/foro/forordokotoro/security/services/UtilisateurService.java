package com.foro.forordokotoro.security.services;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Utils.response.JwtResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UtilisateurService {
    ResponseEntity<JwtResponse> modifierUtilisateur(Long id, Utilisateurs utilisateurs);

    ResponseEntity<?> modifierProfil(Long id, String nomfile);

    List<Utilisateurs> recupererUtilisateurActive();
}
