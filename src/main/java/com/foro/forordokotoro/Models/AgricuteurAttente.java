package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class AgricuteurAttente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photocarteidentite;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstatusDemande statusdemande;

    private Date datedemande;

    private Date dateacceptation;

    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;
}
