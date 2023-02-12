package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Commentaires {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datepub;

    private Long nombreaime;

    private Long nombrenonaime;

    @NotBlank
    @Size(max = 255)
    private String description;

    private String lien;

    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "problemes_id")
    private Publications publications;

    @ManyToOne
    @JoinColumn(name = "posteur_id")
    private Agriculteurs posteur;
}
