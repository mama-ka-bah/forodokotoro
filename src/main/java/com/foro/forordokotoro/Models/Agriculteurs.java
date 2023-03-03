package com.foro.forordokotoro.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@DiscriminatorValue("A")
@NoArgsConstructor
public class Agriculteurs extends Utilisateurs{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
    @NotBlank
    @Size(max = 120)
    private String photocarteidentite;

    public Agriculteurs(String photocarteidentite) {
        this.photocarteidentite =  photocarteidentite;
    }
}
