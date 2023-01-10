package com.foro.forordokotoro.Controlleurs;

import com.foro.forordokotoro.Models.Previsions;
import com.foro.forordokotoro.services.PrevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("previsions")
public class PrevisionsControlleur {
    @Autowired
    PrevisionService previsionService;
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterPrevisions(@RequestBody Previsions previsions){
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
}
