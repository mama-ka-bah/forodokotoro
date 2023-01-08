package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Varietes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Long cycle;
    private Long taillefinal;
    private String photo;
    private  String description;
    private Boolean etat;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "varietes_previsions",
            joinColumns = @JoinColumn(name = "varietes_id"),
            inverseJoinColumns = @JoinColumn(name = "previsions_id"))
    private List<Previsions> roles = new ArrayList<>();
}
