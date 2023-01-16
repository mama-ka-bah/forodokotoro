package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.PrevisionMeteo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PrevisionMeteoRepository extends JpaRepository<PrevisionMeteo, Long> {
    List<PrevisionMeteo> findByChamp(Champ champ);
    List<PrevisionMeteo> findByChampAndJour(Champ champ, LocalDate jour);
    PrevisionMeteo findByChampAndJourAndHeure(Champ champ, LocalDate jour, LocalTime heure);
}
