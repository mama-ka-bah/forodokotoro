package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParserelleServiceImpl implements ParserelleService{

    @Autowired
    ParserelleRepository parserelleRepository;

    @Autowired
    ChampsRepository champsRepository;

    @Autowired
    ChampServices champServices;

    @Override
    public ResponseEntity<?> ajouter(Parserelle parserelle, Long chmpid) throws IOException {
        Optional<Champ> champ = champsRepository.findById(parserelle.getChamp().getId());

        //verifie si le champ existe
        if(champ == null){
            return ResponseEntity.ok(new Reponse("Ce champ n'existe pas", 0));
        }else {

            //recupere le champ conserné
            Champ champConserner = champsRepository.findById(parserelle.getChamp().getId()).get();

            //verifier si la longueur demandée est disponible
            if(champServices.verifierDisponibiliteDimension(chmpid).get(0) < 0){
                return ResponseEntity.ok(new Reponse("La longueur demandée n'est plus disponible", 0));
            }

            //verifier si la largeur demandée est disponible
            else if(champServices.verifierDisponibiliteDimension(chmpid).get(1) < 0){
                return ResponseEntity.ok(new Reponse("La largeur demandée n'est plus disponible", 0));
            }else {
                parserelle.setChamp(champConserner);
                parserelle.setStatus(EstatusParserelle.LIBRE);
                parserelleRepository.save(parserelle);
                champServices.incrementerNombreParserelle(parserelle.getChamp().getId());
                return ResponseEntity.ok(new Reponse(parserelle.getNom() + " a été ajouté avec succès", 0));
            }
        }

    }

    @Override
    public ResponseEntity<?> modifier(Parserelle parserelle, Long id) {
        return parserelleRepository.findById(id)
                .map(p-> {
                    if(parserelle.getNom() != null)
                        p.setNom(parserelle.getNom());
                    if(parserelle.getEtypeParserelle() != null)
                        p.setEtypeParserelle(parserelle.getEtypeParserelle());
                    if(parserelle.getStatus() != null)
                        p.setStatus(parserelle.getStatus());
                    if(parserelle.getChamp() != null)
                        p.setChamp(parserelle.getChamp());
                    if(parserelle.getLargeur() != null && champServices.verifierDisponibiliteDimension(p.getChamp().getId()).get(1) < 0)
                        p.setLargeur(parserelle.getLargeur());
                    else
                        return ResponseEntity.ok(new Reponse("Largeur non disponible", 0));
                    if(parserelle.getLongueur() != null && champServices.verifierDisponibiliteDimension(p.getChamp().getId()).get(0) < 0)
                        p.setLongueur(parserelle.getLongueur());
                    else
                        return ResponseEntity.ok(new Reponse("Longueur non disponible", 0));
                    parserelleRepository.save(p);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Parserelle non trouvé ! "));
    }


    @Override
    public List<Parserelle> recupererLesParserelleDunChamp(Long id) {
        if(!parserelleRepository.existsById(id)){
            List<Parserelle> p = new ArrayList<>();
            return p;
        }
        return parserelleRepository.findByChamp(champsRepository.findById(id).get());
    }
}
