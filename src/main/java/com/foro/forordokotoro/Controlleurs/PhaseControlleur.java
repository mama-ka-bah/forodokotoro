package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.Repository.CultiveRepository;
import com.foro.forordokotoro.Repository.PhaseCultiveRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/phases")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class PhaseControlleur {

    @Autowired
    PhaseService phaseService;

    @Autowired
    CultivesService cultivesService;

    @Autowired
    PhaseCultiveRepository phaseCultiveRepository;

    @Autowired
    CultiveRepository cultiveRepository;

    @PostMapping("/ajouter/{idcultive}")
    public ResponseEntity<?> ajouterPhase(@Valid @RequestParam(value = "file") MultipartFile file,
                                          @Valid  @RequestParam(value = "phaseReçu") String phaseReçu, @RequestParam(value = "datedebut") String datedebut,
                                          @RequestParam(value = "datefin") String datefin, @PathVariable Long idcultive) throws IOException {

        String type = "champs";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDebut = LocalDate.parse(datedebut, formatter);
        LocalDate dateFin = LocalDate.parse(datefin, formatter);

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        PhaseCultive phase = new JsonMapper().readValue(phaseReçu, PhaseCultive.class);

        phase.setDatedebut(dateDebut);
        phase.setDatefin(dateFin);
        phase.setEtat(true);

        phase.setCultive(cultivesService.recupererParId(idcultive));

        if(phase.getDatedebut().isAfter(LocalDate.now()) || phase.getDatefin().isAfter(LocalDate.now())){
            return ResponseEntity.ok(new Reponse("Veuilez attendre la fin de la phase puis renseigner", 0));
        }else {
            return phaseService.ajouterPhase(phase, type, nomfile, file);
        }
    }

    @PatchMapping("/modifer/{idphase}")
    public ResponseEntity<?> modifierPhase(@RequestBody PhaseCultive phase, @PathVariable Long idphase){
        System.out.println("id id id id id id id : " + idphase);
        return phaseService.modifierPhase(idphase, phase);
    }

    @GetMapping("/recupererPhaseActives")
    public List<PhaseCultive> recupererPhaseActive(){

        return phaseService.recupererPhaseActive();
    }

    @GetMapping("/recupererphaseactivesdunecultive/{idCultive}")
    public List<PhaseCultive> recupererPhaseActivesDuneCultive(@PathVariable Long idCultive){

        Cultive cultive = cultiveRepository.findById(idCultive).get();

        if( cultive != null){
            return  phaseCultiveRepository.findByCultiveAndEtatOrderByDatefinDesc(cultive, true);
        }else {
            return null;
        }
    }

    @GetMapping("/detailPhase/{id}")
    public PhaseCultive recupererPhaseDetail(@PathVariable Long id){
        return  phaseService.recupererParId(id);
    }
}
