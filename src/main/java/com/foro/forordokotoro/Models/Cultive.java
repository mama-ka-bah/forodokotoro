package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
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

    private String reference;
    private String recoleprevue;
    private String recolterealise;
    private LocalDate datesemis;
    private Long quantiteseme;
    private Boolean etat;
    private LocalDate datearrive;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "cultive_etat",
            joinColumns = @JoinColumn(name = "cultive_id"),
            inverseJoinColumns = @JoinColumn(name = "etat_id"))
    private List<EtatCultive> etatcultives = new ArrayList<>();

}
