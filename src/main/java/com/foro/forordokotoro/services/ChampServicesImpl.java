package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ChampServicesImpl implements ChampServices{

    @Autowired
    ChampsRepository champsRepository;

    @Override
    public ResponseEntity<?> ajouterChamp(Champ champ, String url, String nomfile, MultipartFile file) throws IOException {

        if(champsRepository.existsByNom(champ.getNom())){
            return ResponseEntity.ok(new Reponse(champ.getNom() + " existe déjà", 0));
        }else {
            ConfigImages.saveimg(url, nomfile, file);
            champ.setPhoto(nomfile);
            champ.setEtat(true);
            champsRepository.save(champ);
            return ResponseEntity.ok(new Reponse(champ.getNom() + " a été engistré avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierChamp(Long id, Champ champ) {
        return champsRepository.findById(id)
                .map(ch-> {
                    if(champ.getNom() != null)
                        ch.setNom(champ.getNom());
                    if(champ.getEtypeChamp() != null)
                        ch.setEtypeChamp(champ.getEtypeChamp());
                    if(champ.getLargeur() != null)
                        ch.setLargeur(champ.getLargeur());
                    if(champ.getLongueur() != null)
                        ch.setLongueur(champ.getLongueur());
                    if(champ.getEtat() != null)
                        ch.setEtat(champ.getEtat());
                    if(champ.getAdresse() != null)
                        ch.setAdresse(champ.getAdresse());
                    champsRepository.save(ch);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public List<Champ> recuperChampActives() {
        return champsRepository.findByEtat(true);
    }

    @Override
    public Champ recupererChampParId(Long id) {
        return champsRepository.findById(id).get();
    }
}
