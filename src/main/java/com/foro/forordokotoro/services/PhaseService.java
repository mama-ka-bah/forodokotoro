package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PhaseCultive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface  PhaseService {
    ResponseEntity<?> ajouterPhase(PhaseCultive phaseCultive, String type, String nomfile, MultipartFile file) throws IOException;
    ResponseEntity<?> modifierPhase(Long id, PhaseCultive phaseCultive);
    List<PhaseCultive> recupererPhaseActive();

    PhaseCultive recupererParId(Long id);
}
