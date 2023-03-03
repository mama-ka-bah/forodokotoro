package com.foro.forordokotoro.Utils.response;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class TransEnAttenteRetour {

    private Long id;
    private String nomcomplet;
    private String adresse;
    private String username;
    private String photo;
    private String photopermis;

    @Enumerated(EnumType.STRING)
    private EstatusDemande status;
}
