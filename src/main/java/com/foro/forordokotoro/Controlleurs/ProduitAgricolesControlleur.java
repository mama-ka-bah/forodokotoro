package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.services.ProduitAgricoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produitagricoles")
@CrossOrigin("*")
public class ProduitAgricolesControlleur {

    @Autowired
    ProduitAgricoleService produitAgricoleService;

    @PostMapping("/ajouter/{user}")
    public ResponseEntity<?> ajouterProduitAgricole(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                                                    @Valid  @RequestParam(value = "produitreçu") String produitreçu, @PathVariable Utilisateurs user) throws IOException {

        //chemin de stockage des images
        String type = "produitsAgricoles";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());

        ProduitAgricole produitAgricole = new JsonMapper().readValue(produitreçu, ProduitAgricole.class);
        produitAgricole.setCreateur(user);
        return produitAgricoleService.ajouterProduitAgricole(produitAgricole, type, nomfile, file);
    }

    @PatchMapping("/modifier/{id}")
    public ResponseEntity<?> modifierProduitAgricole(@RequestBody ProduitAgricole produitAgricole, @PathVariable Long id){

        return  produitAgricoleService.modifierProduitAgricole(produitAgricole, id);
    }

    @GetMapping("/produitagricoleactives")
    public List<ProduitAgricole> produitAgricoleActives(){
        return  produitAgricoleService.RecupererproduitAgricoleActives();
    }

    @GetMapping("/detailproduitagricole/{id}")
    public ProduitAgricole recupererDetailProduitAgricole(@PathVariable Long id){
        return  produitAgricoleService.recupererProduitAgricoleParId(id);
    }
}
