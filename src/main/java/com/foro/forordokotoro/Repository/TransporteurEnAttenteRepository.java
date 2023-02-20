package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Models.TransporteurAttente;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransporteurEnAttenteRepository extends JpaRepository<TransporteurAttente, Long> {
    TransporteurAttente findByUserid(Utilisateurs utilisateurs);

    List<TransporteurAttente> findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande estatusDemande);
}
