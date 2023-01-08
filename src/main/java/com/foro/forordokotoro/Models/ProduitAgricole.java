package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class ProduitAgricole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String photo;
    private  String description;
    private Boolean etat;
    private Boolean statusSubvention;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "produitagricole_varietes",
            joinColumns = @JoinColumn(name = "produit_id"),
            inverseJoinColumns = @JoinColumn(name = "varietes_id"))
    private List<Varietes> roles = new ArrayList<>();



}
