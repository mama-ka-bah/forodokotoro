package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.EvolutionStock;
import com.foro.forordokotoro.Models.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvolutionStockRepository extends JpaRepository<EvolutionStock, Long> {
    List<EvolutionStock> findByStocksOrderByDateDesc(Stocks stocks);

    @Query(value = "SELECT SUM(quantitededuit) FROM evolution_stock", nativeQuery = true)
    Double SUMSTOCKVENDU();

    @Query(value = "SELECT SUM(quantiterestant) FROM evolution_stock", nativeQuery = true)
    Double SUMSTOCKRESTANT();
}
