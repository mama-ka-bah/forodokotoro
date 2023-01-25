package com.foro.forordokotoro.security;

import com.foro.forordokotoro.security.jwt.AuthEntryPointJwt;
import com.foro.forordokotoro.security.jwt.AuthTokenFilter;
import com.foro.forordokotoro.security.services.UtilisateursDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
Pour aider Spring Security à savoir quand nous voulons exiger que tous les utilisateurs soient
authentifiés, quel gestionnaire d'exceptions choisir, quel filtre et quand nous voulons qu'il fonctionne.
 Nous implémentons WebSecurityConfigurerAdapteret fournissons une configuration dans la
 configure(HttpSecurity http)

 */

/*
* HttpSecurity configurations pour configurer cors, csrf, la gestion de session, les règles
*  pour les ressources protégées.
* */

/*
* @EnableGlobalMethodSecurityassure la sécurité AOP sur les méthodes. Il permet @PreAuthorize,
*  @PostAuthorize, il prend également en charge JSR-250 .
* */

/*
@EnableWebSecurity permet à Spring de trouver et d'appliquer automatiquement
la sécurité à la classe Web globale.
 */


/*
L'implémentation de UserDetailsService sera utilisée pour la configuration DaoAuthenticationProviderpar
 AuthenticationManagerBuilder.userDetailsService()méthode.

– Nous avons également besoin d'un PasswordEncoderpour le DaoAuthenticationProvider. Si nous ne le
spécifions pas, il utilisera du texte brut.
 */

//L'accès à l'API Restful est protégé par HTTPSecurity et autorisé avec Method Security

/*
Spring Security fournit des annotations pour les vérifications d'autorisation avant et après l'invocation, le filtrage des arguments de collection soumis
ou les valeurs de retour : @PreAuthorize, @PreFilteret .@PostAuthorize@PostFilter

Pour activer les expressions de sécurité de méthode , nous utilisons l' @EnableGlobalMethodSecurityannotation
 */

@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true)
public class WebSecurityConfig {



  @Autowired
  UtilisateursDetailsServiceImpl userDetailsService;


 /*
  Si l'utilisateur demande une ressource HTTP sécurisée sans être authentifié,
  AuthenticationEntryPointsera appelé.
   A ce moment, une AuthenticationExceptionest lancée, commence()la méthode sur
   le point d'entrée est déclenchée :
  */
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }


  /*
  DaoAuthenticationProvider, qui récupère les détails de l'utilisateur à partir d'un fichier
  UserDetailsService
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {


      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    /*
    - UserDetailsService L'interface a une méthode pour charger l'utilisateur par nom d' utilisateur
    et renvoie un UserDetailsobjet que Spring Security peut utiliser pour l'authentification et la
    validation.

    – UserDetails contient les informations nécessaires (telles que : nom d'utilisateur, mot de passe,
     autorités) pour construire un objet d'authentification.
     */

      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }


  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /*
  Pour activer la sécurité HTTP dans Spring, nous devons créer un bean SecurityFilterChain

  La configuration ci-dessus garantit que toute demande adressée à l'application est authentifiée
  avec une connexion basée sur un formulaire ou une authentification de base HTTP.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/transporteurs/**").permitAll()
        .antMatchers("/api/bienvenue/**").permitAll()
        .anyRequest().authenticated();
           // .and()
            //.oauth2Login();
   // http.formLogin();

    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

}
