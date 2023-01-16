package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.Cultive;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Models.Parserelle;
import com.foro.forordokotoro.Models.Previsions;
import com.foro.forordokotoro.Repository.ChampsRepository;
import com.foro.forordokotoro.Repository.CultiveRepository;
import com.foro.forordokotoro.Repository.ParserelleRepository;
import com.foro.forordokotoro.Repository.PrevisionDuncultiveRepository;
import com.foro.forordokotoro.payload.Autres.Reponse;
import com.foro.forordokotoro.Models.PrevisionDunCultive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CultiveServiceImpl implements CultivesService{

    @Autowired
    CultiveRepository cultiveRepository;
    @Autowired
    private ChampsRepository champsRepository;

    @Autowired
    VarietesServices varietesServices;

    @Autowired
    PrevisionService previsionService;

    @Autowired
    PrevisionDuncultiveRepository previsionDuncultiveRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    ParserelleRepository parserelleRepository;

    @Autowired
    ParserelleService parserelleService;

    /*
    * L'ajout d'un cultive c'est à dire le semis
    * Pour ajouter unn cultive on a besoin de la date de debut du semis et la date de fin du semis
    * La pareserelle doit etre libre et present
    * On recuperere également la liste de prevision lié à la variété semé et ensuite on determine automatiquement les prevision de la cullture actuelle
    * on les enregistre dans la base
    * puis on met l'état de la parserelle à OCCUPE
    * */
    @Override
    public ResponseEntity<?> ajouterCultive(Cultive cultive) {
        LocalDate datejour = LocalDate.now();
        String reference = cultive.getParserelle().getNom() + "-" + cultive.getDatedebutsemis().getMonth() + "-" + cultive.getDatefinsemis() + "-" + datejour.getYear();
        cultive.setReference(reference);
        if(cultiveRepository.existsByReference(reference)){
            return ResponseEntity.ok(new Reponse("Vous avez déjà semé à cette date", 0));
        }else {

            //Utilise pour recuperer les previsions lié à la variete semé qui seront ensuite modifier pour adapter à la culture actuelle
            List<Previsions> previsionsVarietes = new ArrayList<>();

            //Cette liste sera utilise pour parcourir les previsions contenant dans *previsionsVarietes*
            List<PrevisionDunCultive> toutesprevisionsDunCultive = new ArrayList<>();

            //sera utilisé de maniere temporaire pour parcourir *toutesprevisionsDunCultive*
            PrevisionDunCultive previsiontmp = new PrevisionDunCultive();

            cultive.setEtat(true);
            //on recupere la valeur du recolte prevue
            Long recolteprevue = varietesServices.recupererVarieteParId(cultive.getVarietes().getId()).getResultatparkilo()*cultive.getQuantiteseme();
            cultive.setRecoleprevue(recolteprevue);

            //on recupere la liste des previsions lié à la varieté semé
            previsionsVarietes = previsionService.recupererPrevisionActives();

            //on calcule la moyenne entre la date de debut et la date de fin
            long moyenneDifferenceDateSemis = ChronoUnit.DAYS.between(cultive.getDatefinsemis(), cultive.getDatedebutsemis())/2;
            LocalDate dateReference = cultive.getDatedebutsemis().plusDays(moyenneDifferenceDateSemis);

            //on parcours la previsons pour determiner la previsions rééles de la culture
            /*
            Cette boucle va determiner les previsions un à un puis les stocker un obget "prevision temporaire"
            Cet objet est ensuite ajouter aufur et à mesure à une liste qui sera en fin enregistré dans la base
             */
            for(Previsions pc : previsionsVarietes){
                //detrmination d'une prevision
                previsiontmp.setDateprevisionnelle(dateReference.plusDays(pc.getDelaijour()));
                previsiontmp.setLibelle(pc.getLibelle());
                previsiontmp.setNbrepluieNec(pc.getNbrepluie());
                previsiontmp.setRecoltePrevue(recolteprevue);
                previsiontmp.setCultive(cultive);

                //ajout des previosns à la liste au fur et à mesure de l'execution de la boucle
                toutesprevisionsDunCultive.add(previsiontmp);
            }

            //enregistre la lultive
            Parserelle parserelle = cultiveRepository.save(cultive).getParserelle();

            parserelle.setStatus(EstatusParserelle.OCCUPE);

            //on modifie l' état de la parserelle
            parserelleService.modifier(parserelle, parserelle.getId());

            //Enregistrement de la liste de previsions
            previsionDuncultiveRepository.saveAll(toutesprevisionsDunCultive);


            return ResponseEntity.ok(new Reponse("Votre cultive a été créé avec le reference: "+cultive.getReference(), 1));
        }
    }

    /*
        Ici on recupere toutes les cultures en cours et leur activité
        ensuite on verifie les activitées( avec la condition jours en avance et 5 jours apres)
        Toutes activite conteant dans cet intervalle le propriettaire sera notofié
        la fonction s'execute toutes les 24heurs pour verifier cette condition
     */
    @Scheduled(fixedRate = 86400000)
    public void verificationDatePrevisionelle(){
        System.out.println("ttttttttttttttttttttttttttttttttttttt");
        List<PrevisionDunCultive> toutesprevisionsDunCultive = previsionDuncultiveRepository.findAll();
        LocalDate datejour = LocalDate.now();
       for(PrevisionDunCultive pc: toutesprevisionsDunCultive){
           if(pc.getCultive().getEtat()){
               long ecart = ChronoUnit.DAYS.between(pc.getDateprevisionnelle(), datejour);
               System.out.println("ghgjklmvbn "+ ecart);
               if( ecart <= 5 && ecart >= -5){
                   String message = "Votre cultive avec le reference "+ pc.getCultive().getReference()  +" du champ " + pc.getCultive().getParserelle() + " tend vers" + pc.getLibelle();
                   emailSenderService.sendSimpleEmail(pc.getCultive().getParserelle().getChamp().getProprietaire().getEmail(), "Prevision", message);
               }
           }
       }
    }

    @Override
    public ResponseEntity<?> modifierCultive(Cultive cultive, Long id) {
        return cultiveRepository.findById(id)
                .map(c-> {
                    if(cultive.getQuantiteseme() != null)
                        c.setQuantiteseme(c.getQuantiteseme());
                    if(cultive.getDatedebutsemis() != null)
                        c.setDatedebutsemis(cultive.getDatedebutsemis());
                    if(cultive.getDatefinsemis() != null)
                        c.setDatedebutsemis(cultive.getDatedebutsemis());
                    if(cultive.getEtat() != null)
                        c.setEtat(cultive.getEtat());
                    if(cultive.getRecoleprevue() != null)
                        c.setRecoleprevue(cultive.getRecoleprevue());
                    if(cultive.getRecolterealise() != null)
                        c.setRecolterealise(cultive.getRecolterealise());
                    if(cultive.getParserelle() != null)
                        c.setParserelle(cultive.getParserelle());
                    if(cultive.getVarietes() != null)
                        c.setVarietes(cultive.getVarietes());
                    cultiveRepository.save(c);
                    return new ResponseEntity<>("Modification reçu", HttpStatus.OK);
    }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public List<Cultive> recupererCultiveDunchamp(Long parserelleid) {
        return cultiveRepository.findByParserelle(parserelleRepository.findById(parserelleid).get());
    }

    @Override
    public List<Cultive> recupererTousLesCultiveActive() {
        return cultiveRepository.findByEtat(true);
    }

    @Override
    public Cultive recupererCultiveDunChampEnfonctionDateDebut(LocalDate datedebut, Long parserelleid) {
        return cultiveRepository.findByDatedebutsemisAndParserelle(datedebut, parserelleRepository.findById(parserelleid).get());
    }

    @Override
    public Cultive recupererCultiveParReference(String reference) {
        return cultiveRepository.findByReference(reference);
    }

    @Override
    public Cultive recupererParId(Long id) {
        return cultiveRepository.findById(id).get();
    }
}
