package com.foro.forordokotoro.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@DiscriminatorValue("T")
@NoArgsConstructor
@AllArgsConstructor
public class Transporteurs extends Utilisateurs{

    private Boolean disponibilite;

    private Long nombrecontact;

    @NotBlank
    @Size(max = 25)
    private String photopermis;

    @NotBlank
    @Size(max = 25)
    private String numeroplaque;


    public Transporteurs(String username, String email, String password, String adresse, String nomcomplet, String photo, Boolean disponibilite, String photopermis, String numeroplaque) {
        super(username, email, password, adresse, nomcomplet, photo);
        this.disponibilite = disponibilite;
        this.photopermis = photopermis;
        this.numeroplaque = numeroplaque;
    }



}
