package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
    /*
    * garantissent l'unicité d'une colonne ou d'un ensemble de colonnes.
    * */
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
@Data
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="profession", discriminatorType = DiscriminatorType.STRING)
public class Utilisateurs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @Size(max = 80)
  private String nomcomplet;

  @NotBlank
  @Size(max = 25)
  private String photo;

  @NotBlank
  @Size(max = 120)
  private String adresse;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public Utilisateurs() {
  }

  public Utilisateurs(String username, String email, String password, String adresse,
   String nomcomplet, String photo)
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.adresse = adresse;
    this.nomcomplet = nomcomplet;
    this.photo = photo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

}
