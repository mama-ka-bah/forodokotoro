package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.Models.Stocks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StocksService {
    ResponseEntity<?> ajouterStock(Stocks stocks, String url, String nomfile, MultipartFile file) throws IOException;
    ResponseEntity<?> modifierStock(Long id, Stocks stocks);
    List<Stocks> recupererStockActive();

    Stocks recupererParId(Long id);
}
