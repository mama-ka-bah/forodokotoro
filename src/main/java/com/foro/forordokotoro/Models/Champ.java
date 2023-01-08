package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Champ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long longueur;
    private  Long Largeur;
    private Long surface;
    private Long adresse;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;
}
