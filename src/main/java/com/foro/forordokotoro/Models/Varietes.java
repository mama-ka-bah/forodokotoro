package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Varietes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 25)
    private String nom;

    @NotBlank
    @Size(max = 60)
    private Long cycle;

    @Size(max = 25)
    private Long taillefinal;

    @NotBlank
    @Size(max = 25)
    private String photo;

    @NotBlank
    @Size(max = 255)
    private  String description;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "produit_agricole_id")
    private ProduitAgricole produitagricole;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "varietes_previsions",
            joinColumns = @JoinColumn(name = "varietes_id"),
            inverseJoinColumns = @JoinColumn(name = "previsions_id"))
    private List<Previsions> previsions = new ArrayList<>();
}
