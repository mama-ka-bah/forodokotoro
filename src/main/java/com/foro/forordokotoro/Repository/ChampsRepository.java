package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Champ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChampsRepository extends JpaRepository<Champ, Long> {
    Boolean existsByNom(String nom);
    Champ findByNom(String nom);
    List<Champ> findByProprietaire(Agriculteurs agriculteurs);
    List<Champ> findByEtat(Boolean etat);
}
