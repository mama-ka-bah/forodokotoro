package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class TransporteurAttente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean disponibilite;

    @NotBlank
    @Size(max = 25)
    private String photopermis;

    @NotBlank
    @Size(max = 20)
    private String numeroplaque;

    private LocalDate datedemande;

    private LocalDate dateacceptation;


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusDemande statusdemande;

    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;

}
