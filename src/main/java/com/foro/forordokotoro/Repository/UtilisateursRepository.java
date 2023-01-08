package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.Agriculteurs;
import com.foro.forordokotoro.Models.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateursRepository extends JpaRepository<Utilisateurs, Long> {
  Optional<Utilisateurs> findByUsername(String username);

  Boolean existsByUsername(String username);

  boolean existsById(Long id);

  Boolean existsByEmail(String email);

  List<Utilisateurs> findByEtat(Boolean etat);


  @Modifying
  @Transactional
  @Query(value = "UPDATE users SET profession = 'A' WHERE id = :id", nativeQuery = true)
  int DEVENIRAGRICULTEURDEPROFESSION(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE users SET profession = 'T' WHERE id = :id", nativeQuery = true)
  int DEVENIRTRANSPORTEURDEPROFESSION(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE users SET profession = 'Utilisateurs' WHERE id = :id", nativeQuery = true)
  int RETIRERTYPETRANSPORTEUROUAGRICULTEUR(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query(value = "insert into user_roles(user_id,role_id) values (:user_id, :role_id);", nativeQuery = true)
  int DONNERROLEAUSER(@Param("user_id") Long user_id, @Param("role_id") Long role_id);

}
