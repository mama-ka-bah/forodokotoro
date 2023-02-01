package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.PhaseCultive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhaseCultiveRepository extends JpaRepository<PhaseCultive, Long> {
    PhaseCultive findByLibelle(String libelle);
    List<PhaseCultive> findByEtat(Boolean etat);
    PhaseCultive findByCultive(Cultive cultive);
    Boolean existsByLibelle(String libelle);

    Boolean existsByLibelleAndCultive(String libelle, Cultive cultive);

    List<PhaseCultive> findByCultiveAndEtatOrderByDatefinDesc(Cultive cultive, Boolean etat);


}
