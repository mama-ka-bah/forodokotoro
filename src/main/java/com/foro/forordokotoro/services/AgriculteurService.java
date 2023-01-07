package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Controlleurs.AgriculteurControlleur;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AgriculteurService {
   ResponseEntity<?> AjouterAgriculteur(Agriculteurs agriculteurs);

   /* ResponseEntity<?> ModifierAgriculteur(Long id, Agriculteurs agriculteur); */

   ResponseEntity<?> DevenirAgriculteur(Long id, AgricuteurAttente agricuteurAttente, String url, String nomfile, MultipartFile file) throws IOException;

   ResponseEntity<?> accepterAgriculteur(String username);

   //List<Agriculteurs> recupererAgriculteursNonAccepter();

   ResponseEntity<?> rejeterAgriculteur(String username);

   ResponseEntity<?> modifierAgriculteur(Long id, Agriculteurs agriculteurs);
}
