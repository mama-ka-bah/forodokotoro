package com.foro.forordokotoro.Models;

import com.foro.forordokotoro.Models.Enumerations.ERole;
import com.foro.forordokotoro.Models.Enumerations.EtypePublication;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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

    private Long nombreaime;

    private Long nombrenonaime;
    private Long nombrecommentaire;

    @Size(max = 60)
    private String media;

    @NotBlank
    @Size(max = 255)
    private String description;
    private Boolean etat;

    private String lien;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtypePublication typepub;

    @ManyToOne
    @JoinColumn(name = "posteur_id")
    private Agriculteurs posteur;

    public Publications(String titre, String soustitre, String description, LocalDateTime now, boolean b, Agriculteurs posteur) {
        this.titre = titre;
        this.soustitre = soustitre;
        this.description = description;
        this.datepub = now;
        this.posteur = posteur;
        this.etat = b;
    }
}
