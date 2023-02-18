package com.foro.forordokotoro.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "text")
    private String contenu;

    @NotBlank
    @Size(max = 60)
    private String titre;
    private Date datenotification;
    private Boolean lu;

    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;

    public Notifications(String titre, String contenu, Date date, boolean lu) {
        this.titre = titre;
        this.contenu = contenu;
        this.datenotification = date;
        this.lu = lu;
    }
}
