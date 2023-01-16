package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.PrevisionMeteo;
import com.foro.forordokotoro.payload.request.Meteo;
import com.foro.forordokotoro.services.PrevisionMeteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prevision")
public class PrevisionMeteoControlleur {

    @Autowired
    PrevisionMeteoService previsionMeteoService;

    @PostMapping("ajouterprevision")
    PrevisionMeteo ajouterprevision(@Valid @RequestParam(value = "previsionMeteoReçu") String previsionMeteoReçu) throws JsonProcessingException {
        PrevisionMeteo previsionMeteo = new JsonMapper().readValue(previsionMeteoReçu, PrevisionMeteo.class);

        return previsionMeteoService.ajouter(previsionMeteo);
    }

    @GetMapping("recupererPrevisionsChampEnfonctionDunJour")
    List<PrevisionMeteo> recupererPrevisionsChampEnfonctionDunJours(@Valid @RequestParam(value = "meteoReçu") String meteoReçu) throws JsonProcessingException {

        Meteo meteo = new JsonMapper().readValue(meteoReçu, Meteo.class);

        return previsionMeteoService.recupererPrevisionsChampEnfonctionDunJour(meteo.getChampId(), meteo.getJour());
    }

    @GetMapping("recupererprevisionschampenfonctiondunjouretduneHeure")
    PrevisionMeteo recupererPrevisionsChampEnfonctionDunJourEtDuneHeure(@Valid @RequestParam(value = "meteoReçu") String meteoReçu) throws JsonProcessingException {

        Meteo meteo = new JsonMapper().readValue(meteoReçu, Meteo.class);

        return previsionMeteoService.recupererPrevisionsChampEnfonctionDunJourEtDuneHeure(meteo.getChampId(), meteo.getJour(), meteo.getHeure());
    }
}
