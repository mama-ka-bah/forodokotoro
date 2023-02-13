package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Commentaires;
import com.foro.forordokotoro.Models.Publications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PublicationsService {
    ResponseEntity<?> ajouter(Publications publications,  String type, String nomfile, MultipartFile file) throws IOException;
    ResponseEntity<?> modifier(Long idPub, Publications publications);

    ResponseEntity<?> modifierCommentaire(Long idCommentaire, Commentaires commentaire);
}
