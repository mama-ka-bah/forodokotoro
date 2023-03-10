package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Varietes;
import com.foro.forordokotoro.Repository.VarietesRepository;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
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
    public ResponseEntity<?> ajouterVarietes(Varietes varietes, String type, String nomfile, MultipartFile file) throws IOException {
        if(varietesRepository.existsByNom(varietes.getNom())){
            return ResponseEntity.ok(new Reponse(varietes.getNom() + " existe déjà", 1));
        }else {

            varietes.setPhoto(ConfigImages.saveimg(type, nomfile, file));
            varietesRepository.save(varietes);
            return ResponseEntity.ok(new Reponse(varietes.getNom() + " a été ajouter avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifiervarietes(Long id, Varietes varietes) {
        return varietesRepository.findById(id)
                .map(v-> {
                    if(varietes.getNom() != null)
                        v.setNom(varietes.getNom());
                    if(varietes.getDescription() != null)
                        v.setDescription(varietes.getDescription());
                    if(varietes.getEtat() != null)
                        v.setEtat(varietes.getEtat());
                    if(varietes.getCycle() != null)
                        v.setCycle(varietes.getCycle());
                    if(varietes.getPrevisions() != null)
                        v.setPrevisions(varietes.getPrevisions());
                    if(varietes.getProduitagricole() != null)
                        v.setProduitagricole(varietes.getProduitagricole());
                    if(varietes.getTaillefinal() != null)
                        v.setTaillefinal(varietes.getTaillefinal());
                    varietesRepository.save(v);

                    return ResponseEntity.ok(new Reponse("Modification reçu", 1));

                }).orElseThrow(() -> new RuntimeException("Produit non trouvé ! "));
    }

    @Override
    public List<Varietes> recupererVarietesActives() {
        return varietesRepository.findByEtat(true);
    }

    @Override
    public ResponseEntity<?> modifierPhotoVarietes(String url, String nomfile, MultipartFile file) {
        return null;
    }

    @Override
    public List<Varietes> recupererVarietesParProduitAgricole(ProduitAgricole produitAgricole) {

        return varietesRepository.findByProduitagricole(produitAgricole);
    }

    @Override
    public Varietes recupererVarieteParId(Long id) {
        return varietesRepository.findById(id).get();
    }
}
