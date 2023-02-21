package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.services.AgriculteurService;
import com.foro.forordokotoro.services.ChampServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/champs")
@CrossOrigin("*")
public class ChampsControlleur {
    @Autowired
    ChampServices champServices;

    @Autowired
    AgriculteurService agriculteurService;

    @Autowired
    ChampsRepository champsRepository;
    @Autowired
    private ParserelleRepository parserelleRepository;

    @PostMapping("/ajouter/{idproprietaire}")
    public ResponseEntity<?> ajouterProduit(@Valid @RequestParam(value = "file") MultipartFile file,
                                            @Valid  @RequestParam(value = "champReçu") String champReçu,
                                            @PathVariable Long idproprietaire) throws IOException {
        //chemin de stockage des images
        String type = "champs";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        Champ champ = new JsonMapper().readValue(champReçu, Champ.class);
        champ.setProprietaire(agriculteurService.recupererAgriculteurPArId(idproprietaire));
        champ.setDatecreation(LocalDateTime.now());

        return champServices.ajouterChamp(champ, type, nomfile, file);
    }

    @GetMapping("/recupererchampactives")
    List<Champ> recupererCultiveChamp(){
        return champServices.recuperChampActives();
    }

    @GetMapping("/modifierchamp/{idchamp}")
    ResponseEntity<?> modifierChamp(@RequestBody Champ champ, @PathVariable Long id){
        return champServices.modifierChamp(id, champ);
    }

    @GetMapping("/detailChamp/{id}")
    public Champ recupererChampDetail(@PathVariable Long id){
        return  champServices.recupererChampParId(id);
    }

    @GetMapping("/leschampagriculteur/{id}")
    public List<Champ> recupererChampParProprietaire(@PathVariable Long id){
        Agriculteurs agriculteurs = agriculteurService.recupererAgriculteurPArId(id);
        return  champsRepository.findByProprietaireOrderByDatecreationDesc(agriculteurs);
    }

    @GetMapping("/recupererlesparsserelle")
    public ResponseEntity<?> recupererLesParsserelle(){
        return  ResponseEntity.ok(parserelleRepository.findAll());
    }

}
