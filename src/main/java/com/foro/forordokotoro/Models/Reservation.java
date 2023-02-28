package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date datereservation;

    @Enumerated(EnumType.STRING)
    private EstatusDemande status;

    @ManyToOne
    @JoinColumn(name = "reserveur_id")
    private Utilisateurs reserveur;
}
