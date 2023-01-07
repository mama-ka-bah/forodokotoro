package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Controlleurs.AgriculteurControlleur;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgriculteurService {
   ResponseEntity<?> AjouterAgriculteur(Agriculteurs agriculteurs);

   /* ResponseEntity<?> ModifierAgriculteur(Long id, Agriculteurs agriculteur); */

   ResponseEntity<?> DevenirAgriculteur(Long id, AgricuteurAttente agricuteurAttente);

   ResponseEntity<?> accepterAgriculteur(String username);

   //List<Agriculteurs> recupererAgriculteursNonAccepter();

   ResponseEntity<?> rejeterAgriculteur(String username);
}
