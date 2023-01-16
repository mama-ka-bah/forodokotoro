package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.EvolutionStock;
import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Repository.EvolutionStockRepository;
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

    @Autowired
    EvolutionStockRepository evolutionStockRepository;

    @Override
    public ResponseEntity<?> ajouterStock(Stocks stocks, String url, String nomfile, MultipartFile file) throws IOException {
        if(stockRepository.existsByLibelle(stocks.getLibelle())){
            return ResponseEntity.ok(new Reponse(stocks.getLibelle() + " existe déjà", 1));
        }else {
            ConfigImages.saveimg(url, nomfile, file);
            stocks.setPhoto(nomfile);
            stocks.setQuantiterestant(stocks.getNombrekilo());
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
                    if(stocks.getQuantiterestant() != null)
                        s.setQuantiterestant(stocks.getQuantiterestant());
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

    @Override
    public ResponseEntity<?> mettreajourLestock(EvolutionStock evolutionStock) {
        evolutionStockRepository.save(evolutionStock);
        List<EvolutionStock> listEvolutionStocks = recupererEvolutionStock();
        Long totalStockDeduit = 0L;
        Long totalStockAjoute = 0l;
        Stocks stocks = new Stocks();


        for(EvolutionStock es : listEvolutionStocks){
            totalStockDeduit += es.getQuantitededuit();
            totalStockAjoute += es.getQuantiteajoute();
        }

        stocks.setQuantiterestant(evolutionStock.getStocks().getNombrekilo() + totalStockAjoute - totalStockDeduit);
        modifierStock(evolutionStock.getStocks().getId(), stocks);

        return ResponseEntity.ok(new Reponse("Votre stock a été mise à jour avec succès", 1));
    }

    @Override
    public ResponseEntity<?> modifierEvolution(EvolutionStock evolutionStock, Long id) {
        return evolutionStockRepository.findById(id)
                .map(es-> {
                    if(evolutionStock.getQuantiteajoute() != null)
                        es.setQuantiteajoute(evolutionStock.getQuantiteajoute());
                    if(evolutionStock.getQuantitededuit() != null)
                        evolutionStock.setQuantitededuit(evolutionStock.getQuantitededuit() );
                    if(evolutionStock.getDate() != null)
                        es.setDate(evolutionStock.getDate());
                    if(evolutionStock.getStocks() != null)
                        es.setStocks(evolutionStock.getStocks());
                    evolutionStockRepository.save(es);
                    return ResponseEntity.ok(new Reponse("Modification reçu", 1));
                }).orElseThrow(() -> new RuntimeException("evolutionStock non trouvé ! "));
    }

    @Override
    public List<EvolutionStock> recupererEvolutionStock() {
        return evolutionStockRepository.findAll();
    }

    @Override
    public ResponseEntity<?> supprimerEvolutionStock(Long id) {
        evolutionStockRepository.deleteById(id);
        return ResponseEntity.ok(new Reponse("Mise à jour effectué avec succès", 1));
    }
}
