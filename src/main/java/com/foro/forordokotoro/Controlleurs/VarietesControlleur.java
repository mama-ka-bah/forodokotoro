package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Varietes;
import com.foro.forordokotoro.services.ProduitAgricoleService;
import com.foro.forordokotoro.services.VarietesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/varietes")
public class VarietesControlleur {

    @Autowired
    VarietesServices varietesServices;

    @Autowired
    ProduitAgricoleService produitAgricoleService;

    @PostMapping("/ajouter/{idproduit}")
    public ResponseEntity<?> ajouterVarietes(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                                             @Valid  @RequestParam(value = "varieteReçue") String varieteReçue, @PathVariable Long idproduit) throws IOException {
        //chemin de stockage des images
        String url = "C:/Users/KEITA Mahamadou/Desktop/keita/project/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());

        Varietes varietes = new JsonMapper().readValue(varieteReçue, Varietes.class);
        varietes.setProduitagricole(produitAgricoleService.recupererProduitAgricoleParId(idproduit));

        return varietesServices.ajouterVarietes(varietes,url,nomfile, file);
    }

    @GetMapping("/recuperervarietesactives")
    public List<Varietes> recupererVarietesActives(){
        return varietesServices.recupererVarietesActives();
    }

    @GetMapping("/recuperervarietesparproduit/{idvariete}")
    public List<Varietes> recupererVarietesParProduit(@PathVariable Long idvariete){
        return varietesServices.recupererVarietesParProduitAgricole(produitAgricoleService.recupererProduitAgricoleParId(idvariete));
    }

    @GetMapping("/modifier/{idvariete}")
    public ResponseEntity<?> modifierVarietes(@RequestBody Varietes varietes, @PathVariable Long idvariete) {

        return varietesServices.modifiervarietes(idvariete, varietes);
    }
}
