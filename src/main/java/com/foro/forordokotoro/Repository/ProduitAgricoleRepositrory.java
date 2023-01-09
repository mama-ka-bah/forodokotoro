package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.ProduitAgricole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitAgricoleRepositrory extends JpaRepository<ProduitAgricole, Long> {
    Boolean existsByNom(String nom);

    ProduitAgricole findByNom(String nom);

    List<ProduitAgricole> findByEtat(Boolean etat);
}
