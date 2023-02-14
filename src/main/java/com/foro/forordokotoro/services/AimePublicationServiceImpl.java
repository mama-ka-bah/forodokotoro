package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Repository.AimePublicationRepository;
import com.foro.forordokotoro.Repository.PublicationsRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AimePublicationServiceImpl implements AimePublicationService{

    @Autowired
    AimePublicationRepository aimePublicationRepository;

    @Autowired
    PublicationsService publicationsService;

    @Autowired
    PublicationsRepositroy publicationsRepositroy;

    @Override
    public ResponseEntity<?> ajouter(AimePublication aimePublication, Utilisateurs utilisateurs, Publications publications) {
        int nombreAimePublication = 0;
        int nombreNonAimePublication = 0;

        aimePublication.setPublications(publications);
        aimePublication.setUtilisateur(utilisateurs);
        AimePublication aimePublicationRetouner =  aimePublicationRepository.save(aimePublication);

        Publications publications1 = new Publications();

        try{

            if(aimePublication.getAime() == true){
                nombreAimePublication = aimePublicationRepository.findByPublicationsAndAime(publications, aimePublication.getAime()).size();
                publications1.setNombreaime((long) nombreAimePublication);
                publicationsService.modifier(publications.getId(), publications1);
            }else{
                nombreNonAimePublication = aimePublicationRepository.findByPublicationsAndAime(publications, aimePublication.getAime()).size();
                publications1.setNombrenonaime((long) nombreNonAimePublication);
                publicationsService.modifier(publications.getId(), publications1);
            }
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }

        return ResponseEntity.ok(aimePublicationRetouner.getPublications());
    }

    @Override
    public ResponseEntity<?> modifier(Long id, AimePublication aimePublication, Publications publications, Utilisateurs utilisateurs) {
        return aimePublicationRepository.findById(id)
                .map(ap-> {

                    AimePublication aimePublicationsRetourner = new AimePublication();
                     Publications publicationsARetourner = ap.getPublications();

                    if(ap.getAime() != aimePublication.getAime()){
                        //aimeStockRetourner = aimeStockRepository.save(as);
                        ap.setAime(aimePublication.getAime());
                        aimePublicationRepository.save(ap);
                    }else {
                        aimePublicationRepository.deleteById(ap.getId());
                    }


                    int nombreAimePublication = 0;
                    int nombreNonAimePublication = 0;
                    Publications publications1 = new Publications();

                    try{
                        nombreAimePublication = aimePublicationRepository.findByPublicationsAndAime(publications, true).size();
                        nombreNonAimePublication = aimePublicationRepository.findByPublicationsAndAime(publications, false).size();
                        publications1.setNombrenonaime((long) nombreNonAimePublication);
                        publications1.setNombreaime((long) nombreAimePublication);

                        publicationsService.modifier(publications.getId(), publications1);
                    }catch (Exception e){
                        return ResponseEntity.ok(e);
                    }

                    return ResponseEntity.ok(publicationsRepositroy.findById(publicationsARetourner.getId()));

                }).orElseThrow(() -> new RuntimeException("Aime non trouvé ! "));
    }

    @Override
    public ResponseEntity<?> recupererListeDesJaimeDunePublication(Publications publications) {
        return null;
    }
}
