package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Enumerations.EtypeParserelle;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.Utils.request.ParserelleVevant;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.services.ParserelleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/parserelle")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class ParserelleControllleur {

    @Autowired
    ParserelleService parserelleService;

    @Autowired
    ParserelleRepository parserelleRepository;

    @PostMapping("/ajouterparserelle/{champid}")
    public ResponseEntity<?> ajouterParserelle(@Valid  @RequestParam(value = "parserelleReçu") String parserelleReçu, @PathVariable Long champid) throws IOException {


        ParserelleVevant parsvnt = new JsonMapper().readValue(parserelleReçu, ParserelleVevant.class);

        Parserelle parserelle = new Parserelle(parsvnt.getNom(), parsvnt.getLongueur(), parsvnt.getLargeur());

        String type = "champs";

        //Parserelle parserelle = new JsonMapper().readValue(parserelleReçu, Parserelle.class);
        if(parsvnt.getEtypeparserelle().equals("graine")){
            parserelle.setEtypeparserelle(EtypeParserelle.GRAINE);
        }else if(parsvnt.getEtypeparserelle().equals("semence")){
            parserelle.setEtypeparserelle(EtypeParserelle.SEMENCE);
        } else if(parsvnt.getEtypeparserelle().equals("tout")){
                parserelle.setEtypeparserelle(EtypeParserelle.TOUT);
        } else {
            return  ResponseEntity.ok(new Reponse("Veuillez choisir un type de parserelle", 0));
        }

        return parserelleService.ajouter(parserelle, champid);
    }

    @PatchMapping("/modifierparserelle/{idparserelle}")
    ResponseEntity<?> modifierParserelle(@Valid  @RequestParam(value = "parserelleReçu") String parserelleReçu, @PathVariable Long idparserelle) throws JsonProcessingException {

        ParserelleVevant parserellev = new JsonMapper().readValue(parserelleReçu, ParserelleVevant.class);

        Parserelle parserelle = new Parserelle(parserellev.getNom(), parserellev.getLongueur(), parserellev.getLargeur());

        if(parserellev.getStatus() != null){
            if(parserellev.getStatus().equals("occupe")){
                parserelle.setStatus(EstatusParserelle.OCCUPE);
            }else if(parserellev.getStatus().equals("libre")){
                parserelle.setStatus(EstatusParserelle.LIBRE);
            }
        }

         if(parserellev.getEtypeparserelle() != null){
             if(parserellev.getEtypeparserelle().equals("Graine")){
                 parserelle.setEtypeparserelle(EtypeParserelle.GRAINE);
             }else if(parserellev.getEtypeparserelle().equals("Semence")){
                 parserelle.setEtypeparserelle(EtypeParserelle.SEMENCE);
             }else if(parserellev.getEtypeparserelle().equals("Tout")){
                 parserelle.setEtypeparserelle(EtypeParserelle.TOUT);
             }
         }


        return parserelleService.modifier(parserelle, idparserelle);
    }

    @GetMapping("/recupererlesparserelledunchamp/{idchamp}")
    List<Parserelle> recupererLesParserelledunChamp(@PathVariable Long idchamp) throws JsonProcessingException {

        return parserelleService.recupererLesParserelleDunChamp(idchamp);
    }

    @GetMapping("recupererparserelleparsonid/{idparserelle}")
    Parserelle recupererParserelleParSonId(@PathVariable Long idparserelle){
        return parserelleRepository.findById(idparserelle).get();
    }
}
