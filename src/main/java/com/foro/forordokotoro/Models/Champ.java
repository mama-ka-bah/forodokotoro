package com.foro.forordokotoro.Models;

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

    @Size(max = 100)
    private String longitude;

    @Size(max = 100)
    private String latitude;

    @Size(max = 10)
    private Long nombreParserelle;

    @NotBlank
    @Size(max = 60)
    private String photo;

    private Boolean etat;

    /*
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusChamps status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtypeChamp etypeChamp;
     */

   @ManyToOne
   @JoinColumn(name = "proprietaire_id")
   private Agriculteurs proprietaire;
}
