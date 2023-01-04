package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.ERole;
import com.foro.forordokotoro.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(ERole name);
}
