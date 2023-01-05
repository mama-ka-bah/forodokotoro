package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Transporteurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteurRepository extends JpaRepository<Transporteurs, Long> {

}
