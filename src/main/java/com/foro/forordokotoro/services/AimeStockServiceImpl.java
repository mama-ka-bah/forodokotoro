package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.AimeStock;
import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Models.Utilisateurs;
import com.foro.forordokotoro.Repository.AimeStockRepository;
import com.foro.forordokotoro.Repository.StockRepository;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.Utils.response.ReponseUserAimeStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AimeStockServiceImpl implements AimeStockService{

    @Autowired
    AimeStockRepository aimeStockRepository;

    @Autowired
    StocksService stocksService;
    @Autowired
    private StockRepository stockRepository;

    @Override
    public ResponseEntity<?> ajouter(AimeStock aimeStock, Utilisateurs utilisateurs, Stocks stocks) {

        int nombreAimeStock = 0;
        int nombreNonAimeStock = 0;

        aimeStock.setStocks(stocks);
        aimeStock.setUtilisateur(utilisateurs);
        AimeStock aimeStockRetouner =  aimeStockRepository.save(aimeStock);

        Stocks stocks1 = new Stocks();

        try{

            if(aimeStock.getAime() == true){
                nombreAimeStock = aimeStockRepository.findByStocksAndAime(stocks, aimeStock.getAime()).size();
                stocks1.setNombreaime((long) nombreAimeStock);
                stocksService.modifierStock(stocks.getId(), stocks1);
            }else{
                nombreNonAimeStock = aimeStockRepository.findByStocksAndAime(stocks, aimeStock.getAime()).size();
                stocks1.setNombrenonaime((long) nombreNonAimeStock);
                stocksService.modifierStock(stocks.getId(), stocks1);
            }
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }

        return ResponseEntity.ok(aimeStockRetouner.getStocks());
    }

    @Override
    public ResponseEntity<?> modifier(Long id, AimeStock aimeStock, Stocks stocks, Utilisateurs utilisateurs) {

        return aimeStockRepository.findById(id)
                .map(as-> {

                    AimeStock aimeStockRetourner = new AimeStock();
                    Stocks stocksARetourner = as.getStocks();

                    if(as.getAime() != aimeStock.getAime()){
                        //aimeStockRetourner = aimeStockRepository.save(as);
                        as.setAime(aimeStock.getAime());
                        aimeStockRepository.save(as);
                    }else {
                        aimeStockRepository.deleteById(as.getId());
                    }


                    int nombreAimeStock = 0;
                    int nombreNonAimeStock = 0;
                    Stocks stocks1 = new Stocks();

                    try{
                        nombreAimeStock = aimeStockRepository.findByStocksAndAime(stocks, true).size();
                        nombreNonAimeStock = aimeStockRepository.findByStocksAndAime(stocks, false).size();
                        stocks1.setNombrenonaime((long) nombreNonAimeStock);
                        stocks1.setNombreaime((long) nombreAimeStock);

                        stocksService.modifierStock(stocks.getId(), stocks1);
                    }catch (Exception e){
                        return ResponseEntity.ok(e);
                    }

                    return ResponseEntity.ok(stocksService.recupererParId(stocksARetourner.getId()));

                }).orElseThrow(() -> new RuntimeException("Aime non trouvé ! "));
    }

    @Override
    public ResponseEntity<?> recupererListeDesJaimeDunStock(Stocks stocks) {

        List<AimeStock> listeAime = aimeStockRepository.findByStocks(stocks);

        List<ReponseUserAimeStock> listeIdUser = new ArrayList<>();

        for(AimeStock as : listeAime){
            ReponseUserAimeStock reponseUserAimeStock = new ReponseUserAimeStock();
            reponseUserAimeStock.setId(as.getUtilisateur().getId());
            reponseUserAimeStock.setStatus(as.getAime());
            listeIdUser.add(reponseUserAimeStock);
        }

        return ResponseEntity.ok(listeIdUser);
    }
}
