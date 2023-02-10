package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.AimeStock;
import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.http.ResponseEntity;

public interface AimeStockService {

    ResponseEntity<?> ajouter(AimeStock aimeStock, Utilisateurs utilisateurs, Stocks stocks);
    ResponseEntity<?> modifier(Long id, AimeStock aimeStock, Stocks stocks, Utilisateurs utilisateurs);

    ResponseEntity<?> recupererListeDesJaimeDunStock(Stocks stocks);
}
