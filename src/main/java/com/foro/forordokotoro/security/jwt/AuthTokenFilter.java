package com.foro.forordokotoro.security.jwt;

import com.foro.forordokotoro.security.services.UtilisateursDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
OncePerRequestFilter fait une seule exécution pour chaque requête à notre API.
Il fournit une doFilterInternal() méthode que nous allons implémenter pour analyser et
valider JWT, charger les détails de l'utilisateur (à l'aide de UserDetailsService), vérifier
l'autorisation (à l'aide de UsernamePasswordAuthenticationToken).

 */


public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UtilisateursDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

/*
  pour analyser et
  valider JWT, charger les détails de l'utilisateur (à l'aide de UserDetailsService), vérifier
  l'autorisation

 */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      //analyse et retourne le jwt si son format est correcte sinon il retourne null
      String jwt = parseJwt(request);

      //Lorsque le jwt n'est pas null
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

        //recupere username de l'utilisateur à partir du token
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        /*
        recuperer les details de l'utilisateur à partir de son token à savoir:
          String getPassword();
          String getUsername();
          boolean isAccountNonExpired();
          boolean isAccountNonLocked();
          boolean isCredentialsNonExpired();
          boolean isEnabled();
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        /*
        UsernamePasswordAuthenticationToken obtient le nom d'utilisateur/mot de passe de la demande de
        connexion et se combine dans une instance d' Authenticationinterface.
         */
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


      /*
        SecurityContextHolder permet d'accéder au SecurityContext.
        SecurityContextdétient les Authenticationinformations de sécurité et éventuellement spécifiques à la demande.
        Authenticationreprésente le principal qui inclut GrantedAuthorityqui reflète les autorisations à l'échelle de l'application accordées à un principal.
       */

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("ne peut pas être modifié: {}", e);//Cannot set user authentication
    }

    filterChain.doFilter(request, response);
  }


  //analyse
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    /*
  ----hasText Vérifiez si la chaîne donnée contient du texte réel.
  Plus précisément, cette méthode renvoie true si la chaîne n'est pas nulle,
  si sa longueur est supérieure à 0 et si elle contient au moins un caractère non blanc.

  ---startsWith verifie si une chaine commence par un prefixe
   */

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

      //permet de retourner le jwt sans le mot clé Bearer
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
