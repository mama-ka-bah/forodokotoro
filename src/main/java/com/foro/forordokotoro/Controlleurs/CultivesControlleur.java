package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.services.ChampServices;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.VarietesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("cultive")
public class CultivesControlleur {

    @Autowired
    CultivesService cultivesService;

    @Autowired
    ChampServices champServices;

    @Autowired
    VarietesServices varietesServices;

    @PostMapping("/ajouter/{varieteid}/{champid}")
    ResponseEntity<?> ajouterCultive(@RequestBody Cultive cultive, @PathVariable Long varieteid, @PathVariable Long champid){
        cultive.setChamp(champServices.recupererChampParId(champid));
        cultive.setVarietes(varietesServices.recupererVarieteParId(varieteid));
        return cultivesService.ajouterCultive(cultive);
    }

    @PutMapping("/modifier/{idcultive}")
    ResponseEntity<?> modifierCultive(@RequestBody Cultive cultive, @PathVariable Long idcultive){
        return cultivesService.modifierCultive(cultive, idcultive);
    }

    @GetMapping("/cultivedunchamp/{idchamp}")
    List<Cultive> recupererCultiveChamp(@PathVariable Long idchamp){
        return cultivesService.recupererCultiveDunchamp(idchamp);
    }

    @GetMapping("/tousLesCultiveActive")
    List<Cultive> modifierCultive(){
        return cultivesService.recupererTousLesCultiveActive();
    }

    @GetMapping("/cultiveDunchampparDateDebut/{localdate}/{champid}")
     Cultive recupererCultiveDunChampParDateDebut(@PathVariable LocalDate dateDebut, @PathVariable Long champid){
        return cultivesService.recupererCultiveDunChampEnfonctionDateDebut(dateDebut, champid);
    }

    @GetMapping("/cultiveparReference/{reference}")
    Cultive recupererCultiveParReference(@PathVariable String reference){
        return cultivesService.recupererCultiveParReference(reference);
    }

}
