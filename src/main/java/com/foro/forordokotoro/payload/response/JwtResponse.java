package com.foro.forordokotoro.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private String adresse;
  private String photo;
  private String nomcomplet;
  private List<String> roles;
  private Boolean disponibilite;
  private String numeroplaque;

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, String adresse,
                     String photo, String nomcomplet) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.adresse = adresse;
    this.photo = photo;
    this.nomcomplet = nomcomplet;
  }

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, String adresse,
                     String photo, String nomcomplet, Boolean disponibilite, String numeroplaque) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.adresse = adresse;
    this.photo = photo;
    this.nomcomplet = nomcomplet;
    this.disponibilite = disponibilite;
    this.numeroplaque = numeroplaque;
  }

  /*
  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

   */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}
