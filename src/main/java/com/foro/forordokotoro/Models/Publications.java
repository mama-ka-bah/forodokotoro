package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
public class Publications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String titre;

    @NotBlank
    @Size(max = 60)
    private String soustitre;

    private LocalDateTime datepub;

    @NotBlank
    @Size(max = 25)
    private String photo;

    @NotBlank
    @Size(max = 25)
    private String video;

    @NotBlank
    @Size(max = 255)
    private String description;
    private Boolean etat;

    @ManyToOne
    @JoinColumn(name = "posteur_id")
    private Agriculteurs posteur;
}
