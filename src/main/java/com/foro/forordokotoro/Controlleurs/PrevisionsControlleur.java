package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.PrevisionDunCultive;
import com.foro.forordokotoro.Models.Previsions;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.CultiveRepository;
import com.foro.forordokotoro.Repository.PrevisionDuncultiveRepository;
import com.foro.forordokotoro.Repository.PrevisionsRepository;
import com.foro.forordokotoro.services.CultivesService;
import com.foro.forordokotoro.services.PrevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("previsions")
@CrossOrigin("*")
public class PrevisionsControlleur {
    @Autowired
    PrevisionService previsionService;

    @Autowired
    CultiveRepository cultiveRepository;

    @Autowired
    CultivesService cultivesService;

    @Autowired
    PrevisionDuncultiveRepository previsionDuncultiveRepository;

    @Autowired
    PrevisionsRepository previsionsRepository;

    @PostMapping("/ajouter/{user}")
    public ResponseEntity<?> ajouterPrevisions(@RequestBody Previsions previsions, @PathVariable Utilisateurs user){
        previsions.setCreateur(user);
        return previsionService.ajouterPrevisions(previsions);
    }
    @PatchMapping("/modifier/{idprevision}")
    public ResponseEntity<?> modifier(@RequestBody Previsions previsions, @PathVariable Long idprevision){

        return previsionService.modifierPrevision(idprevision, previsions);
    }

    @GetMapping("/detailPrevision/{id}")
    public Previsions recupererprevisionDetail(@PathVariable Long id){
        return  previsionService.recupererPrevisionParId(id);
    }

    @GetMapping("/recupererlesprevisionsduncultive/{cultiveid}")
    public List<PrevisionDunCultive> recupererlesprevisionsduncultive(@PathVariable Long cultiveid){
        Cultive cultive = cultiveRepository.findById(cultiveid).get();
        if(previsionDuncultiveRepository.existsByCultive(cultive)){
            return previsionDuncultiveRepository.findByCultive(cultive);
        }else {
            return null;
        }

    }
}
