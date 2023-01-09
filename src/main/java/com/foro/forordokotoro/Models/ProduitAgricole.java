package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class ProduitAgricole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nom;

    @NotBlank
    @Size(max = 25)
    private String photo;

    @NotBlank
    @Size(max = 255)
    private  String description;
    private Boolean etat;
    private Boolean statusubvention;
}
