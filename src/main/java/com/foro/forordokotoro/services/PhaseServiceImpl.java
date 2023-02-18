package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.Repository.PhaseCultiveRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService{

    @Autowired
    PhaseCultiveRepository phaseCultiveRepository;

    @Override
    public ResponseEntity<?> ajouterPhase(PhaseCultive phaseCultive, String type, String nomfile, MultipartFile file) throws IOException {

        //on verifie si la phase n'existe pas au prealable dans la base
        if(phaseCultiveRepository.existsByLibelleAndCultive(phaseCultive.getLibelle(), phaseCultive.getCultive())){
            return ResponseEntity.ok(new Reponse(phaseCultive.getLibelle() + " existe déjà", 0));
        }else {
            /*
             * Ici on verifie que la date de debut de la phase soit comprise entre la date de debut de cultive(date debut de semis) et
             *  la date de fin de cultive
             * */
            if(phaseCultive.getDatedebut().isAfter(phaseCultive.getDatefin())){
                return ResponseEntity.ok(new Reponse("Date debut superieur à date fin", 0));
            } else {
                if(phaseCultive.getCultive().getDatefinCultive() != null){
                    System.err.println(phaseCultive.getCultive().getDatefinCultive());
                    /*
                    System.out.println("date debut de semis: "+ phaseCultive.getCultive().getDatedebutsemis());
                    System.out.println("date fin cultive: " + phaseCultive.getCultive().getDatefinCultive());

                    System.out.println("date debut phase: "+ phaseCultive.getDatedebut());
                    System.out.println("date fin phase: "+ phaseCultive.getDatefin());

                     */

                    if (!phaseCultive.getCultive().getDatefinCultive().isAfter(phaseCultive.getDatedebut()) || !phaseCultive.getCultive().getDatefinCultive().isAfter(phaseCultive.getDatefin())) {
                        return ResponseEntity.ok(new Reponse("Les dates doivent etres comprises entre " + phaseCultive.getCultive().getDatefinsemis() + " et " + phaseCultive.getCultive().getDatefinCultive(), 0));
                    } else {
                        System.out.println("je suis dans le premier");
                        phaseCultive.setPhoto(ConfigImages.saveimg(type, nomfile, file));
                        phaseCultive.setPhoto(nomfile);
                        phaseCultiveRepository.save(phaseCultive);
                        return ResponseEntity.ok(new Reponse(phaseCultive.getLibelle() + " a été ajouter avec succès", 1));
                    }
                } else {
                    if(!phaseCultive.getCultive().getDatedebutsemis().isBefore(phaseCultive.getDatedebut()) ||  !phaseCultive.getCultive().getDatedebutsemis().isBefore(phaseCultive.getDatefin())){
                        return ResponseEntity.ok(new Reponse("La date de debut doit être après " + phaseCultive.getCultive().getDatefinsemis(), 0));
                    }
                    System.out.println("je suis dans le deuxieme");
                    phaseCultive.setPhoto(ConfigImages.saveimg(type, nomfile, file));
                    phaseCultiveRepository.save(phaseCultive);
                    return ResponseEntity.ok(new Reponse(phaseCultive.getLibelle() + " a été ajouter avec succès", 1));
                }
            }

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
                        pc.setRemarques(phaseCultive.getRemarques());
                    if(phaseCultive.getRemarques() != null)
                        pc.setRemarques(phaseCultive.getRemarques());
                    if(phaseCultive.getNbrepluies() != null)
                        pc.setNbrepluies(phaseCultive.getNbrepluies());
                    phaseCultiveRepository.save(pc);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Phase non trouvé ! "));
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
