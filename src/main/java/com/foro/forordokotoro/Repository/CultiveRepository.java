package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.Varietes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CultiveRepository extends JpaRepository<Cultive, Long> {

    Boolean existsByReference(String reference);
    Boolean existsByDatedebutsemis(String datedebutsemis);
    List<Cultive> findByChamp(Champ champ);
    Cultive findByVarietes(Varietes varietes);
    Cultive findByReference(String reference);

    List<Cultive> findByEtat(Boolean etat);
    Cultive findByDatedebutsemisAndChamp(LocalDate datedebutsemis, Champ champ);
}
