package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EtypePublication;
import com.foro.forordokotoro.Repository.AgriculteursRepository;
import com.foro.forordokotoro.Repository.CommentaireRepository;
import com.foro.forordokotoro.Repository.PublicationsRepositroy;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.request.PublicationReçu;
import com.foro.forordokotoro.security.services.UtilisateurService;
import com.foro.forordokotoro.services.PublicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/publications")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class PublicationsControlleur {

    @Autowired
    PublicationsService publicationsService;

    @Autowired
    PublicationsRepositroy publicationsRepositroy;

    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    AgriculteursRepository agriculteursRepository;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    CommentaireRepository commentaireRepository;

    @PostMapping("/ajouter/{iduser}")
    public ResponseEntity<?> ajouterPublication(@Valid @RequestParam(value = "file") MultipartFile file,
                                                    @Valid  @RequestParam(value = "pubReçu") String pubReçu, @PathVariable Long iduser) throws IOException {

        //chemin de stockage des images
        String type = "publications";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());

        PublicationReçu publicationReçu = new JsonMapper().readValue(pubReçu, PublicationReçu.class);

        Agriculteurs posteur = agriculteursRepository.findById(iduser).get();

        Publications publications = new Publications(publicationReçu.getTitre(), publicationReçu.getSoustitre(), publicationReçu.getDescription(), LocalDateTime.now(), true, posteur);

        if(publicationReçu.getTypepub().equals("probleme")){
            publications.setTypepub(EtypePublication.PROBLEME);
        }else {
            publications.setTypepub(EtypePublication.CONSEIL);
        }

        return publicationsService.ajouter(publications, type, nomfile, file);
    }


    @PostMapping("ajoutercomentaire/{idPosteur}/{idPub}")
    ResponseEntity<?> ajouterComentaire(@RequestBody Commentaires commentaires, @PathVariable Long idPosteur, @PathVariable Long idPub){

        try{
            Agriculteurs posteur = agriculteursRepository.findById(idPosteur).get();
            Publications publications = publicationsRepositroy.findById(idPub).get();
            commentaires.setPublications(publications);
            commentaires.setPosteur(posteur);
            commentaires.setDatepub(LocalDateTime.now());

            return ResponseEntity.ok(commentaireRepository.save(commentaires));
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }
    }

    @GetMapping("recuperertouspublications")
    ResponseEntity<?> recupererTousPublications(){
        try{
            return ResponseEntity.ok(publicationsRepositroy.findAllByOrderByDatepubDesc());
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }
    }


    @GetMapping("recuperertouscommentairedunepublication/{idPub}")
    ResponseEntity<?> recuperertousCommentaireDunePublicatio(@PathVariable Long idPub){
        try{
            Publications publications = publicationsRepositroy.findById(idPub).get();
            return ResponseEntity.ok(commentaireRepository.findByPublicationsOrderByDatepubDesc(publications));
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }
    }

}
