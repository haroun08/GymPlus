package com.smz.gymplus.repository;

import com.smz.gymplus.domain.PeriodicSubscription;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PeriodicSubscriptionRepositoryWithBagRelationshipsImpl implements PeriodicSubscriptionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PERIODICSUBSCRIPTIONS_PARAMETER = "periodicSubscriptions";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PeriodicSubscription> fetchBagRelationships(Optional<PeriodicSubscription> periodicSubscription) {
        return periodicSubscription.map(this::fetchIds);
    }

    @Override
    public Page<PeriodicSubscription> fetchBagRelationships(Page<PeriodicSubscription> periodicSubscriptions) {
        return new PageImpl<>(
            fetchBagRelationships(periodicSubscriptions.getContent()),
            periodicSubscriptions.getPageable(),
            periodicSubscriptions.getTotalElements()
        );
    }

    @Override
    public List<PeriodicSubscription> fetchBagRelationships(List<PeriodicSubscription> periodicSubscriptions) {
        return Optional.of(periodicSubscriptions).map(this::fetchIds).orElse(Collections.emptyList());
    }

    PeriodicSubscription fetchIds(PeriodicSubscription result) {
        return entityManager
            .createQuery(
                "select periodicSubscription from PeriodicSubscription periodicSubscription left join fetch periodicSubscription.ids where periodicSubscription.id = :id",
                PeriodicSubscription.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PeriodicSubscription> fetchIds(List<PeriodicSubscription> periodicSubscriptions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, periodicSubscriptions.size()).forEach(index -> order.put(periodicSubscriptions.get(index).getId(), index));
        List<PeriodicSubscription> result = entityManager
            .createQuery(
                "select periodicSubscription from PeriodicSubscription periodicSubscription left join fetch periodicSubscription.ids where periodicSubscription in :periodicSubscriptions",
                PeriodicSubscription.class
            )
            .setParameter(PERIODICSUBSCRIPTIONS_PARAMETER, periodicSubscriptions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
