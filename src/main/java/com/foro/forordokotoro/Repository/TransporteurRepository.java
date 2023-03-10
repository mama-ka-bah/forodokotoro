package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Transporteurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface TransporteurRepository extends JpaRepository<Transporteurs, Long> {

   // List<Transporteurs> findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande estatusDemande);

    Transporteurs findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "insert into transporteurs(id,disponibilite,photopermis,numeroplaque, nombrecontact) values (:id, :disponibilite, :photopermis, :numeroplaque, :nombrecontact);", nativeQuery = true)
    int DEVENIRTRANSPORTEUR(@Param("id") Long id, @Param("disponibilite") Boolean disponibilite, @Param("photopermis") String photopermis, @Param("numeroplaque") String numeroplaque, @Param("nombrecontact") Long nombrecontact);

    List<Transporteurs> findByEtat(Boolean etat);

    List<Transporteurs> findByDisponibilite(Boolean disponibilite);

    List<Transporteurs> findByIdIn(List<Long> idList);

    @Transactional
    @Query(value = "SELECT * FROM transporteurs where id = :id", nativeQuery = true)
    Transporteurs RECUPERERTRANSPORTEUR(@Param("id") Long id);

}
