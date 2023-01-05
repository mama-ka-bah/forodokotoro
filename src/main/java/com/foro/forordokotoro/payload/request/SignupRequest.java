package com.foro.forordokotoro.payload.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(min = 3, max = 50)
  @Email
  private String email;


  @NotBlank
  @Size(min = 3, max = 80)
  private String nomcomplet;

  @NotBlank
  @Size(min = 3, max = 25)
  private String photo;

  @NotBlank
  @Size(min = 3, max = 120)
  private String adresse;

  @NotBlank
  @Size(min = 3, max = 120)
  private String photopermis;

  @NotBlank
  @Size(min = 3, max = 120)
  private String numeroplaque;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
