package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class DeductionStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Long quantitededuit;
    private Long quantiterestant;

    @ManyToOne
    @JoinColumn(name = "stocks_id")
    private Stocks stocks;

}
