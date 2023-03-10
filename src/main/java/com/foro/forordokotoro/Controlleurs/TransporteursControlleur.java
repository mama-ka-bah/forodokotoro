package com.foro.forordokotoro.Controlleurs;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foro.forordokotoro.Models.*;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Repository.ReservationRepository;
import com.foro.forordokotoro.Repository.TransporteurEnAttenteRepository;
import com.foro.forordokotoro.Repository.TransporteurRepository;
import com.foro.forordokotoro.Repository.UtilisateursRepository;
import com.foro.forordokotoro.Utils.request.DemandeTransporteur;
import com.foro.forordokotoro.Utils.response.Reponse;
import com.foro.forordokotoro.Utils.response.RetourUserEnttente;
import com.foro.forordokotoro.Utils.response.TransEnAttenteRetour;
import com.foro.forordokotoro.services.TransporteursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/transporteurs")
@CrossOrigin("*")
public class TransporteursControlleur {
    @Autowired
    TransporteursService transporteursService;
    @Autowired
    TransporteurRepository transporteurRepository;

    @Autowired
    UtilisateursRepository utilisateursRepository;

    @Autowired
    TransporteurEnAttenteRepository transporteurAttenteRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TransporteurEnAttenteRepository transporteurEnAttenteRepository;

    @PostMapping("/devenirtransporteur/{id}")
    public ResponseEntity<?> devenirTransporteur(@Valid @RequestParam(value = "file", required = true) MultipartFile file,
                                                 @Valid  @RequestParam(value = "donneesTransporteur") String donneesTransporteur, @PathVariable Long id) throws IOException {

        System.out.println("nnnnnnnnnnnnnnnnnnnnnn" + donneesTransporteur);
        //chemin de stockage des images
        String type = "permis";

        //recupere le nom de l'image
        String nomfile = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(nomfile);

        //envoie le nom, url et le fichier ?? la classe ConfigImages qui se chargera de sauvegarder l'image
        //ConfigImages.saveimg(url, nomfile, file);

        //converssion du string re??u en classe Transporteur
        DemandeTransporteur demandeurProfil1 = new JsonMapper().readValue(donneesTransporteur, DemandeTransporteur.class);
        TransporteurAttente demandeurProfil = new TransporteurAttente();

        demandeurProfil.setNumeroplaque(demandeurProfil1.getNumeroplaque());
        demandeurProfil.setDisponibilite(demandeurProfil1.getDisponibilite());
        demandeurProfil.setPhotopermis(nomfile);
        demandeurProfil.setDatedemande(LocalDate.now());
        demandeurProfil.setStatusdemande(EstatusDemande.ENCOURS);

        return transporteursService.devenirTransporteur(id, demandeurProfil, type, nomfile, file);
    }

    @PostMapping("/accepteratransporteur/{username}")
    public ResponseEntity<?> accepterAgriculteur(@PathVariable String username){

        return transporteursService.accepterTransporteur(username);
    }

    @PostMapping("/rejetertransporteur/{username}")
    public ResponseEntity<?> rejeterTransporetur(@PathVariable String username){

        return transporteursService.rejeterTransporteur(username);
    }


    @PatchMapping("/modifiertransporteur/{id}")
    public ResponseEntity<?> updateTransporteur(@PathVariable Long id, @RequestBody Transporteurs transporteurs) {

        return transporteursService.modifierTransporteur(id, transporteurs);
    }

    @GetMapping("/RecupererTousTransporteur")
    public List<Transporteurs> recupererTransporteurDetail(){
        return  transporteurRepository.findByDisponibilite(true);
    }

    @GetMapping("/detailTransprteur/{id}")
    public Transporteurs recupererTousLesTransporteur(@PathVariable Long id){
        return  transporteursService.recupererTransporteurParId(id);
    }


    @GetMapping("/recupererlestransporteurenattente")
    public ResponseEntity<?> recupererTousLesAgriculteurEnattente(){
        List<TransporteurAttente> transporteurAttenteList = transporteurAttenteRepository.findAll();
        List<Long> idList = new ArrayList<>();
        List<RetourUserEnttente> retourList = new ArrayList<>();
        for(TransporteurAttente te: transporteurAttenteList){
            idList.add(te.getUserid().getId());
        }
//        List<Utilisateurs> utilisateursList = utilisateursRepository.findAllById(idList);
//
//        return ResponseEntity.ok(utilisateursList);
//        List<Transporteurs> transporteursList = transporteurRepository.findAllById(idList);
//        System.out.println(utilisateursRepository.findAllById(idList));

        List<Utilisateurs> utilisateursList = utilisateursRepository.findAllById(idList);
        List<TransEnAttenteRetour> transporteursList = new ArrayList<>();

        for (Utilisateurs u: utilisateursList){
            TransporteurAttente tmp = new TransporteurAttente();

            tmp = transporteurEnAttenteRepository.findByUserid(u);

            Transporteurs utmp = new Transporteurs();

            TransEnAttenteRetour retour = new TransEnAttenteRetour();

            retour.setPhoto(u.getPhoto());
            retour.setNomcomplet(u.getNomcomplet());
            retour.setAdresse(u.getAdresse());
            retour.setUsername(u.getUsername());
            retour.setPhotopermis(tmp.getPhotopermis());
            retour.setStatus(tmp.getStatusdemande());

            transporteursList.add(retour);
        }
        return ResponseEntity.ok(transporteursList);
    }


    @GetMapping("/recupererlestransporteurenattenteencours")
    public ResponseEntity<?> recupererLesAgriculteurEnattente(){
        List<TransporteurAttente> transporteurAttenteList = transporteurAttenteRepository.findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande.ENCOURS);
        List<Long> idList = new ArrayList<>();
        List<RetourUserEnttente> retourList = new ArrayList<>();

        for(TransporteurAttente te: transporteurAttenteList){
            idList.add(te.getUserid().getId());
        }

        List<Utilisateurs> utilisateursList = utilisateursRepository.findAllById(idList);
        List<TransEnAttenteRetour> transporteursList = new ArrayList<>();

        for (Utilisateurs u: utilisateursList){
            TransporteurAttente tmp = new TransporteurAttente();

            tmp = transporteurEnAttenteRepository.findByUserid(u);

            Transporteurs utmp = new Transporteurs();

            TransEnAttenteRetour retour = new TransEnAttenteRetour();

            retour.setPhoto(u.getPhoto());
            retour.setNomcomplet(u.getNomcomplet());
            retour.setAdresse(u.getAdresse());
            retour.setUsername(u.getUsername());
            retour.setPhotopermis(tmp.getPhotopermis());
            retour.setStatus(tmp.getStatusdemande());

            transporteursList.add(retour);
        }

        return ResponseEntity.ok(transporteursList);
    }

    @GetMapping("/recupererlestransporteurenattenterejeter")
    public ResponseEntity<?> recupererLesAgriculteurEnattenteRejeter(){
        List<TransporteurAttente> transporteurAttenteList = transporteurAttenteRepository.findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande.REJETER);
        List<Long> idList = new ArrayList<>();
        List<RetourUserEnttente> retourList = new ArrayList<>();
        for(TransporteurAttente te: transporteurAttenteList){
            idList.add(te.getUserid().getId());
        }
//        List<Utilisateurs> utilisateursList = utilisateursRepository.findAllById(idList);
//        List<Transporteurs> transporteursList = transporteurRepository.findAllById(idList);
//
//        System.out.println(utilisateursRepository.findAllById(idList));

        List<Utilisateurs> utilisateursList = utilisateursRepository.findAllById(idList);
        List<TransEnAttenteRetour> transporteursList = new ArrayList<>();

        for (Utilisateurs u: utilisateursList){
            TransporteurAttente tmp = new TransporteurAttente();

            tmp = transporteurEnAttenteRepository.findByUserid(u);

            Transporteurs utmp = new Transporteurs();

            TransEnAttenteRetour retour = new TransEnAttenteRetour();

            retour.setPhoto(u.getPhoto());
            retour.setNomcomplet(u.getNomcomplet());
            retour.setAdresse(u.getAdresse());
            retour.setUsername(u.getUsername());
            retour.setPhotopermis(tmp.getPhotopermis());
            retour.setStatus(tmp.getStatusdemande());

            transporteursList.add(retour);
        }

        return ResponseEntity.ok(transporteursList);
    }

    @GetMapping("/contactertransporteur/{transporteur}/{utilisateur}")
    public ResponseEntity<?> contactertransporteur(@PathVariable Transporteurs transporteur, @PathVariable Utilisateurs utilisateur){
        return transporteursService.contacter(transporteur, utilisateur);
    }

    @PostMapping("/acceptereservation/{reservation}")
    public ResponseEntity<?> acceptereRervation(@PathVariable Reservation reservation){
        return transporteursService.accepterRerservation(reservation);
    }

    @PostMapping("/rejetereservation/{reservation}")
    public ResponseEntity<?> rejetereRervation(@PathVariable Reservation reservation){
        return transporteursService.rejeterRerservation(reservation);
    }

    @PostMapping("/mettrefinreservation/{reservation}/{transporteur}")
    public ResponseEntity<?> mettreFinReservation(@PathVariable Reservation reservation, @PathVariable Transporteurs transporteur){
        return transporteursService.mettrefinAUneRerservation(transporteur, reservation);
    }

    @GetMapping("/recupererlesreservationencoursduntransporteur/{transporteur}")
    public ResponseEntity<?> recupererLesReservationEncoursDunTransporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(transporteur.getListreserveur());
    }

    @GetMapping("/reservationacceptetransporteur/{transporteur}")
    public ResponseEntity<?> reservationacceptetransporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByStatusAndTransporteur(EstatusDemande.ACCEPTER, transporteur));
    }

    @GetMapping("/reservationenattentetransporteur/{transporteur}")
    public ResponseEntity<?> reservationenattentetransporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByStatusAndTransporteur(EstatusDemande.ENATTENTE , transporteur));
    }

    @GetMapping("/reservationenrejetertransporteur/{transporteur}")
    public ResponseEntity<?> reservationenrejetertransporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByStatusAndTransporteur(EstatusDemande.REJETER, transporteur));
    }

    @GetMapping("/reservationenreencoursansporteur/{transporteur}")
    public ResponseEntity<?> reservationenreencoursansporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByStatusAndTransporteur(EstatusDemande.ENCOURS, transporteur));
    }

    @GetMapping("/reservationenterminertransporteur/{transporteur}")
    public ResponseEntity<?> reservationenterminertransporteur(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByStatusAndTransporteur(EstatusDemande.TERMINER, transporteur));
    }

    @GetMapping("/recupererlalisteDereservations/{transporteur}")
    public ResponseEntity<?> recupererLaListeDeReservations(@PathVariable Transporteurs transporteur){
        return ResponseEntity.ok(reservationRepository.findByTransporteur(transporteur));
    }

    @DeleteMapping("/annulerReservation/{transporteur}")
    public ResponseEntity<?> annulerReservation(@PathVariable Transporteurs  transporteur){

        Long rid = 0L;
        System.err.println(rid);
        List<Reservation> reservationList = transporteur.getListreserveur();
        for (Reservation r: reservationList){
            if(r.getStatus() == EstatusDemande.ENCOURS || r.getStatus() == EstatusDemande.ENATTENTE){
                rid = r.getId();
            }
        }

        try{
            reservationRepository.deleteById(rid);
            return ResponseEntity.ok(new Reponse("Reservation annul??e", 1));
        }catch (Exception e){
            return ResponseEntity.ok(new Reponse("Echec", 0));
        }
    }
}
