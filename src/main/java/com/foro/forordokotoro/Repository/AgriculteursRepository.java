package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.EstatusDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface AgriculteursRepository extends JpaRepository<Agriculteurs, Long> {

    Agriculteurs findByUsername(String username);

    //List<Agriculteurs> findAllByOrderByDatedemandeAsc();

    //List<Agriculteurs> findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande estatusDemande);


    @Modifying
    @Transactional
    @Query(value = "insert into agriculteurs(id,photocarteidentite) values (:id, :photocarteidentite);", nativeQuery = true)
    int DEVENIRAGRICULTEUR(@Param("id") Long id, @Param("photocarteidentite") String photocarteidentite);


    List<Agriculteurs> findByEtat(Boolean etat);

}
