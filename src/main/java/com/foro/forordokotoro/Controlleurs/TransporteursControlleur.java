package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.EstatusDemande;
import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Transporteurs;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.DemandeTransporteur;
import com.foro.forordokotoro.services.TransporteursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("/transporteurs")
public class TransporteursControlleur {

    @Autowired
    TransporteursService transporteursService;

    @PostMapping("/devenirtransporteur/{id}")
    public ResponseEntity<?> devenirTransporteur(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                                                 @Valid  @RequestParam(value = "donneesTransporteur") String donneesTransporteur, @PathVariable Long id) throws IOException {

        //chemin de stockage des images
        String url = "C:/Users/mkkeita/Desktop/projects/medias/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        //envoie le nom, url et le fichier à la classe ConfigImages qui se chargera de sauvegarder l'image
        ConfigImages.saveimg(url, nomfile, file);

        //converssion du string reçu en classe Transporteur
        DemandeTransporteur demandeurProfil1 = new JsonMapper().readValue(donneesTransporteur, DemandeTransporteur.class);
        TransporteurAttente demandeurProfil = new TransporteurAttente();

        demandeurProfil.setNumeroplaque(demandeurProfil1.getNumeroplaque());
        demandeurProfil.setPhotopermis(nomfile);
        demandeurProfil.setDatedemande(new Date());
        demandeurProfil.setStatusdemande(EstatusDemande.ENCOURS);

        return transporteursService.devenirAgriculteur(id, demandeurProfil);
    }

    @PostMapping("/accepteratransporteur/{username}")
    public ResponseEntity<?> accepterAgriculteur(@PathVariable String username){

        return transporteursService.accepterTransporteur(username);
    }

    /*
    @GetMapping("/transporteurnomaccepter")
    public List<Transporteurs> transporteurNomAccepter(){

        return transporteursService.recupererTransporteurNonAccepter();
    }

     */

    @PostMapping("/rejetertransporteur/{username}")
    public ResponseEntity<?> rejeterTransporetur(@PathVariable String username){

        return transporteursService.rejeterTransporteur(username);
    }

}
