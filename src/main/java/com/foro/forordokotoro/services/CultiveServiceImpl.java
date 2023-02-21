package com.foro.forordokotoro.services;

import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EstatusCultive;
import com.foro.forordokotoro.Models.Enumerations.EstatusParserelle;
import com.foro.forordokotoro.Repository.*;
import com.foro.forordokotoro.Utils.response.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @Autowired
    VarietesRepository varietesRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PhaseCultiveRepository phaseCultiveRepository;

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
        Random random = new Random();
        /*
        * On forme la reference avec le mon du parserelle + le jour eu debut  et fin de semis plus l'anne de semis plus l'anne actuelle

        * * */



        String reference = cultive.getParserelle().getNom().trim() + cultive.getDatedebutsemis().getMonthValue() + cultive.getDatedebutsemis().getDayOfMonth() + cultive.getDatefinsemis().getMonthValue() + cultive.getDatefinsemis().getDayOfMonth() + cultive.getDatefinsemis().getYear() + cultive.getParserelle().getId();
        cultive.setReference(reference);

        //on verifie si la parsell n'etatait pas occuper dans l'intervalle demandé
        Reponse reponse = verifierValiditeDesDatesDeCultive(cultive.getDatedebutsemis(), cultive.getDatefinsemis(), cultive);

            if(reponse.getStatus() == 0){
                return ResponseEntity.ok(reponse);
            } else if (cultive.getParserelle().getStatus() == EstatusParserelle.OCCUPE) {
                return ResponseEntity.ok(new Reponse("Cette parserelle est occupée", 0));
            }else {

            //si la date de de debut du semis est aujourd'hui ou avant aujoujourd'hui
             if((cultive.getDatedebutsemis().isBefore(datejour) || cultive.getDatedebutsemis().equals(datejour)) && cultive.getDatefinsemis().isBefore(datejour) || cultive.getDatefinsemis().equals(cultive.getDatedebutsemis())){
                cultive.setStatus(EstatusCultive.ENCOURS);

                 //Utilise pour recuperer les previsions lié à la variete semé qui seront ensuite modifier pour adapter à la culture actuelle
                 List<Previsions> previsionsVarietes = new ArrayList<>();

                 //Cette liste sera utilise pour parcourir les previsions contenant dans *previsionsVarietes*
                 List<PrevisionDunCultive> toutesprevisionsDunCultive = new ArrayList<>();

                 cultive.setEtat(true);
                 //on recupere la valeur du recolte prevue
                 Double recolteprevue = varietesServices.recupererVarieteParId(cultive.getVarietes().getId()).getResultatparkilo()*cultive.getQuantiteseme();
                 cultive.setRecoleprevue(recolteprevue);

                 //on recupere la liste des previsions lié à la varieté semé
                 // previsionsVarietes = previsionService.recupererPrevisionActives();
                 previsionsVarietes = cultive.getVarietes().getPrevisions();

                 //on calcule la moyenne entre la date de debut et la date de fin (difference de jours divisé par deux
                 long moyenneDifferenceDateSemis = ChronoUnit.DAYS.between(cultive.getDatefinsemis(), cultive.getDatedebutsemis())/2;

                 //ici on calcule une date de reference pour nos differentes traiteements
                 LocalDate dateReference = cultive.getDatedebutsemis().plusDays(moyenneDifferenceDateSemis);

                // cultive.getParserelle().setStatus(EstatusParserelle.OCCUPE);
                 //parserelleService.modifier(cultive.getParserelle(), cultive.getParserelle().getId());
                 //enregistre la lultive
                 cultiveRepository.save(cultive);

                 //on parcours la previsons pour determiner la previsions rééles de la culture
                /*
                Cette boucle va determiner les previsions un à un puis les stocker un obget "prevision temporaire"
                Cet objet est ensuite ajouter aufur et à mesure à une liste qui sera en fin enregistré dans la base
                 */

                 for(Previsions pc : previsionsVarietes){
                     //sera utilisé de maniere temporaire pour parcourir *toutesprevisionsDunCultive*
                     PrevisionDunCultive previsiontmp = new PrevisionDunCultive();

                     //detrmination d'une prevision
                     previsiontmp.setDateprevisionnelle(dateReference.plusDays(pc.getDelaijour()));
                     previsiontmp.setLibelle(pc.getLibelle());
                     previsiontmp.setNbrepluieNec(pc.getNbrepluienecessaire());
                     previsiontmp.setRecoltePrevue(recolteprevue);
                     previsiontmp.setCultive(cultive);

                     //toutesprevisionsDunCultive.add(previsiontmp);
                     //on enregistre la prevision conrrespondant à cette cultive à chaque tour du boucle
                     previsionDuncultiveRepository.save(previsiontmp);
                 }

                 //on recupere la parserelle concerné
                 Parserelle parserelle = cultive.getParserelle();

                 //on met son état à ocuupe
                 parserelle.setStatus(EstatusParserelle.OCCUPE);

                 //on cree un nouveau objet pour modifier pour modifier l'etat de la parserelle
                 Parserelle pn = new Parserelle();
                 pn.setStatus(EstatusParserelle.OCCUPE);

                 if(parserelle.getNombrecultive() != null)
                 //on incremente le nombre de cultive lié à la parserelle
                 pn.setNombrecultive(parserelle.getNombrecultive()+1);

                 //on modifie l' état de la parserelle
                 parserelleService.modifier(pn, parserelle.getId());

                 System.out.println("Le nombre de previson à enregistré" + toutesprevisionsDunCultive.size());

                 return ResponseEntity.ok(new Reponse("Votre cultive a été créé avec le reference: "+cultive.getReference(), 1));
            }else {
                 return ResponseEntity.ok(new Reponse("Veuillez semer puis renseigner les informations", 0));
             }
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
        List<PrevisionDunCultive> toutesprevisionsDunCultive = previsionDuncultiveRepository.findAll();
        LocalDate datejour = LocalDate.now();
       for(PrevisionDunCultive pc: toutesprevisionsDunCultive){
           if(pc.getCultive().getEtat()){
               long ecart = ChronoUnit.DAYS.between(datejour, pc.getDateprevisionnelle());
               System.out.println("ecart "+ ecart);
               if( ecart <= 5 && ecart >= -5){
                   String message = "Votre cultive avec le reference "+ pc.getCultive().getReference()  +" du champ " + pc.getCultive().getParserelle() + " tend vers" + pc.getLibelle();

                   Notifications notifications =new Notifications();
                   notifications.setLu(false);
                   notifications.setDatenotification(new Date());
                   notifications.setContenu("message votre cultive ayant les references suivant a une activiteé qui doit etre effectué dans deux jours veuillez la realiser");
                   notifications.setTitre("Simulation");
                   notifications.setUserid(pc.getCultive().getParserelle().getChamp().getProprietaire());
                   notificationRepository.save(notifications);
                   notificationRepository.save(new Notifications());
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
                    if(cultive.getDatefinCultive() != null)
                        c.setDatefinCultive(cultive.getDatefinCultive());
                    if(cultive.getEtat() != null)
                        c.setEtat(cultive.getEtat());
                    if(cultive.getStatus() != null)
                        c.setStatus(cultive.getStatus());
                    if(cultive.getRecoleprevue() != null)
                        c.setRecoleprevue(cultive.getRecoleprevue());
                    if(cultive.getRecolterealise() != null)
                        c.setRecolterealise(cultive.getRecolterealise());
                    if(cultive.getParserelle() != null)
                        c.setParserelle(cultive.getParserelle());
                    if(cultive.getVarietes() != null)
                        c.setVarietes(cultive.getVarietes());
                    cultiveRepository.save(c);
                    return ResponseEntity.ok(new Reponse("Mise à jour reçue", 1));
    }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public List<Cultive> recupererCultiveDunchamp(Long parserelleid) {
        return cultiveRepository.findByParserelleAndEtat(parserelleRepository.findById(parserelleid).get(), true);
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


    //cette methode permet de verifier s'il ya une phase du cultive qui n'est pas dans l'intervalle dateFinCultive
    @Override
    public Reponse verificationAvantMettreFinUnCultive(Long idCultive, Cultive cultive) {
    //cultive.setId(idCultive);
        List<PhaseCultive> lesPhasesDuCunltive = phaseCultiveRepository.findByEtatAndCultive(true, cultiveRepository.findById(idCultive).get());

        Cultive lecultive  = cultiveRepository.findById(idCultive).get();

        if(lecultive.getDatefinCultive() == null){
            if(lesPhasesDuCunltive.size() > 0){
                for(PhaseCultive phc : lesPhasesDuCunltive){
                    if(phc.getDatefin().isAfter(cultive.getDatefinCultive())){
                        System.out.println("" + phc.getDatefin());
                        return new Reponse("La date de fin de la phase " + phc.getLibelle() + " est superieur à " + cultive.getDatefinCultive(), 0);
                    }
                }
            }

            return  new Reponse("Le cultive " + lecultive.getReference() +" est desormais terminé" , 1);
        }else {
            return  new Reponse("Ce cultive est déjà terminé" , 0);
        }

    }

    @Override
    public ResponseEntity<?> mettreFincultive(Long idCultive, Cultive cultive) {
        Cultive cultiveAvecDonneeComplet = cultiveRepository.findById(idCultive).get();
        //Reponse reponse = verificationAvantMettreFinUnCultive(idCultive, cultiveAvecDonneeComplet);

        Reponse reponse = verificationAvantMettreFinUnCultive(idCultive, cultive);

        Reponse reponse1 = verifierValiditeDesDatesDeCultive(cultiveAvecDonneeComplet.getDatedebutsemis(), cultiveAvecDonneeComplet.getDatefinsemis(), cultiveAvecDonneeComplet);
        return cultiveRepository.findById(idCultive)
                .map(c-> {
                    if(cultive.getDatefinCultive() != null)
                        if(reponse.getStatus() == 1 && reponse1.getStatus() == 1){
                            c.setDatefinCultive(cultive.getDatefinCultive());
                            c.setStatus(EstatusCultive.TERMINER);
                            c.setRecolterealise(cultive.getRecolterealise());
                        }else if (reponse.getStatus() == 1 && reponse1.getStatus() == 0){
                            return ResponseEntity.ok(reponse1);
                        }else {
                            return ResponseEntity.ok(reponse);
                        }
                    cultiveRepository.save(c);
                    Parserelle parserelle = new Parserelle();
                    parserelle.setStatus(EstatusParserelle.LIBRE);
                    parserelleService.modifier(parserelle, c.getParserelle().getId());
                    return  ResponseEntity.ok(reponse);
                }).orElseThrow(() -> new RuntimeException("Champ non trouvé ! "));
    }

    @Override
    public Reponse verifierValiditeDesDatesDeCultive(LocalDate datedebut, LocalDate datefin, Cultive cultive) {
        //on recuper toutes les cultives actives lié à la parserelle
        List<Cultive> cultivesActives = cultiveRepository.findByParserelleAndEtat(cultive.getParserelle(), true);

        //on verifie s'il ya dejà une cultive active
        if(!cultivesActives.isEmpty()){
            //dans le cas ou il existe de cultive active on verfie les dates de ces differentes cultives
            for (Cultive c : cultivesActives){
                //ici on verifie si le cultive actuel est terminer ou pas dans le cas ou c'est  termine on verfie sa date de fin sinon
                //on verifie uniquement des dates de semis
                if(c.getDatefinCultive() != null){
                    if(datedebut.equals(c.getDatedebutsemis()) || datedebut.equals(c.getDatedebutsemis()) || datefin.equals(c.getDatedebutsemis()) || datefin.equals(c.getDatedebutsemis()) || datefin.equals(c.getDatefinCultive()) || datedebut.equals(c.getDatefinCultive())){
                        return new Reponse("Ce Parserelle était occupé dans cet intervalle", 0);
                    }
                    //dans le cas ou le cultive n'est pas termine on se concentre uniquement sur les dates de semis
                    else if(datedebut.isAfter(c.getDatedebutsemis()) && datedebut.isBefore(c.getDatefinCultive()) || datefin.isAfter(c.getDatedebutsemis()) && datefin.isBefore(c.getDatefinCultive())){  //|| datedebut.equals(c.getDatedebutsemis()) ||  datedebut.equals(c.getDatedebutsemis())
                        return new Reponse("Ce Parserelle était occupé dans cet intervalle", 0);
                    }
                    else if (datedebut.isAfter(c.getDatedebutsemis()) && datedebut.isBefore(c.getDatefinsemis()) || datefin.isAfter(c.getDatedebutsemis()) && datefin.isBefore(c.getDatefinsemis()) ) { //|| datedebut.equals(c.getDatedebutsemis()) ||  datedebut.equals(c.getDatefinsemis())
                        return new Reponse("Cet Parserelle était occupée dans cet intervalle", 0);
                    }
                    }
            }
        }



        return new Reponse("ok", 1);
    }

}