package com.smz.gymplus.repository;

import com.smz.gymplus.domain.Period;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Period entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {}
