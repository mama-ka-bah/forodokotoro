package com.foro.forordokotoro.Controlleurs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bienvenue")
public class BienvenuController {

  private static final Logger log = LoggerFactory.getLogger(BienvenuController.class);



  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String allAccess() {

/*
  SecurityContextHolder permet d'accéder au SecurityContext.
  SecurityContextdétient les Authenticationinformations de sécurité et éventuellement spécifiques à la demande.
  Authenticationreprésente le principal qui inclut GrantedAuthorityqui reflète les autorisations à l'échelle de l'application accordées à un principal.
 */

    Object principal = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

      String username = ((UserDetails)principal).getUsername();
      Collection<? extends GrantedAuthority> role = ((UserDetails)principal).getAuthorities();

      log.info("bienvenue controlleur");
      return "Bienvenue " + username + " Votre role est  " + role.toString().substring(1, role.toString().length()-1);
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {

    return "Bienvenue admin.";
  }

}
