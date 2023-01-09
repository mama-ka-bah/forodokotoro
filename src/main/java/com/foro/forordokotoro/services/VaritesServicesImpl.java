package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Varietes;
import com.foro.forordokotoro.Repository.VarietesRepository;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VaritesServicesImpl implements VarietesServices {

    @Autowired
    VarietesRepository varietesRepository;

    @Override
    public ResponseEntity<?> ajouterVarietes(Varietes varietes, String url, String nomfile, MultipartFile file) throws IOException {
        if(varietesRepository.existsByNom(varietes.getNom())){
            return ResponseEntity.ok(new Reponse(varietes.getNom() + " existe déjà", 1));
        }else {
            ConfigImages.saveimg(url, nomfile, file);
            varietes.setPhoto(nomfile);
            varietesRepository.save(varietes);
            return ResponseEntity.ok(new Reponse(varietes.getNom() + " a été ajouter avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifiervarietes(Long id, Varietes varietes) {
        return null;
    }

    @Override
    public List<Varietes> recupererVarietesActives() {
        return null;
    }

    @Override
    public ResponseEntity<?> modifierPhotoVarietes(String url, String nomfile, MultipartFile file) {
        return null;
    }
}
