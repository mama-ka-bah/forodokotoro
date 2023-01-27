package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.ProduitAgricole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProduitAgricoleService {
    ResponseEntity<?> ajouterProduitAgricole(ProduitAgricole produitAgricole, String type, String nomfile, MultipartFile file) throws IOException;

    ResponseEntity<?> modifierProduitAgricole(ProduitAgricole produitAgricole, Long id);

    List<ProduitAgricole> RecupererproduitAgricoleActives();

    ProduitAgricole recupererProduitAgricoleParId(Long id);
}
