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

    @NotBlank
    @Size(max = 60)
    private String titre;
    private LocalDateTime datepub;

    @NotBlank
    @Size(max = 255)
    private String description;


    @Size(max = 25)
    private String photo;


    @Size(max = 25)
    private String video;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "problemes_id")
    private Publications problemes;

    @ManyToOne
    @JoinColumn(name = "posteur_id")
    private Agriculteurs posteur;
}
