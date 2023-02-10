package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.EvolutionStock;
import com.foro.forordokotoro.Models.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolutionStockRepository extends JpaRepository<EvolutionStock, Long> {
    List<EvolutionStock> findByStocksOrderByDateDesc(Stocks stocks);
}
