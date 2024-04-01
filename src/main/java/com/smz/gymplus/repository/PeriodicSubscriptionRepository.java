package com.smz.gymplus.repository;

import com.smz.gymplus.domain.PeriodicSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PeriodicSubscription entity.
 *
 * When extending this class, extend PeriodicSubscriptionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PeriodicSubscriptionRepository
    extends PeriodicSubscriptionRepositoryWithBagRelationships, JpaRepository<PeriodicSubscription, Long> {
    default Optional<PeriodicSubscription> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PeriodicSubscription> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PeriodicSubscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
