package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.ProduitAgricole;
import com.foro.forordokotoro.Models.Varietes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface VarietesRepository extends JpaRepository<Varietes, Long> {
    Boolean existsByNom(String nom);
    Varietes findByNom(String nom);
    List<Varietes> findByProduitagricole(ProduitAgricole produitAgricole);
    List<Varietes> findByEtat(Boolean etat);


    //@Query(value = "select varietes.* FROM varites, varietes_previsions where varietes_previsions.varietes_id := varietes_id", nativeQuery = true)
    //List<Varietes> TROUVERLESPREVISIONSDUNEVARIETES(@Param("varietes_id") Long varietes_id);


}


