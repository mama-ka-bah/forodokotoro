package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stocks, Long> {
    Boolean existsByLibelle(String libelle);
    Stocks findByLibelle(String libelle);
    List<Stocks> findByEtatOrderByDatepublicationDesc(Boolean etat);

    List<Stocks> findByEtatAndProprietaireOrderByDatepublicationDesc(Boolean etat, Agriculteurs agriculteurs);
}
