package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Commentaires;
import com.foro.forordokotoro.Models.Publications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaires, Long> {
    List<Commentaires> findByPublicationsOrderByDatepubDesc(Publications publications);
}
