package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PrevisionMeteo;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.PrevisionMeteoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PrevisionMeteoServiceImpl implements PrevisionMeteoService{

    @Autowired
    PrevisionMeteoRepository previsionMeteoRepository;

    @Autowired
    ChampsRepository champsRepository;

    @Autowired
    ChampServices champServices;

    @Override
    public PrevisionMeteo ajouter(PrevisionMeteo previsionMeteo) {

        return previsionMeteoRepository.save(previsionMeteo);
    }

    @Override
    public List<PrevisionMeteo> ajouterListePrevision(List<PrevisionMeteo> previsionMeteo) {
        return previsionMeteoRepository.saveAll(previsionMeteo);
    }

    @Override
    public List<PrevisionMeteo> recupererPrevisionsChamp(Long champid) {
        if(champServices.recupererChampParId(champid) == null){
            return null;
        }
        return previsionMeteoRepository.findByChamp(champServices.recupererChampParId(champid));
    }

    @Override
    public List<PrevisionMeteo> recupererPrevisionsChampEnfonctionDunJour(Long champid, LocalDate jour) {
        if(champServices.recupererChampParId(champid) == null){
            return null;
        }
        return previsionMeteoRepository.findByChampAndJour(champServices.recupererChampParId(champid), jour);
    }

    @Override
    public PrevisionMeteo recupererPrevisionsChampEnfonctionDunJourEtDuneHeure(Long champid, LocalDate jour, LocalTime heure) {
        if(champServices.recupererChampParId(champid) == null){
            return null;
        }
        return previsionMeteoRepository.findByChampAndJourAndHeure(champServices.recupererChampParId(champid),jour,heure);
    }
}
