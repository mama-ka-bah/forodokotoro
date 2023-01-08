package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Prix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long valeur;
    private LocalDate datedebut;
    private LocalDate datefin;

    @ManyToOne
    @JoinColumn(name = "produit_agricole_id")
    private ProduitAgricole produitAgricole;

    @ManyToOne
    @JoinColumn(name = "regions_id")
    private Regions regions;
}
