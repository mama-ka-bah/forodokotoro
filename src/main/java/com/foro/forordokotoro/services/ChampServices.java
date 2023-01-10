package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChampServices {
    ResponseEntity<?> ajouterChamp(Champ champ);
    ResponseEntity<?> modifierChamp();
    List<Champ> recuperChampActives();
}
