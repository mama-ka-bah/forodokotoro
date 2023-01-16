package com.foro.forordokotoro.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class PrevisionDunCultive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private LocalDate dateprevisionnelle;
    private Long nbrepluieNec;
    private Long recoltePrevue;

    @ManyToOne
    @JoinColumn(name = "cultive_id")
    private Cultive cultive;

}
