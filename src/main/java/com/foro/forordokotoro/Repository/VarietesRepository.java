package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Varietes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VarietesRepository extends JpaRepository<Varietes, Long> {
    Boolean existsByNom(String nom);
    Varietes findByNom(String nom);
    List<Varietes> findByProduitagricole(ProduitAgricole produitAgricole);
    List<Varietes> findByEtat(Boolean etat);
}


