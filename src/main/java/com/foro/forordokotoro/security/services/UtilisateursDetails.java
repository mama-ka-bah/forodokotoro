package com.foro.forordokotoro.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foro.forordokotoro.Models.Utilisateurs;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class UtilisateursDetails implements UserDetails {

  private static final long serialVersionUID = 1L;


  private Long id;

  private String username;

  private String email;

  private String nomcomplet;

  private String photo;

  private String adresse;

  @JsonIgnore
  private String password;

/*
  GrantedAuthorityest une autorité accordée au mandant. Ces autorités sont généralement
  des "rôles", tels que ROLE_ADMIN, ROLE_PM, ROLE_USER…
 */
  private Collection<? extends GrantedAuthority> authorities;

  public UtilisateursDetails(Long id, String username, String email, String password,
                             Collection<? extends GrantedAuthority> authorities , String adresse,
                             String photo, String nomcomplet) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.adresse = adresse;
    this.nomcomplet = nomcomplet;
    this.photo = photo;
  }

  //return l'user avec tous ces droits et toutes ces informations
  public static UtilisateursDetails build(Utilisateurs user) {

    /*
    * Dans Spring Security, nous pouvons considérer chaque GrantedAuthority comme un privilège individuel.
    *  Les exemples peuvent inclure READ_AUTHORITY , WRITE_PRIVILEGE
    * De même, dans Spring Security, nous pouvons considérer chaque rôle comme une GrantedAuthority à
    * gros grains représentée sous la forme d'une chaîne et préfixée par « ROLE »
     * */

    //Stream est utilisée pour traiter des collections d'objets
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
        .collect(Collectors.toList());

    //on cree retourne un objet UtilisateursDetails
    return new UtilisateursDetails(
        user.getId(), 
        user.getUsername(), 
        user.getEmail(),
        user.getPassword(),
        authorities,
        user.getAdresse(),
        user.getPhoto(),
        user.getNomcomplet()
    );
  }

  //recupere les information l'user de l'utilisateur
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UtilisateursDetails user = (UtilisateursDetails) o;
    return Objects.equals(id, user.id);
  }
}
