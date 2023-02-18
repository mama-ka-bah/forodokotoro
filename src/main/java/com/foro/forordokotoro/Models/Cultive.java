package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusCultive;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Cultive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 15)
    private String reference;

    //en kilo
    private Double recoleprevue;

    //en kilo
    private Double recolterealise;
    private LocalDate datedebutsemis;
    private LocalDate datefinsemis;

    private LocalDate datefinCultive;

    private LocalDate dateRecolteRéelle;

    @Size(max = 5)
    private Double quantiteseme;
    private Boolean etat;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusCultive status;

    @ManyToOne
    @JoinColumn(name = "varietes_id")
    private Varietes varietes;

    /*
    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;
     */

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Parserelle parserelle;

}
