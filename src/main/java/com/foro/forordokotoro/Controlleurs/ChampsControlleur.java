package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Enumerations.EstatusChamps;
import com.foro.forordokotoro.services.AgriculteurService;
import com.foro.forordokotoro.services.ChampServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/champs")
public class ChampsControlleur {

    @Autowired
    ChampServices champServices;

    @Autowired
    AgriculteurService agriculteurService;

    @PostMapping("/ajouter/{idproprietaire}")
    public ResponseEntity<?> ajouterProduit(@Valid @RequestParam(value = "file") MultipartFile file,
                                            @Valid  @RequestParam(value = "champReçu") String champReçu, @PathVariable Long idproprietaire) throws IOException {
        //chemin de stockage des images
        String url = "C:/Users/KEITA Mahamadou/Desktop/keita/project/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        Champ champ = new JsonMapper().readValue(champReçu, Champ.class);
        champ.setProprietaire(agriculteurService.recupererAgriculteurPArId(idproprietaire));

        champ.setStatus(EstatusChamps.LIBRE);

        return champServices.ajouterChamp(champ, url, nomfile, file);
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

}
