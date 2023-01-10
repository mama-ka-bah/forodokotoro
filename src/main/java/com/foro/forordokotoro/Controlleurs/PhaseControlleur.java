package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/phases")
public class PhaseControlleur {

    @Autowired
    PhaseService phaseService;

    @Autowired
    CultivesService cultivesService;

    @PostMapping("/ajouter/{idcultive}")
    public ResponseEntity<?> ajouterPhase(@Valid @RequestParam(value = "file") MultipartFile file,
                                          @Valid  @RequestParam(value = "phaseReçu") String phaseReçu, @PathVariable Long idcultive) throws IOException {
        //chemin de stockage des images
        String url = "C:/Users/KEITA Mahamadou/Desktop/keita/project/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        PhaseCultive phase = new JsonMapper().readValue(phaseReçu, PhaseCultive.class);

        phase.setCultive(cultivesService.recupererParId(idcultive));
        return phaseService.ajouterPhase(phase, url, nomfile, file);
    }

    @PostMapping("/modifer/{idphase}")
    public ResponseEntity<?> modifierPhase(@RequestBody PhaseCultive phase, @PathVariable Long id){

        return phaseService.modifierPhase(id, phase);
    }

    @PostMapping("/recupererPhaseActives")
    public List<PhaseCultive> recupererPhaseActive(){

        return phaseService.recupererPhaseActive();
    }
    @GetMapping("/detailPhase/{id}")
    public PhaseCultive recupererPhaseDetail(@PathVariable Long id){
        return  phaseService.recupererParId(id);
    }
}
