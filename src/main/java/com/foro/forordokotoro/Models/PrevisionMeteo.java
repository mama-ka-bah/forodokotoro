package com.foro.forordokotoro.Models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class PrevisionMeteo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate jour;
    private LocalTime heure;
    private Double temperature;

   @ManyToOne
   @JoinColumn(name = "champ_id")
   private Champ champ;
}
