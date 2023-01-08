package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;
    private String titre;
    private Date dateNotification;
    private Boolean lu;


    @ManyToOne
    @JoinColumn(name = "userid_id")
    private Utilisateurs userid;
}
