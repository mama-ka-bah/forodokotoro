package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
        Optional<Champ> champ = champsRepository.findById(chmpid);

        if(!parserelleRepository.existsByNomAndChamp(parserelle.getNom(), parserelle.getChamp())){
            //verifie si le champ existe
            if(champ == null){
                return ResponseEntity.ok(new Reponse("Ce champ n'existe pas", 0));
            }else {

            //recupere le champ conserné
            Champ champConserner = champsRepository.findById(chmpid).get();
            System.out.println("Longueur disponible retournée: " + champServices.verifierDisponibiliteDimension(chmpid, parserelle).get(0));

            System.out.println("Largueur disponible retournée: " + champServices.verifierDisponibiliteDimension(chmpid, parserelle).get(1));

            //verifier si la longueur demandée est disponible
            Double resultatVerificationLongeur = champServices.verifierDisponibiliteDimension(chmpid, parserelle).get(0);
            Double resultatVerificationLargeur = champServices.verifierDisponibiliteDimension(chmpid, parserelle).get(1);

            if(parserelle.getLongueur() == 0 || parserelle.getLargeur() == 0){
                return ResponseEntity.ok(new Reponse("Veuillez renseigner les dimensions", 0));
            }else if(resultatVerificationLongeur < 0){

                return ResponseEntity.ok(new Reponse("La longueur demandée n'est plus disponible", 0));
            }

            //verifier si la largeur demandée est disponible
            else if(resultatVerificationLargeur < 0){
                return ResponseEntity.ok(new Reponse("La largeur demandée n'est plus disponible", 0));
            }else {
                parserelle.setChamp(champConserner);
                parserelle.setStatus(EstatusParserelle.LIBRE);
                parserelle.setEtat(true);
                parserelle.setDatecreation(LocalDateTime.now());
                parserelle.setNombrecultive(0L);
                parserelleRepository.save(parserelle);
                champServices.incrementerNombreParserelle(parserelle.getChamp().getId());
                return ResponseEntity.ok(new Reponse(parserelle.getNom() + " a été ajouté avec succès", 1));
            }
        }
        }else {
            return ResponseEntity.ok(new Reponse("Cette Parserelle existe déjà", 0));
        }

    }

    @Override
    public ResponseEntity<?> modifier(Parserelle parserelle, Long id) {
        return parserelleRepository.findById(id)
                .map(p-> {
                    if(parserelle.getNom() != null)
                        p.setNom(parserelle.getNom());
                    if(parserelle.getEtypeparserelle() != null)
                        p.setEtypeparserelle(parserelle.getEtypeparserelle());
                    if(parserelle.getStatus() != null)
                        p.setStatus(parserelle.getStatus());
                    if(parserelle.getChamp() != null)
                        p.setChamp(parserelle.getChamp());
                    if(parserelle.getNombrecultive() != null)
                        p.setNombrecultive(parserelle.getNombrecultive());
                    if(parserelle.getLargeur() != null)
                        if(champServices.verifierDisponibiliteDimension(p.getChamp().getId(), parserelle).get(1) < 0)
                            p.setLargeur(parserelle.getLargeur());
                    else
                        return ResponseEntity.ok(new Reponse("Largeur non disponible", 0));
                    if(parserelle.getLongueur() != null)
                        if(champServices.verifierDisponibiliteDimension(p.getChamp().getId(), parserelle).get(0) < 0)
                         p.setLongueur(parserelle.getLongueur());
                    else
                        return ResponseEntity.ok(new Reponse("Longueur non disponible", 0));
                    parserelleRepository.save(p);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Parserelle non trouvé ! "));
    }


    @Override
    public List<Parserelle> recupererLesParserelleDunChamp(Long idchamp) {
        if(!champsRepository.existsById(idchamp)){
            List<Parserelle> p = new ArrayList<>();
            return p;
        }
        return parserelleRepository.findByChampAndEtatOrderByDatecreationDesc(champsRepository.findById(idchamp).get(), true);
    }
}
