package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EtypeChamp;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Champ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Size(max = 120)
    private String nom;
    @Size(max = 10)
    private Long longueur;

    @Size(max = 10)
    private  Long Largeur;

    @Size(max = 100)
    private String adresse;

    @NotBlank
    @Size(max = 60)
    private String photo;

    private Boolean etat;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtypeChamp etypeChamp;

   @ManyToOne
   @JoinColumn(name = "proprietaire_id")
   private Agriculteurs proprietaire;
}
