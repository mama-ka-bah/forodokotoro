package com.foro.forordokotoro.Models;

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

    private LocalDate datedebut;
    private LocalDate datefin;
    private Long nbrepluies;

    @NotBlank
    @Size(max = 150)
    private String remarques;

    @Enumerated(EnumType.STRING)
    @Column(length = 120)
    private Eactions action;

    @ManyToOne
    @JoinColumn(name = "cultive_id")
    private Cultive cultive;

}
