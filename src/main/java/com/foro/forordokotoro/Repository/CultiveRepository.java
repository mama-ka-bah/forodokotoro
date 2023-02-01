package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Champ;
import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Models.Varietes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CultiveRepository extends JpaRepository<Cultive, Long> {

    Boolean existsByReference(String reference);
    Boolean existsByDatedebutsemis(String datedebutsemis);
    List<Cultive> findByParserelle(Parserelle parserelle);
    Cultive findByVarietes(Varietes varietes);
    Cultive findByReference(String reference);

    List<Cultive> findByEtat(Boolean etat);

    //retourne le cultive d'une parserelle en fonction de la date de debut de semis et d'une parserelle
    Cultive findByDatedebutsemisAndParserelle(LocalDate datedebutsemis, Parserelle parserelle);

    List<Cultive> findByParserelleAndEtatOrderByDatedebutsemisDesc(Parserelle parserelle, Boolean etat);

}
