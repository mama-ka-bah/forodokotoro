package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PhaseCultive;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PhaseService {
    ResponseEntity<?> ajouterPhase(PhaseCultive phaseCultive);
    ResponseEntity<?> modifierPhase(Long id, PhaseCultive phaseCultive);
    List<PhaseCultive> recupererPhaseActive();

    PhaseCultive recupererParId(Long id);
}
