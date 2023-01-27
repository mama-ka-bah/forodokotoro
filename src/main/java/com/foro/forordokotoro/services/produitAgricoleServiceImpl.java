package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Repository.ProduitAgricoleRepositrory;
import com.foro.forordokotoro.Utils.Configurations.ConfigImages;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class produitAgricoleServiceImpl implements ProduitAgricoleService{

    @Autowired
    ProduitAgricoleRepositrory produitAgricoleRepositrory;

    //methode permettant d'ajouter un produit agricole
    @Override
    public ResponseEntity<?> ajouterProduitAgricole(ProduitAgricole produitAgricole, String type, String nomfile, MultipartFile file) throws IOException {
        if(produitAgricoleRepositrory.existsByNom(produitAgricole.getNom())){
            return  ResponseEntity.ok(new Reponse(produitAgricole.getNom() + " existe déjà", 0));
        }else {

            produitAgricole.setPhoto(ConfigImages.saveimg(type, nomfile, file));
            produitAgricoleRepositrory.save(produitAgricole);
            return ResponseEntity.ok(new Reponse(produitAgricole.getNom() + "a été ajouter avec succès", 1));
        }
    }

    @Override
    public ResponseEntity<?> modifierProduitAgricole(ProduitAgricole produitAgricole, Long id) {
        return produitAgricoleRepositrory.findById(id)
                .map(pa-> {
                    if(produitAgricole.getNom() != null)
                   pa.setNom(produitAgricole.getNom());
                    if(produitAgricole.getDescription() != null)
                   pa.setDescription(produitAgricole.getDescription());
                    if(produitAgricole.getEtat() != null)
                   pa.setEtat(produitAgricole.getEtat());
                    if(produitAgricole.getStatusubvention() != null)
                   pa.setStatusubvention(produitAgricole.getStatusubvention());
                    produitAgricoleRepositrory.save(pa);

                    return ResponseEntity.ok(new Reponse("Modification reçu", 1));

                }).orElseThrow(() -> new RuntimeException("Produit non trouvé ! "));
    }

    @Override
    public List<ProduitAgricole> RecupererproduitAgricoleActives() {

        return produitAgricoleRepositrory.findByEtat(true);
    }

    @Override
    public ProduitAgricole recupererProduitAgricoleParId(Long id) {
        return produitAgricoleRepositrory.findById(id).get();
    }
}
