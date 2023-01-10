package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Previsions;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrevisionService {
    ResponseEntity<?> ajouterPrevisions(Previsions previsions);
    ResponseEntity<?> modifierPrevision(Long id, Previsions previsions);

    List<Previsions> recupererPrevisionActives();
    Previsions recupererPrevisionParId(Long id);
}
