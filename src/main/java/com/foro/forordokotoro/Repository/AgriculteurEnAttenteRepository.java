package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.AgricuteurAttente;
import com.foro.forordokotoro.Models.Enumerations.EstatusDemande;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgriculteurEnAttenteRepository extends JpaRepository<AgricuteurAttente, Long> {

    List<AgricuteurAttente> findAllByOrderByDatedemandeAsc();

    List<AgricuteurAttente> findByStatusdemandeOrderByDatedemandeAsc(EstatusDemande estatusDemande);

    AgricuteurAttente findByUserid(Utilisateurs utilisateurs);

/*
    @Modifying
    @Transactional
    @Query(value = "insert into agriculteurs(id,photocarteidentite, statusdemande, datedemande) values (:id, :photocarteidentite, 'ENCOURS', :datedemande);", nativeQuery = true)
    int DEVENIRAGRICULTEUR(@Param("id") Long id, @Param("photocarteidentite") String photocarteidentite, @Param("datedemande") Date datedemande);

 */

}
