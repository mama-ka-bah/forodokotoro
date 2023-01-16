package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Previsions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrevisionsRepository extends JpaRepository<Previsions, Long> {
    Boolean existsByLibelle(String libelle);
    Previsions findByLibelle(String libelle);
    List<Previsions> findByEtatOrderByDelaijour(Boolean etat);
}
