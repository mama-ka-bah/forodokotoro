package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String contenu;

    @NotBlank
    @Size(max = 60)
    private String titre;
    private Date dateNotification;
    private Boolean lu;


    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    private Champ champ;
}
