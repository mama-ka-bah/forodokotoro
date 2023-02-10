package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AimeStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean aime;

    @ManyToOne
    @JoinColumn(name = "stocks_id")
    private Stocks stocks;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateurs utilisateur;
}
