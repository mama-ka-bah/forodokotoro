package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Publications;
import com.foro.forordokotoro.Repository.PublicationsRepositroy;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PublicationsServiceImpl implements PublicationsService{

    @Autowired
    PublicationsRepositroy publicationsRepositroy;

    @Override
    public ResponseEntity<?> ajouter(Publications publications, String type, String nomfile, MultipartFile file) throws IOException {

        if(publicationsRepositroy.findByTitreOrSoustitre(publications.getTitre(),publications.getSoustitre()) == null){

            if(file != null && !file.isEmpty()){
                publications.setMedia(ConfigImages.saveimg(type, nomfile, file));
            }

            publications.setNombreaime(0L);
            publications.setNombrenonaime(0L);
            publications.setNombrecommentaire(0L);

            publicationsRepositroy.save(publications);

            return  ResponseEntity.ok(new Reponse("Ajout éffectué avec succès", 1));
        }else {
            return  ResponseEntity.ok(new Reponse("Il existe déjà un sujet concernant votre conseil", 0));
        }
    }

    @Override
    public ResponseEntity<?> modifier(Long idPub, Publications publications) {
        return publicationsRepositroy.findById(idPub)
                .map(p-> {
                    if(publications.getTitre() != null)
                        p.setTitre(publications.getTitre());
                    if(publications.getNombreaime() != null)
                        p.setNombreaime(publications.getNombreaime());
                    if(publications.getNombrenonaime() != null)
                        p.setNombrenonaime(publications.getNombrenonaime());
                    if(publications.getNombrecommentaire() != null)
                        p.setNombrenonaime(publications.getNombrecommentaire());
                    if(publications.getSoustitre() != null)
                        p.setSoustitre(publications.getSoustitre());
                    if(publications.getDescription() != null)
                       p.setDescription(publications.getDescription());

                    publicationsRepositroy.save(p);
                    return ResponseEntity.ok(new Reponse("Modification reçu", 1));
                }).orElseThrow(() -> new RuntimeException("Stock non trouvé ! "));
    }
}
