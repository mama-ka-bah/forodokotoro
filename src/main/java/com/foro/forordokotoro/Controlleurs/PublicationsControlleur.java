package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EtypePublication;
import com.foro.forordokotoro.Repository.*;
import com.foro.forordokotoro.Utils.request.PublicationReçu;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.security.services.UtilisateurService;
import com.foro.forordokotoro.services.AimePublicationService;
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
    @Autowired
    AimePublicationRepository aimePublicationRepository;

    @Autowired
    AimePublicationService aimePublicationService;

    @PostMapping("/ajouter/{iduser}")
    public ResponseEntity<?> ajouterPublication(@Valid @RequestParam(value = "file", required = false) MultipartFile file,
                                                    @Valid  @RequestParam(value = "pubReçu") String pubReçu, @PathVariable Long iduser) throws IOException {
        String nomfile = "";
        //chemin de stockage des images
        String type = "publications";

        if(file != null && !file.isEmpty()){
            //recupere le nom de l'image
             nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        }


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


    @PatchMapping("modifierpublication/{idPub}")
    ResponseEntity<?> modifierPublication(@RequestBody Publications publications, @PathVariable Long idPub){
        return publicationsService.modifier(idPub, publications);
    }

    @PatchMapping("modifiercommentaire/{idCommentaire}")
    ResponseEntity<?> modifierPublication(@RequestBody Commentaires commentaires, @PathVariable Long idCommentaire){
        return publicationsService.modifierCommentaire(idCommentaire, commentaires);
    }

    @PostMapping("ajoutercomentaire/{idPosteur}/{idPub}")
    ResponseEntity<?> ajouterComentaire(@RequestBody Commentaires commentaires, @PathVariable Long idPosteur, @PathVariable Long idPub){

        try{
            Agriculteurs posteur = agriculteursRepository.findById(idPosteur).get();
            Publications publications = publicationsRepositroy.findById(idPub).get();
            commentaires.setPublications(publications);
            commentaires.setPosteur(posteur);
            commentaires.setDatepub(LocalDateTime.now());
            commentaires.setEtat(true);
            commentaires.setNombreaime(0L);
            commentaires.setNombrenonaime(0L);

            Publications publications1 = new Publications();
            publications1.setNombrecommentaire(publications.getNombrecommentaire()+1);
            publicationsService.modifier(idPub, publications1);

            commentaireRepository.save(commentaires);
            return ResponseEntity.ok(new Reponse("Commentaire ajouté avec succès", 1));
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("recuperertouspublications")
    ResponseEntity<?> recupererTousPublications(){
        try{
            return ResponseEntity.ok(publicationsRepositroy.findAllByOrderByDatepubDesc());
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }



    @GetMapping("recupererpublicationparid/{idPub}")
    ResponseEntity<?> recupererPublicationParId(@PathVariable Long idPub){
        try{
            return ResponseEntity.ok(publicationsRepositroy.findById(idPub));
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
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


    @PostMapping("/aimerunpublication/{idpub}/{iduser}")
    public ResponseEntity<?> aimerUnPublication(@PathVariable Long idpub, @PathVariable Long iduser, @RequestBody AimePublication aimePublication){
        System.err.println("je suis venu");
        Publications publications = publicationsRepositroy.findById(idpub).get();
        Utilisateurs utilisateur = utilisateursRepository.findById(iduser).get();

        AimePublication aimePublicationRetourner = aimePublicationRepository.findByPublicationsAndUtilisateur(publications, utilisateur);

        if(aimePublicationRetourner != null){
            return aimePublicationService.modifier(aimePublicationRetourner.getId(), aimePublication, publications, utilisateur);

        }else {
            return aimePublicationService.ajouter(aimePublication,utilisateur, publications);
        }
    }

}
