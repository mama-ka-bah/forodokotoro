package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChampServices {
    ResponseEntity<?> ajouterChamp(Champ champ, String url, String nomfile, MultipartFile file) throws IOException;
    ResponseEntity<?> modifierChamp(Long id, Champ champ);
    List<Champ> recuperChampActives();

    Champ recupererChampParId(Long id);
}
