package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Repository.StockRepository;
import com.foro.forordokotoro.payload.Autres.ConfigImages;
import com.foro.forordokotoro.payload.Autres.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StockServiceImpl implements StocksService{

    @Autowired
    StockRepository stockRepository;

    @Override
    public ResponseEntity<?> ajouterStock(Stocks stocks, String url, String nomfile, MultipartFile file) throws IOException {
        if(stockRepository.existsByLibelle(stocks.getLibelle())){
            return ResponseEntity.ok(new Reponse(stocks.getLibelle() + " existe déjà", 1));
        }else {
            ConfigImages.saveimg(url, nomfile, file);
            stocks.setPhoto(nomfile);
            stockRepository.save(stocks);
            return ResponseEntity.ok(new Reponse(stocks.getLibelle() + " a été ajouté avec succès", 1));

        }
    }

    @Override
    public ResponseEntity<?> modifierStock(Long id, Stocks stocks) {
        return stockRepository.findById(id)
                .map(s-> {
                    if(stocks.getDisponibilite() != null)
                        s.setDisponibilite(stocks.getDisponibilite());
                    if(stocks.getNombrekilo() != null)
                        s.setNombrekilo(stocks.getNombrekilo());
                    if(stocks.getPrixkilo() != null)
                        s.setPrixkilo(stocks.getPrixkilo());
                    if(stocks.getLibelle() != null)
                        s.setLibelle(stocks.getLibelle());
                    if(stocks.getEtat() != null)
                        s.setEtat(stocks.getEtat());
                    if(stocks.getNombrekilo() != null)
                        s.setNombrekilo(stocks.getNombrekilo());
                    if(stocks.getVarietes() != null)
                        s.setVarietes(stocks.getVarietes());
                    if(stocks.getProprietaire() != null)
                        s.setProprietaire(stocks.getProprietaire());
                    stockRepository.save(s);
                    return ResponseEntity.ok(new Reponse("Modification reçu", 1));
                }).orElseThrow(() -> new RuntimeException("Stock non trouvé ! "));
    }

    @Override
    public List<Stocks> recupererStockActive() {
        return stockRepository.findByEtatOrderByDatepublicationDesc(true);
    }

    @Override
    public Stocks recupererParId(Long id) {
        return stockRepository.findById(id).get();
    }
}
