package com.foro.forordokotoro.Controlleurs;


import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.EstatusDemande;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.services.AgriculteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/agriculteur")
public class AgriculteurControlleur {

    @Autowired
    AgriculteurService agriculteurService;

    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouter(@RequestBody Agriculteurs agriculteurs){
        return agriculteurService.AjouterAgriculteur(agriculteurs);
    }

    @PostMapping("/deveniragriculteur/{id}")
    public ResponseEntity<?> devenirAgriculteur(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                        @PathVariable Long id) throws IOException {

        //@Valid  @RequestParam(value = "donneesuser") String donneesuser

        //chemin de stockage des images
        String url = "C:/Users/mkkeita/Desktop/projects/medias/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        //envoie le nom, url et le fichier à la classe ConfigImages qui se chargera de sauvegarder l'image
        //ConfigImages.saveimg(url, nomfile, file);

        //converssion du string reçu en classe SignupRequest
        //Agriculteurs demandeurProfil = new JsonMapper().readValue(agriculteurs, Agriculteurs.class);

        //Agriculteurs demandeurProfil = new Agriculteurs();
        AgricuteurAttente demandeurProfil = new AgricuteurAttente();
        demandeurProfil.setPhotocarteidentite(nomfile);
        demandeurProfil.setStatusdemande(EstatusDemande.ENCOURS);
        demandeurProfil.setDatedemande(new Date());
        demandeurProfil.setDateacceptation(new Date(0));

        return agriculteurService.DevenirAgriculteur(id, demandeurProfil, url, nomfile, file);
    }


    /*
    @GetMapping("/agriculteursnomaccepter")
    public List<Agriculteurs> agriculteursNomAccepter(){

        return agriculteurService.recupererAgriculteursNonAccepter();
    }

     */

    @PostMapping("/accepteragriculteur/{username}")
    public ResponseEntity<?> accepterAgriculteur(@PathVariable String username){

        return agriculteurService.accepterAgriculteur(username);
    }

    @PostMapping("/rejeteragriculture/{username}")
    public ResponseEntity<?> rejeterAgriculteur(@PathVariable String username){

        return agriculteurService.rejeterAgriculteur(username);
    }


    @PatchMapping("/modifieragriculteur/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Agriculteurs agriculteurs) {

        return agriculteurService.modifierAgriculteur(id, agriculteurs);
    }





}
