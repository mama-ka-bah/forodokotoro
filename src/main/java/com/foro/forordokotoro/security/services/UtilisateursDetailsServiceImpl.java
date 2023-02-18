package com.foro.forordokotoro.security.services;


import com.foro.forordokotoro.Models.Enumerations.ERole;
import com.foro.forordokotoro.Models.Role;
import com.foro.forordokotoro.Models.Utilisateurs;

import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Repository.RoleRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.JwtResponse;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
  public ResponseEntity<JwtResponse> modifierUtilisateur(Long id, Utilisateurs utilisateurs) {
    return utilisateursRepository.findById(id)
            .map(u-> {
                if(utilisateurs.getPassword() != null)
              u.setPassword(utilisateurs.getPassword());
                if(utilisateurs.getAdresse() != null)
              u.setAdresse(utilisateurs.getAdresse());
                if(utilisateurs.getNomcomplet() != null)
              u.setNomcomplet(utilisateurs.getNomcomplet());
                if(utilisateurs.getUsername() != null)
              u.setUsername(utilisateurs.getUsername());
                if(utilisateurs.getEtat() != null)
              u.setEtat(utilisateurs.getEtat());
                if(utilisateurs.getEmail() != null)
                  u.setEmail(utilisateurs.getEmail());
                if(utilisateurs.getSesouvenir() != null)
                    u.setSesouvenir(utilisateurs.getSesouvenir());

                Utilisateurs user = utilisateursRepository.save(u);

                List<String> rolesstr =  new ArrayList<>();

                for (Role role : user.getRoles()) {
                    rolesstr.add(role.getName().toString());
                }


/*
                //Role roles = roleRepository.findByName(ERole.ROLE_USER);
                if (user.getRoles().contains(ERole.ROLE_AGRIGULTEUR)){
                    rolesstr.add("ROLE_AGRICULTEUR");
                    rolesstr.add("ROLE_USER");
                    System.out.println("1 " + user.getRoles());
                }else {
                    rolesstr.add("ROLE_USER");
                    System.out.println("2 " + user.getRoles());
                }

 */

                return ResponseEntity.ok(new JwtResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        rolesstr,
                        user.getAdresse(),
                        user.getPhoto(),
                        user.getNomcomplet(),
                        user.getEtat(),
                        user.getSesouvenir()
                ));

            }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé ! "));
           }


    @Override
    public ResponseEntity<?> modifierProfil(Long id, String nomfile, String type, MultipartFile file) {
        return utilisateursRepository.findById(id)
                .map(u-> {
                    try {
                        u.setPhoto(ConfigImages.saveimg(type, nomfile, file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                  utilisateursRepository.save(u);


                    return ResponseEntity.ok(new Reponse(u.getPhoto(), 1));

                }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé ! "));
    }

    @Override
    public List<Utilisateurs> recupererUtilisateurActive() {
        return utilisateursRepository.findByEtat(true);
    }

}



