package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Enumerations.EtypeParserelle;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Parserelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String nom;

    @Size(max = 10)
    private Long longueur;

    @Size(max = 10)
    private  Long Largeur;

    @NotBlank
    @Size(max = 60)
    private String photo;

    private Boolean etat;

    //le status de la parserelle occupée ou libre
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusParserelle status;

    //le type de la parserelle SEMENCE OU GRAINE
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtypeParserelle etypeParserelle;

    //Le champ au quelle la parserelle est liée
    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;
}
