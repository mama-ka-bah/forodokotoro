package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Parserelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParserelleRepository extends JpaRepository<Parserelle, Long> {
    List<Parserelle> findByChamp(Champ champ);

    Boolean existsByNom(String nom);
}
