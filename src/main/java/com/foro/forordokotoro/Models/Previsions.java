package com.foro.forordokotoro.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Previsions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String libelle;

    private  Long delaijour;
    private Long nbrepluienecessaire;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "varietes_id")
    private Varietes varietes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Utilisateurs createur;

}
