package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.CultiveRepository;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class CultiveServiceImpl implements CultivesService{

    @Autowired
    CultiveRepository cultiveRepository;
    @Autowired
    private ChampsRepository champsRepository;

    @Override
    public ResponseEntity<?> ajouterCultive(Cultive cultive) {
        LocalDate datejour = LocalDate.now();
        String reference = cultive.getChamp().getNom() + "-" + cultive.getDatedebutsemis().getMonth() + "-" + cultive.getDatefinsemis() + "-" + datejour.getYear();
        cultive.setReference(reference);
        if(cultiveRepository.existsByReference(reference)){
            return ResponseEntity.ok(new Reponse("Vous avez déjà semé à cette date", 0));
        }else {
            cultive.setEtat(true);
            cultiveRepository.save(cultive);
            return ResponseEntity.ok(new Reponse("Votre cultive a été créé avec le reference: "+cultive.getReference(), 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierCultive(Cultive cultive, Long id) {
        return cultiveRepository.findById(id)
                .map(c-> {
                    if(cultive.getQuantiteseme() != null)
                        c.setQuantiteseme(c.getQuantiteseme());
                    if(cultive.getDatearrive() != null)
                        c.setDatearrive(cultive.getDatearrive());
                    if(cultive.getDatedebutsemis() != null)
                        c.setDatedebutsemis(cultive.getDatedebutsemis());
                    if(cultive.getDatefinsemis() != null)
                        c.setDatedebutsemis(cultive.getDatedebutsemis());
                    if(cultive.getEtat() != null)
                        c.setEtat(cultive.getEtat());
                    if(cultive.getRecoleprevue() != null)
                        c.setRecoleprevue(cultive.getRecoleprevue());
                    if(cultive.getRecolterealise() != null)
                        c.setRecolterealise(cultive.getRecolterealise());
                    if(cultive.getVarietes() != null)
                        c.setChamp(cultive.getChamp());
                    cultiveRepository.save(c);
                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);
    }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public List<Cultive> recupererCultiveDunchamp(Long idChamp) {
        return cultiveRepository.findByChamp(champsRepository.findById(idChamp).get());
    }

    @Override
    public List<Cultive> recupererTousLesCultiveActive() {
        return cultiveRepository.findByEtat(true);
    }

    @Override
    public Cultive recupererCultiveDunChampEnfonctionDateDebut(LocalDate datedebut, Long champid) {
        return cultiveRepository.findByDatedebutsemisAndChamp(datedebut, champsRepository.findById(champid).get());
    }

    @Override
    public Cultive recupererCultiveParReference(String reference) {
        return cultiveRepository.findByReference(reference);
    }

    @Override
    public Cultive recupererParId(Long id) {
        return cultiveRepository.findById(id).get();
    }
}
