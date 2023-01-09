package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Prix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 25)
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
