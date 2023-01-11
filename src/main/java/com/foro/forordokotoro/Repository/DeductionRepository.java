package com.foro.forordokotoro.Repository;

import com.foro.forordokotoro.Models.DeductionStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductionRepository extends JpaRepository<DeductionStock, Long> {
}
