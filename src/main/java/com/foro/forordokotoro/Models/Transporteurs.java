package com.foro.forordokotoro.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("T")
@NoArgsConstructor
@AllArgsConstructor
public class Transporteurs extends Utilisateurs{

    private Boolean disponibilite;
    private String photopermis;

    private String numeroplaque;


    public Transporteurs(String username, String email, String password, String adresse, String nomcomplet, String photo, Boolean disponibilite, String photopermis, String numeroplaque) {
        super(username, email, password, adresse, nomcomplet, photo);
        this.disponibilite = disponibilite;
        this.photopermis = photopermis;
        this.numeroplaque = numeroplaque;
    }



}
