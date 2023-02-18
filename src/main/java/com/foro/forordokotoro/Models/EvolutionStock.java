package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class EvolutionStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Double quantitededuit;

    private Double quantiteajoute;

    private Double quantiterestant;

    private String motif;

    @ManyToOne
    @JoinColumn(name = "stocks_id")
    private Stocks stocks;

}
