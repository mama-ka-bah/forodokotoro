package com.foro.forordokotoro.Utils.request;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
public class ParserelleVevant {
    @Size(max = 120)
    private String nom;

    @Size(max = 10)
    private Double longueur;

    @Size(max = 10)
    private  Double largeur;

    @Column(length = 20)
    private String etypeparserelle;
}
