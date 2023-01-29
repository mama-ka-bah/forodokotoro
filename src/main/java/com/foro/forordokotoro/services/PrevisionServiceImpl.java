package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Previsions;
import com.foro.forordokotoro.Repository.PhaseCultiveRepository;
import com.foro.forordokotoro.Repository.PrevisionsRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrevisionServiceImpl implements PrevisionService{

    @Autowired
    PrevisionsRepository previsionsRepository;
    @Autowired
    private PhaseCultiveRepository phaseCultiveRepository;

    @Override
    public ResponseEntity<?> ajouterPrevisions(Previsions previsions) {
        if(previsionsRepository.existsByLibelle(previsions.getLibelle())){
            return ResponseEntity.ok(new Reponse(previsions.getLibelle() + " existe déjà", 0));
        }else {
            previsions.setEtat(true);
            previsionsRepository.save(previsions);
            return ResponseEntity.ok(new Reponse(previsions.getLibelle() + " a été ajouté avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierPrevision(Long id, Previsions previsions) {
        return previsionsRepository.findById(id)
                .map(p-> {
                    if(previsions.getLibelle() != null)
                        p.setLibelle(previsions.getLibelle());
                    if(previsions.getDelaijour() != null)
                        p.setDelaijour(previsions.getDelaijour());

                    if(previsions.getNbrepluienecessaire() != null)
                        p.setNbrepluienecessaire(previsions.getNbrepluienecessaire());
                    previsionsRepository.save(p);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Prevision non trouvé ! "));
    }

    @Override
    public List<Previsions> recupererPrevisionActives() {
        return previsionsRepository.findByEtatOrderByDelaijour(true);
    }

    @Override
    public Previsions recupererPrevisionParId(Long id) {
        return previsionsRepository.findById(id).get();
    }
}