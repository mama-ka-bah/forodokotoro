package com.foro.forordokotoro.security.services;


import com.foro.forordokotoro.Models.Utilisateurs;

import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*

UserDetailsService est décrit comme une interface principale qui charge des données spécifiques à
l'utilisateur dans la documentation Spring.

 */

@Service
public class UtilisateursDetailsServiceImpl implements UserDetailsService, UtilisateurService {

    @Autowired
    UtilisateursRepository utilisateursRepository;

  @Autowired
  RoleRepository roleRepository;

  //recupere les details du collaborateur
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Utilisateurs user = utilisateursRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

    return UtilisateursDetails.build(user);
  }


  @Override
  public ResponseEntity<?> modifierUtilisateur(Long id, Utilisateurs utilisateurs) {
    return utilisateursRepository.findById(id)
            .map(u-> {
                if(utilisateurs.getPassword() != null)
              u.setPassword(utilisateurs.getPassword());
                if(utilisateurs.getAdresse() != null)
              u.setAdresse(utilisateurs.getAdresse());
              u.setNomcomplet(utilisateurs.getNomcomplet());
                if(utilisateurs.getUsername() != null)
              u.setUsername(utilisateurs.getUsername());
                if(utilisateurs.getEtat() != null)
              u.setEtat(utilisateurs.getEtat());
                if(utilisateurs.getEmail() != null)
              u.setEmail(utilisateurs.getEmail());
              utilisateursRepository.save(u);

              return new ResponseEntity<>("Modification reçue", HttpStatus.OK);

            }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé ! "));
           }


    @Override
    public ResponseEntity<?> modifierProfil(Long id, String nomfile) {
        return utilisateursRepository.findById(id)
                .map(u-> {
                    u.setPhoto(nomfile);
                    utilisateursRepository.save(u);
                    return new ResponseEntity<>("Modification reçue", HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé ! "));
    }

    @Override
    public List<Utilisateurs> recupererUtilisateurActive() {
        return utilisateursRepository.findByEtat(true);
    }

}



