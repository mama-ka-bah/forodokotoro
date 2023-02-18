package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Publications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationsRepositroy extends JpaRepository<Publications, Long> {
    Publications findByTitreOrSoustitre(String titre, String soustitre);

    List<Publications> findAllByOrderByDatepubDesc();
}
