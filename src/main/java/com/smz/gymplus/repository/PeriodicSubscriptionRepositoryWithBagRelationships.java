package com.smz.gymplus.repository;

import com.smz.gymplus.domain.PeriodicSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PeriodicSubscriptionRepositoryWithBagRelationships {
    Optional<PeriodicSubscription> fetchBagRelationships(Optional<PeriodicSubscription> periodicSubscription);

    List<PeriodicSubscription> fetchBagRelationships(List<PeriodicSubscription> periodicSubscriptions);

    Page<PeriodicSubscription> fetchBagRelationships(Page<PeriodicSubscription> periodicSubscriptions);
}
