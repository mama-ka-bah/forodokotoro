package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.PrevisionDunCultive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrevisionDuncultiveRepository extends JpaRepository<PrevisionDunCultive, Long> {
    List<PrevisionDunCultive> findByCultiveOrderByDateprevisionnelleAsc(Cultive cultive);

}
