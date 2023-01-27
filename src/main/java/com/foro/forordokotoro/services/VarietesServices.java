package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Varietes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VarietesServices {
    ResponseEntity<?> ajouterVarietes(Varietes varietes, String type, String nomfile, MultipartFile file) throws IOException;
    ResponseEntity<?> modifiervarietes(Long id, Varietes varietes);
    List<Varietes> recupererVarietesActives();
    ResponseEntity<?> modifierPhotoVarietes(String url, String nomfile, MultipartFile file);
    List<Varietes> recupererVarietesParProduitAgricole(ProduitAgricole produitAgricole);

    Varietes recupererVarieteParId(Long id);
}
