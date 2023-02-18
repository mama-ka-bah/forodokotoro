package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.AimeStock;
import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AimeStockRepository extends JpaRepository<AimeStock, Long> {
    AimeStock findByStocksAndUtilisateur(Stocks stocks, Utilisateurs utilisateurs);
    List<AimeStock> findByStocksAndAime(Stocks stocks, Boolean aime);

    List<AimeStock> findByStocks(Stocks stocks);
}
