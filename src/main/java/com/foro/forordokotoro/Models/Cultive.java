package com.foro.forordokotoro.Models;

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

    @NotBlank
    @Size(max = 15)
    private String recoleprevue;

    @Size(max = 15)
    private String recolterealise;
    private LocalDate datedebutsemis;
    private LocalDate datefinsemis;

    @Size(max = 5)
    private Long quantiteseme;
    private Boolean etat;
    private LocalDate datearrive;

    @ManyToOne
    @JoinColumn(name = "varietes_id")
    private Varietes varietes;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;

}
