package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PrevisionMeteo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PrevisionMeteoService {
    PrevisionMeteo ajouter(PrevisionMeteo previsionMeteo);

    List<PrevisionMeteo> ajouterListePrevision(List<PrevisionMeteo> previsionMeteo);
    List<PrevisionMeteo> recupererPrevisionsChamp(Long champid);

    List<PrevisionMeteo> recupererPrevisionsChampEnfonctionDunJour(Long champid, LocalDate jour);

    PrevisionMeteo recupererPrevisionsChampEnfonctionDunJourEtDuneHeure(Long champid, LocalDate jour, LocalTime heure);

}
