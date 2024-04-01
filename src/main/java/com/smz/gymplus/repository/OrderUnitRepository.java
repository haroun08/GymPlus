package com.smz.gymplus.repository;

import com.smz.gymplus.domain.OrderUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderUnitRepository extends JpaRepository<OrderUnit, Long> {}
