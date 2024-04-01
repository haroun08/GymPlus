package com.smz.gymplus.repository;

import com.smz.gymplus.domain.ProductHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {}
