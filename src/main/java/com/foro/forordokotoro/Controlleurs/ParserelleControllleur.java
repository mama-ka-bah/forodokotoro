package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.services.ParserelleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/parserelle")
public class ParserelleControllleur {

    @Autowired
    ParserelleService parserelleService;

    @PostMapping("/ajouterparserelle/{champid}")
    public ResponseEntity<?> ajouterParserelle(@Valid  @RequestParam(value = "parserelleReçu") String parserelleReçu, @PathVariable Long champid) throws IOException {

        String type = "champs";

        Parserelle parserelle = new JsonMapper().readValue(parserelleReçu, Parserelle.class);

        return parserelleService.ajouter(parserelle, champid);
    }

    @PatchMapping("/modifierparserelle/{idparserelle}")
    ResponseEntity<?> modifierParserelle(@Valid  @RequestParam(value = "parserelleReçu") String parserelleReçu, @PathVariable Long idparserelle) throws JsonProcessingException {

        Parserelle parserelle = new JsonMapper().readValue(parserelleReçu, Parserelle.class);

        return parserelleService.modifier(parserelle, idparserelle);
    }

    @GetMapping("/recupererlesparserelledunchamp/{idparserelle}")
    List<Parserelle> recupererLesParserelledunChamp(@PathVariable Long idparserelle) throws JsonProcessingException {

        return parserelleService.recupererLesParserelleDunChamp(idparserelle);
    }

}
