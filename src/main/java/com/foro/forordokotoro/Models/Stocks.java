package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String libelle;

    @Size(max = 25)
    private Long prixkilo;

    @Size(max = 25)
    private Long  nombrekilo;

    private Long quantiterestant;

    @NotBlank
    @Size(max = 25)
    private String photo;
    private Boolean disponibilite;

    private LocalDate datepublication;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "varietes_id")
    private Varietes varietes;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Agriculteurs proprietaire;
}
