package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phases")
public class PhaseControlleur {

    @Autowired
    PhaseService phaseService;

    @Autowired
    CultivesService cultivesService;

    @PostMapping("/ajouter/{idcultive}")
    public ResponseEntity<?> ajouterPhase(@RequestBody PhaseCultive phase, @PathVariable Long idcultive){
        phase.setCultive(cultivesService.recupererParId(idcultive));
        return phaseService.ajouterPhase(phase);
    }

    @PostMapping("/modifer/{idphase}")
    public ResponseEntity<?> modifierPhase(@RequestBody PhaseCultive phase, @PathVariable Long id){

        return phaseService.modifierPhase(id, phase);
    }

    @PostMapping("/recupererPhaseActives")
    public List<PhaseCultive> recupererPhaseActive(){

        return phaseService.recupererPhaseActive();
    }
}
