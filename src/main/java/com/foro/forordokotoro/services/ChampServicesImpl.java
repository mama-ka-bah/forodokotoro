package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Champ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChampServicesImpl implements ChampServices{

    @Override
    public ResponseEntity<?> ajouterChamp(Champ champ) {

        return null;
    }

    @Override
    public ResponseEntity<?> modifierChamp() {
        return null;
    }

    @Override
    public List<Champ> recuperChampActives() {
        return null;
    }
}
