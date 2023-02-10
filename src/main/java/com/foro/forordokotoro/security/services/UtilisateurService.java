package com.foro.forordokotoro.security.services;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Utils.response.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UtilisateurService {
    ResponseEntity<JwtResponse> modifierUtilisateur(Long id, Utilisateurs utilisateurs);

    ResponseEntity<?> modifierProfil(Long id, String nomfile, String type, MultipartFile file);

    List<Utilisateurs> recupererUtilisateurActive();
}
