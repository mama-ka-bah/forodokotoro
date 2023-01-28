package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Parserelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParserelleRepository extends JpaRepository<Parserelle, Long> {
    List<Parserelle> findByChamp(Champ champ);

    List<Parserelle> findByChampAndEtat(Champ champ, Boolean etat);

    Boolean existsByNom(String nom);
}
