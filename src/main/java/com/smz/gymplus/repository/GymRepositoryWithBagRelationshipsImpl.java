package com.smz.gymplus.repository;

import com.smz.gymplus.domain.Gym;
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
public class GymRepositoryWithBagRelationshipsImpl implements GymRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String GYMS_PARAMETER = "gyms";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Gym> fetchBagRelationships(Optional<Gym> gym) {
        return gym.map(this::fetchIds);
    }

    @Override
    public Page<Gym> fetchBagRelationships(Page<Gym> gyms) {
        return new PageImpl<>(fetchBagRelationships(gyms.getContent()), gyms.getPageable(), gyms.getTotalElements());
    }

    @Override
    public List<Gym> fetchBagRelationships(List<Gym> gyms) {
        return Optional.of(gyms).map(this::fetchIds).orElse(Collections.emptyList());
    }

    Gym fetchIds(Gym result) {
        return entityManager
            .createQuery("select gym from Gym gym left join fetch gym.ids where gym.id = :id", Gym.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Gym> fetchIds(List<Gym> gyms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, gyms.size()).forEach(index -> order.put(gyms.get(index).getId(), index));
        List<Gym> result = entityManager
            .createQuery("select gym from Gym gym left join fetch gym.ids where gym in :gyms", Gym.class)
            .setParameter(GYMS_PARAMETER, gyms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
