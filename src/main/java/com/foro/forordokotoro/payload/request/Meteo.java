package com.foro.forordokotoro.payload.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Meteo {
    Long ChampId;
    private LocalDate jour;
    private LocalTime heure;
}