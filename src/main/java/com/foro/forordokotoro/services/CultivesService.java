package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface CultivesService {
    ResponseEntity<?> ajouterCultive(Cultive cultive);
    ResponseEntity<?> modifierCultive(Cultive cultive, Long id);
    List<Cultive> recupererCultiveDunchamp(Long idChamp);
    List<Cultive> recupererTousLesCultiveActive();
    Cultive recupererCultiveDunChampEnfonctionDateDebut(LocalDate datedebut, Long champid);

    Cultive recupererCultiveParReference(String reference);

    Cultive recupererParId(Long id);
}
