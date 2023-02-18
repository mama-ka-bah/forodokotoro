package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Parserelle;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ParserelleService {
    ResponseEntity<?> ajouter(Parserelle parserelle, Long chmpid) throws IOException;
    ResponseEntity<?> modifier(Parserelle parserelle, Long id);
    List<Parserelle> recupererLesParserelleDunChamp(Long idchamp);
}
