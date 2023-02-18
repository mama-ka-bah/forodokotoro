package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.Enumerations.EstatusCultive;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Repository.CultiveRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.services.ChampServices;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.VarietesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("cultive")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class CultivesControlleur {

    @Autowired
    CultivesService cultivesService;

    @Autowired
    ChampServices champServices;

    @Autowired
    VarietesServices varietesServices;

    @Autowired
    ParserelleRepository parserelleRepository;

    @Autowired
    CultiveRepository cultiveRepository;

    @PostMapping("/ajouter/{varieteid}/{parserelleid}")
    public ResponseEntity<?> ajouterCultive(@RequestBody Cultive cultive, @PathVariable Long varieteid, @PathVariable Long parserelleid){
        cultive.setParserelle(parserelleRepository.findById(parserelleid).get());
        cultive.setVarietes(varietesServices.recupererVarieteParId(varieteid));
        if(cultive.getDatedebutsemis().isAfter(cultive.getDatefinsemis())){
            return ResponseEntity.ok(new Reponse("La date de debut ne peut pas etre superieur à la date de fin", 1));
        }else {
            return cultivesService.ajouterCultive(cultive);
        }

    }

    @PatchMapping("/modifier/{idcultive}")
    public ResponseEntity<?> modifierCultive(@RequestBody Cultive cultive, @PathVariable Long idcultive){
        return cultivesService.modifierCultive(cultive, idcultive);
    }

    @DeleteMapping("signalercultivecommenonterminer/{idCultive}")
    public ResponseEntity<?> signalerCultiveCommeNonTerminer(@PathVariable Long idCultive){
        Cultive  cultive = new Cultive();
        cultive.setStatus(EstatusCultive.ENCOURS);
        cultive.setDatefinCultive(null);

            if(cultiveRepository.findById(idCultive).get().getParserelle().getStatus() == EstatusParserelle.OCCUPE){
                return ResponseEntity.ok(new Reponse("Cette parserelle est occupée veuillez d'abord liberer", 0));
            }

        return cultivesService.modifierCultive(cultive, idCultive);
    }

    @GetMapping("/cultivedunchamp/{idchamp}")
    public List<Cultive> recupererCultiveChamp(@PathVariable Long idchamp){
        return cultivesService.recupererCultiveDunchamp(idchamp);
    }

    @GetMapping("/tousLesCultiveActive")
    public List<Cultive> recupererLesCultiveActive(){
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

    @GetMapping("/recupererlescultivesactiveduneparserelleordonnepardatedefin/{idparserelle}")
    public List<Cultive> recupererLesCultivesActiveDuneParserelleOrdonneParDateDefin(@PathVariable Parserelle idparserelle){

        return  cultiveRepository.findByParserelleAndEtatOrderByDatedebutsemisDesc(idparserelle, true);
    }

    @PatchMapping("/mettrefinauncultive/{idCultive}/{quatiteRecolte}")
    public ResponseEntity<?> mettreFinAUnCultive(@Param(value = "datefin") String datefin, @PathVariable Long idCultive, @PathVariable Double quatiteRecolte){
        Cultive cultive = new Cultive();

        //DateTimeFormatter permet de formatter la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //on lui passe notre date de fin recuperer en tant que string por qu'il le convertit en localdate sous la  forme "yyyy-MM-dd"
        LocalDate datefinCultive = LocalDate.parse(datefin, formatter);

        cultive.setDatefinCultive(datefinCultive);
        cultive.setRecolterealise(quatiteRecolte);

        Cultive cultive1 = cultiveRepository.findById(idCultive).get();

        if(datefinCultive.isAfter(LocalDate.now())){
            return ResponseEntity.ok(new Reponse("Imposible de signaler la fin de la culture avant de terminer", 0));
        } else if (cultive1.getDatedebutsemis().isAfter(datefinCultive) || cultive1.getDatefinsemis().isAfter(datefinCultive)) {
            return ResponseEntity.ok(new Reponse("La date de fin ne peut pas etre dans l'intervalle de semis", 0));
        }

        return cultivesService.mettreFincultive(idCultive, cultive);
    }


}
