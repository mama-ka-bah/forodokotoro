package com.foro.forordokotoro.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    private Double longueur;

    @Size(max = 10)
    private  Double Largeur;

    @Size(max = 100)
    private String adresse;

    @Size(max = 100)
    private Double longitude;

    @Size(max = 100)
    private Double latitude;

    @Size(max = 10)
    private Long nombreParserelle;

    @NotBlank
    @Size(max = 60)
    private String photo;

    private LocalDateTime datecreation;

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
