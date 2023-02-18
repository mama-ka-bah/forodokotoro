package com.foro.forordokotoro.Controlleurs;


import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Utils.request.MonObjet;
import com.foro.forordokotoro.services.AgriculteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/agriculteur")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class AgriculteurControlleur {

    @Autowired
    AgriculteurService agriculteurService;

    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouter(@RequestBody Agriculteurs agriculteurs){
        return agriculteurService.AjouterAgriculteur(agriculteurs);
    }

    @PostMapping("/deveniragriculteur")
    public ResponseEntity<?> devenirAgriculteur(@Valid @RequestParam(value = "id", required = true) String id) throws IOException {

        //@Valid  @RequestParam(value = "donneesuser") String donneesuser

        //chemin de stockage des images
        //String url = "C:/Users/KEITA Mahamadou/Desktop/keita/project/images";

        //recupere le nom de l'image
       // String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
       // System.out.println(nomfile);

        //envoie le nom, url et le fichier à la classe ConfigImages qui se chargera de sauvegarder l'image
        //ConfigImages.saveimg(url, nomfile, file);

        //converssion du string reçu en classe SignupRequest
        MonObjet objet = new JsonMapper().readValue(id, MonObjet.class);

        //Agriculteurs demandeurProfil = new Agriculteurs();
        AgricuteurAttente demandeurProfil = new AgricuteurAttente();
        //demandeurProfil.setPhotocarteidentite(nomfile);
        demandeurProfil.setStatusdemande(EstatusDemande.ENCOURS);
        demandeurProfil.setDatedemande(LocalDate.now());
        demandeurProfil.setDateacceptation(LocalDate.now());

        return agriculteurService.DevenirAgriculteur(objet.getId(), demandeurProfil);
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

    @GetMapping("/detailagriculteur/{id}")
    public Agriculteurs recupereragriculteurdetail(@PathVariable Long id){
        return  agriculteurService.recupererAgriculteurPArId(id);
    }





}
