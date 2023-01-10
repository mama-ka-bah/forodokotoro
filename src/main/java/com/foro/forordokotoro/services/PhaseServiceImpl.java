package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.Repository.PhaseCultiveRepository;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService{

    @Autowired
    PhaseCultiveRepository phaseCultiveRepository;

    @Override
    public ResponseEntity<?> ajouterPhase(PhaseCultive phaseCultive) {
        if(phaseCultiveRepository.existsByLibelle(phaseCultive.getLibelle())){
            return ResponseEntity.ok(new Reponse(phaseCultive.getLibelle() + " existe déjà", 0));
        }else {
            phaseCultiveRepository.save(phaseCultive);
            return ResponseEntity.ok(new Reponse(phaseCultive.getLibelle() + " a été ajouter avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierPhase(Long id, PhaseCultive phaseCultive) {
        return phaseCultiveRepository.findById(id)
                .map(pc-> {
                    if(phaseCultive.getLibelle() != null)
                        pc.setLibelle(phaseCultive.getLibelle());
                    if(phaseCultive.getCultive() != null)
                        pc.setDatedebut(phaseCultive.getDatedebut());
                    if(phaseCultive.getDatefin() != null)
                        pc.setNbrepluies(phaseCultive.getNbrepluies());
                    if(phaseCultive.getRemarques() != null)
                        pc.setAction(phaseCultive.getAction());
                    phaseCultiveRepository.save(pc);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public List<PhaseCultive> recupererPhaseActive() {
        return phaseCultiveRepository.findByEtat(true);
    }

    @Override
    public PhaseCultive recupererParId(Long id) {
        return phaseCultiveRepository.findById(id).get();
    }
}
