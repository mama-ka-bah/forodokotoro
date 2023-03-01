package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date datereservation;

    @Enumerated(EnumType.STRING)
    private EstatusDemande status;

    @ManyToOne
    @JoinColumn(name = "transporteur_id")
    private Transporteurs transporteur;

    @ManyToOne
    @JoinColumn(name = "reserveur_id")
    private Utilisateurs reserveur;

    public Reservation(Utilisateurs reserveur, Date date, EstatusDemande status, Transporteurs transporteurs) {
        this.datereservation = date;
        this.reserveur = reserveur;
        this.status = status;
        this.transporteur = transporteurs;
    }
}
