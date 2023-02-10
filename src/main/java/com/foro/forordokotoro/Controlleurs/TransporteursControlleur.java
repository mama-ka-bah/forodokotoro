package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Transporteurs;
import com.foro.forordokotoro.Repository.TransporteurRepository;
import com.foro.forordokotoro.Utils.request.DemandeTransporteur;
import com.foro.forordokotoro.services.TransporteursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/transporteurs")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class TransporteursControlleur {

    @Autowired
    TransporteursService transporteursService;

    @Autowired
    TransporteurRepository transporteurRepository;

    @PostMapping("/devenirtransporteur/{id}")
    public ResponseEntity<?> devenirTransporteur(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                                                 @Valid  @RequestParam(value = "donneesTransporteur") String donneesTransporteur, @PathVariable Long id) throws IOException {

        System.out.println("nnnnnnnnnnnnnnnnnnnnnn" + donneesTransporteur);
        //chemin de stockage des images
        String type = "permis";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        //envoie le nom, url et le fichier à la classe ConfigImages qui se chargera de sauvegarder l'image
        //ConfigImages.saveimg(url, nomfile, file);

        //converssion du string reçu en classe Transporteur
        DemandeTransporteur demandeurProfil1 = new JsonMapper().readValue(donneesTransporteur, DemandeTransporteur.class);
        TransporteurAttente demandeurProfil = new TransporteurAttente();

        demandeurProfil.setNumeroplaque(demandeurProfil1.getNumeroplaque());
        demandeurProfil.setDisponibilite(demandeurProfil1.getDisponibilite());
        demandeurProfil.setPhotopermis(nomfile);
        demandeurProfil.setDatedemande(LocalDate.now());
        demandeurProfil.setStatusdemande(EstatusDemande.ENCOURS);

        return transporteursService.devenirTransporteur(id, demandeurProfil, type, nomfile, file);
    }

    @PostMapping("/accepteratransporteur/{username}")
    public ResponseEntity<?> accepterAgriculteur(@PathVariable String username){

        return transporteursService.accepterTransporteur(username);
    }

    @PostMapping("/rejetertransporteur/{username}")
    public ResponseEntity<?> rejeterTransporetur(@PathVariable String username){

        return transporteursService.rejeterTransporteur(username);
    }


    @PatchMapping("/modifiertransporteur/{id}")
    public ResponseEntity<?> updateTransporteur(@PathVariable Long id, @RequestBody Transporteurs transporteurs) {

        return transporteursService.modifierTransporteur(id, transporteurs);
    }

    @GetMapping("/RecupererTousTransporteur")
    public List<Transporteurs> recupererTransporteurDetail(){
        return  transporteurRepository.findByEtat(true);
    }

    @GetMapping("/detailTransprteur/{id}")
    public Transporteurs recupererTousLesTransporteur(@PathVariable Long id){
        return  transporteursService.recupererTransporteurParId(id);
    }

}
