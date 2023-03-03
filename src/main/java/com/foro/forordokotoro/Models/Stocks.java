package com.foro.forordokotoro.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foro.forordokotoro.Models.Enumerations.EtypeParserelle;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Double  nombrekilo;

    private Double quantiterestant;

    private Long nombreaime;

    private Long nombrenonaime;

    @NotBlank
    @Size(max = 25)
    private String photo;
    private Boolean disponibilite;

    private LocalDateTime datepublication;

    @NotBlank
    @Size(max = 25)
    private String typestock;

    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "varietes_id")
    private Varietes varietes;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateurs proprietaire;

}
