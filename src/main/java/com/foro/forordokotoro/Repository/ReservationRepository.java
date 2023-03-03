package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Models.Reservation;
import com.foro.forordokotoro.Models.Transporteurs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusAndTransporteur(EstatusDemande status, Transporteurs transporteurs);

    List<Reservation> findByStatus(EstatusDemande status);

    List<Reservation> findByTransporteur(Transporteurs transporteurs);

}
