package com.foro.forordokotoro.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foro.forordokotoro.Models.Enumerations.Eactions;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class PhaseCultive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String libelle;
    private LocalDate datedebut;
    private LocalDate datefin;


    //le nombre de fois qu'il a plevie dans laphases
    private Long nbrepluies;

    @Size(max = 120)
    private String photo;
    private Boolean etat;

    //une petite description de la phase
    @NotBlank
    //@Column(columnDefinition = "text")
    @Size(max = 150)
    private String remarques;

    /*
    @Enumerated(EnumType.STRING)
    @Column(length = 120)
    private Eactions action;
     */

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cultive_id")
    private Cultive cultive;

}
