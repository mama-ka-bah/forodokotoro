package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChampServicesImpl implements ChampServices{

    @Autowired
    ChampsRepository champsRepository;

    @Autowired
    ParserelleRepository parserelleRepository;

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
                    /*
                    if(champ.getEtypeChamp() != null)
                        ch.setEtypeChamp(champ.getEtypeChamp());
                     */
                    if(champ.getLargeur() != null)
                        ch.setLargeur(champ.getLargeur());
                    if(champ.getLongueur() != null)
                        ch.setLongueur(champ.getLongueur());
                    if(champ.getEtat() != null)
                        ch.setEtat(champ.getEtat());
                    if(champ.getNombreParserelle() != null)
                        ch.setNombreParserelle(champ.getNombreParserelle());
                    if(champ.getAdresse() != null)
                        ch.setAdresse(champ.getAdresse());
                    champsRepository.save(ch);

                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);

                }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }


    @Override
    //methode pemettant d'incrementer le nombre de parserelle d'un champ
    public ResponseEntity<?> incrementerNombreParserelle(Long id) {
        return champsRepository.findById(id)
                .map(ch-> {
                        ch.setNombreParserelle(ch.getNombreParserelle() + 1);
                    champsRepository.save(ch);
                    return new ResponseEntity<>("Incrementé reçu", HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("Champ nom trouvé ! "));
    }

    @Override
    public List<Champ> recuperChampActives() {
        return champsRepository.findByEtat(true);
    }

    @Override
    public Champ recupererChampParId(Long id) {
        return champsRepository.findById(id).get();
    }

    @Override
    public ArrayList<Double> verifierDisponibiliteDimension(Long champid) {
        Double longueurTotalParserelles = null;
        Double largeurTotalParserelles = null;
        ArrayList<Double> dimensions = new ArrayList<>();

        //recupere le champ conserné
        Champ champConserner = champsRepository.findById(champid).get();

        //recupere la liste des parserelles du champ
        List<Parserelle> parserelleList = parserelleRepository.findByChamp(champConserner);

        //recuperation de la longueur total et de la largeur total de toutes les pareserelles du champ conserné
        for(Parserelle p : parserelleList){
            longueurTotalParserelles += longueurTotalParserelles;
            largeurTotalParserelles += largeurTotalParserelles;
        }
        dimensions.add(0, champConserner.getLongueur() - longueurTotalParserelles);
        dimensions.add(1, champConserner.getLargeur() - largeurTotalParserelles);
        return dimensions;
    }
}
