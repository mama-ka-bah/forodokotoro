package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class AgricuteurAttente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String photocarteidentite;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusDemande statusdemande;

    private LocalDate datedemande;

    private LocalDate dateacceptation;

    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;
}
