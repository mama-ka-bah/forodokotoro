package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AimePublicationRepository extends JpaRepository<AimePublication, Long> {
    AimePublication findByPublicationsAndUtilisateur(Publications publications, Utilisateurs utilisateurs);
    List<AimePublication> findByPublicationsAndAime(Publications publications, Boolean aime);

    List<AimePublication> findByPublications(Publications publications);
}
