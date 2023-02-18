package com.foro.forordokotoro.Utils.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Meteo {
    private Long ChampId;
    private LocalDate jour;
    private LocalTime heure;
}
