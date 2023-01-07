package com.foro.forordokotoro.Models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@DiscriminatorValue("A")
@NoArgsConstructor
public class Agriculteurs extends Utilisateurs{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    private String photocarteidentite;

    public Agriculteurs(String photocarteidentite) {
        this.photocarteidentite =  photocarteidentite;
    }
}
