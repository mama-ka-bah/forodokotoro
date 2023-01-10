package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.PhaseCultive;
import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Stocks;
import com.foro.forordokotoro.Repository.StockRepository;
import com.foro.forordokotoro.services.AgriculteurService;
import com.foro.forordokotoro.services.ProduitAgricoleService;
import com.foro.forordokotoro.services.StocksService;
import com.foro.forordokotoro.services.VarietesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class StockControlleur {

    @Autowired
    StocksService stocksService;

    @Autowired
    VarietesServices varietesServices;

    @Autowired
    AgriculteurService agriculteurService;

    @PostMapping("/ajouter/{idvarietes}/{idproprietaire}")
    public ResponseEntity<?> ajouterStocks(@Valid @RequestParam(value = "file") MultipartFile file,
                                          @Valid  @RequestParam(value = "stocksReçu") String stocksReçu, @PathVariable Long idvarietes, @PathVariable Long idproprietaire) throws IOException {
        //chemin de stockage des images
        String url = "C:/Users/KEITA Mahamadou/Desktop/keita/project/images";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        Stocks stocks = new JsonMapper().readValue(stocksReçu, Stocks.class);

        stocks.setVarietes(varietesServices.recupererVarieteParId(idvarietes));
        stocks.setProprietaire(agriculteurService.recupererAgriculteurPArId(idproprietaire));
        return stocksService.ajouterStock(stocks, url, nomfile, file);
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
}
