package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Enumerations.EtypeParserelle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Parserelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    private Double longueur;

    private  Double largeur;

    private Boolean etat;

    private LocalDateTime datecreation;

    //le status de la parserelle occupée ou libre
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusParserelle status;

    private Long nombrecultive;

    //le type de la parserelle SEMENCE OU GRAINE
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtypeParserelle etypeparserelle;

    //Le champ au quelle la parserelle est liée
    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;

    public Parserelle(String nom, Double longueur, Double largeur) {
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
    }
}
