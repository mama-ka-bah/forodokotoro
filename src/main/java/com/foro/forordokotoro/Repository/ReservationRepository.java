package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
