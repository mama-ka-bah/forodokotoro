package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class TransporteurAttente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean disponibilite;

    private String photopermis;

    private String numeroplaque;

    private Date datedemande;

    private Date dateacceptation;


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusDemande statusdemande;

    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;

}
