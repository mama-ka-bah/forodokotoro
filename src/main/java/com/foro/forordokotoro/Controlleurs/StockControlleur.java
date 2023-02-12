package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Repository.AimeStockRepository;
import com.foro.forordokotoro.Repository.EvolutionStockRepository;
import com.foro.forordokotoro.Repository.StockRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.response.ReponseUserAimeStock;
import com.foro.forordokotoro.security.services.UtilisateurService;
import com.foro.forordokotoro.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "http://localhost:8100", maxAge = 3600, allowCredentials="true")
public class StockControlleur {

    @Autowired
    StocksService stocksService;

    @Autowired
    VarietesServices varietesServices;

    @Autowired
    AgriculteurService agriculteurService;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    EvolutionStockRepository evolutionStockRepository;

    @Autowired
    AimeStockRepository aimeStockRepository;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    AimeStockService aimeStockService;

    @PostMapping("/ajouter/{idvarietes}/{idproprietaire}")
    public ResponseEntity<?> ajouterStocks(@Valid @RequestParam(value = "file") MultipartFile file,
                                          @Valid  @RequestParam(value = "stocksReçu") String stocksReçu, @PathVariable Long idvarietes, @PathVariable Long idproprietaire) throws IOException {
        //chemin de stockage des images
        String type = "stocks";

        System.out.println("ccccccccccccccccccc: " + idvarietes);

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        Stocks stocks = new JsonMapper().readValue(stocksReçu, Stocks.class);

        stocks.setVarietes(varietesServices.recupererVarieteParId(idvarietes));
        stocks.setProprietaire(agriculteurService.recupererAgriculteurPArId(idproprietaire));
        stocks.setNombreaime(0L);
        stocks.setNombrenonaime(0L);
        return stocksService.ajouterStock(stocks, type, nomfile, file);
    }

    @PatchMapping("/modifier/{id}")
    public ResponseEntity<?> modifierProduitAgricole(@RequestBody Stocks stocks, @PathVariable Long id){

        return  stocksService.modifierStock(id, stocks);
    }

    @GetMapping("/stocksactives")
    public List<Stocks> produitAgricoleActives(){
        return  stocksService.recupererStockActive();
    }

    @GetMapping("/detailstock/{id}")
    public Stocks recupererprodudetailstock(@PathVariable Long id){
        return  stocksService.recupererParId(id);
    }

    @PostMapping("/mettrejourstock/{stockid}/{quantiteRestant}")
    public ResponseEntity<?> mettreJourStock(@RequestBody EvolutionStock evolutionStock, @PathVariable Long stockid, @PathVariable Double quantiteRestant){

        Stocks stocks = stocksService.recupererParId(stockid);
        Stocks stock1 = new Stocks();
        stock1.setQuantiterestant(quantiteRestant);

        evolutionStock.setStocks(stocks);
        evolutionStock.setDate(LocalDate.now());
        stocksService.modifierStock(stocks.getId(), stock1);

        return stocksService.mettreajourLestock(evolutionStock);
    }

    @GetMapping("/recuperertousevolutionstocks")
    public List<EvolutionStock> recupererTousEvolutionStocks(){
        return stocksService.recupererEvolutionStock();
    }

    @GetMapping("/recuperertousevolutionstocksdunstocks/{idStock}")
    public List<EvolutionStock> recupererTousEvolutionStocksDunStocks(@PathVariable Long idStock){
        Stocks stocks = stocksService.recupererParId(idStock);
        return evolutionStockRepository.findByStocksOrderByDateDesc(stocks);
    }

    @PatchMapping("/modifierevolutionstocks/{id}")
    public ResponseEntity<?> modifierEvolutionStocks(@PathVariable Long id, @RequestBody EvolutionStock evolutionStock){
        return stocksService.modifierEvolution(evolutionStock, id);
    }

    @DeleteMapping("/supprimererevolutionstocks{id}")
    public ResponseEntity<?> supprimerEvolutionStocks(Long id){
        return stocksService.supprimerEvolutionStock(id);
    }

    @GetMapping("/recuperertousstocksvalidesagriculteur/{idagri}")
    public List<Stocks> recupererTousStocksActiveAdiculteur(@PathVariable Agriculteurs idagri){

        return stockRepository.findByEtatAndProprietaireOrderByDatepublicationDesc(true,idagri);
    }

    @GetMapping("/recupererlisteaimesdunstock/{idstock}")
    public ResponseEntity<?> recupererListeAimesDunStock(@PathVariable Long idstock){

        return aimeStockService.recupererListeDesJaimeDunStock(stocksService.recupererParId(idstock));
    }

    @PostMapping("/aimerunstock/{idstock}/{iduser}")
    public ResponseEntity<?> aimerUnStock(@PathVariable Long idstock, @PathVariable Long iduser, @RequestBody AimeStock aimeStock){

        Stocks stocks = stocksService.recupererParId(idstock);
        Utilisateurs utilisateur = utilisateursRepository.findById(iduser).get();

        AimeStock aimeStockRetourner = aimeStockRepository.findByStocksAndUtilisateur(stocks, utilisateur);

        if(aimeStockRetourner != null){
            return aimeStockService.modifier(aimeStockRetourner.getId(), aimeStock, stocks, utilisateur);
        }else {
            return aimeStockService.ajouter(aimeStock,utilisateur, stocks);
        }
    }
}
