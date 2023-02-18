package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AimePublication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean aime;

    @ManyToOne
    @JoinColumn(name = "publications_id")
    private Publications publications;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateurs utilisateur;

}
