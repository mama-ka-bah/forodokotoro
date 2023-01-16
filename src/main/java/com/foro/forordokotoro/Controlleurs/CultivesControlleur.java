package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Repository.ParserelleRepository;
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

    @Autowired
    ParserelleRepository parserelleRepository;

    @PostMapping("/ajouter/{varieteid}/{parserelleid}")
    public ResponseEntity<?> ajouterCultive(@RequestBody Cultive cultive, @PathVariable Long varieteid, @PathVariable Long parserelleid){
        cultive.setParserelle(parserelleRepository.findById(parserelleid).get());
        cultive.setVarietes(varietesServices.recupererVarieteParId(varieteid));
        return cultivesService.ajouterCultive(cultive);
    }

    @PutMapping("/modifier/{idcultive}")
    public ResponseEntity<?> modifierCultive(@RequestBody Cultive cultive, @PathVariable Long idcultive){
        return cultivesService.modifierCultive(cultive, idcultive);
    }

    @GetMapping("/cultivedunchamp/{idchamp}")
    public List<Cultive> recupererCultiveChamp(@PathVariable Long idchamp){
        return cultivesService.recupererCultiveDunchamp(idchamp);
    }

    @GetMapping("/tousLesCultiveActive")
    public List<Cultive> modifierCultive(){
        return cultivesService.recupererTousLesCultiveActive();
    }

    @GetMapping("/cultiveDunchampparDateDebut/{localdate}/{champid}")
     Cultive recupererCultiveDunChampParDateDebut(@PathVariable LocalDate dateDebut, @PathVariable Long champid){
        return cultivesService.recupererCultiveDunChampEnfonctionDateDebut(dateDebut, champid);
    }

    @GetMapping("/cultiveparReference/{reference}")
    public Cultive recupererCultiveParReference(@PathVariable String reference){
        return cultivesService.recupererCultiveParReference(reference);
    }

    @GetMapping("/detailCultive/{id}")
    public Cultive recupererCultiveDetail(@PathVariable Long id){
        return  cultivesService.recupererParId(id);
    }

}
