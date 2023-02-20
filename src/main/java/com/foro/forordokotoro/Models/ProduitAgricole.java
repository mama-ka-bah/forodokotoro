package com.foro.forordokotoro.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class ProduitAgricole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nom;

    @NotBlank
    @Size(max = 25)
    private String photo;

    @NotBlank
    @Column(columnDefinition = "text")
    private  String description;
    private Boolean etat;
    private Boolean statusubvention;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Utilisateurs createur;
}
